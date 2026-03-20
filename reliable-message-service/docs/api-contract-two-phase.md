# API Contract: Two-Phase Reliable Messaging and Outbox Pattern

## 1. Scope / 范围

**EN**  
This document defines core API contracts for two consistency architectures in cross-system business interaction (e.g., Order -> Bank):
- Two-Phase Reliable Messaging System (centralized middleware)
- Outbox Pattern (business-local transactional outbox)

**中文**  
本文档定义跨系统业务交互（例如订单 -> 银行）下两种一致性架构的核心 API 规约：
- Two-Phase 可靠消息系统（中心化中间件）
- Outbox 模式（业务本地事务 outbox）

---

## 2. Common Domain Fields / 通用领域字段

**EN**
- `messageId`: global idempotency key (required)
- `bizKey`: business key, e.g., orderNo (required)
- `eventType`: semantic event name, e.g., PAYMENT_DEDUCT_REQUESTED (required)
- `destination`: downstream MQ topic/queue (required for publish)
- `payload`: business payload JSON/string (required)
- `traceId`: tracing correlation key (optional but recommended)
- `status`: lifecycle status
- `sendTimes`, `maxRetryTimes`, `nextRetryAt`, `lastError`

**中文**
- `messageId`：全局幂等键（必填）
- `bizKey`：业务键，例如订单号（必填）
- `eventType`：事件语义名，例如 PAYMENT_DEDUCT_REQUESTED（必填）
- `destination`：下游 MQ topic/queue（发送时必填）
- `payload`：业务消息体 JSON/字符串（必填）
- `traceId`：链路追踪关联键（建议）
- `status`：生命周期状态
- `sendTimes`、`maxRetryTimes`、`nextRetryAt`、`lastError`

---

## 3. Two-Phase Reliable Messaging API / Two-Phase 可靠消息 API

### 3.1 Interaction Model / 交互模型

**EN**  
Upstream business service calls message system in phases:
1) `PrepareMessage`  
2) execute local business transaction  
3) `CommitMessage` or `RollbackMessage`  
Then message system relay publishes to downstream MQ.

**中文**  
上游业务系统按阶段调用消息系统：
1）`PrepareMessage`  
2）执行业务本地事务  
3）`CommitMessage` 或 `RollbackMessage`  
之后由消息系统 relay 投递下游 MQ。

### 3.2 gRPC Proto Draft (Two-Phase) / gRPC 草案（Two-Phase）

```proto
syntax = "proto3";

package rms.twophase.v1;
option java_multiple_files = true;
option java_package = "org.tus.tx.rms.twophase.v1";

service ReliableMessageService {
  rpc PrepareMessage(PrepareMessageRequest) returns (MessageAck);
  rpc CommitMessage(CommitMessageRequest) returns (MessageAck);
  rpc RollbackMessage(RollbackMessageRequest) returns (MessageAck);
  rpc GetMessageStatus(GetMessageStatusRequest) returns (MessageStatusResponse);

  // Optional: downstream reports final processing result to RMS
  rpc ReportConsumeResult(ReportConsumeResultRequest) returns (MessageAck);
}

message PrepareMessageRequest {
  string message_id = 1;
  string biz_key = 2;
  string event_type = 3;
  string destination = 4;
  string payload = 5;
  string trace_id = 6;
  int32 ttl_seconds = 7;
}

message CommitMessageRequest {
  string message_id = 1;
  string biz_key = 2;
  string trace_id = 3;
}

message RollbackMessageRequest {
  string message_id = 1;
  string reason = 2;
}

message GetMessageStatusRequest {
  string message_id = 1;
}

message ReportConsumeResultRequest {
  string message_id = 1;
  string biz_key = 2;
  string result_code = 3; // SUCCESS/FAIL/TIMEOUT
  string result_payload = 4;
  string error_code = 5;
  string error_message = 6;
}

message MessageAck {
  bool accepted = 1;
  string message = 2;
}

message MessageStatusResponse {
  string message_id = 1;
  string biz_key = 2;
  string event_type = 3;
  string status = 4; // PREPARED/COMMITTED/ROLLED_BACK/SENDING/SENT/DEAD
  int32 send_times = 5;
  int64 next_retry_at_epoch_ms = 6;
  string last_error = 7;
  int64 updated_at_epoch_ms = 8;
}
```

### 3.3 Status Semantics / 状态语义

**EN**
- `PREPARED`: pre-message accepted, waiting commit/rollback
- `COMMITTED`: confirmed by business, ready to dispatch
- `ROLLED_BACK`: canceled by business
- `SENDING`: scheduler selected for dispatch
- `SENT`: published successfully
- `DEAD`: exceeded retry policy

**中文**
- `PREPARED`：预消息已接收，等待提交/回滚
- `COMMITTED`：业务确认可发送，待投递
- `ROLLED_BACK`：业务回滚取消
- `SENDING`：已被调度器选中发送
- `SENT`：发送成功
- `DEAD`：超过重试策略

---

## 4. Outbox Pattern API / Outbox 模式 API

### 4.1 Interaction Model / 交互模型

**EN**  
Outbox is usually not middleware-first API. The active business service writes business state + outbox event in one local transaction, then local relay dispatches asynchronously.

**中文**  
Outbox 通常不是“中间件优先 API”。主动业务系统在同一本地事务写入业务状态 + outbox 事件，再由本地 relay 异步投递。

### 4.2 Suggested Internal Service API / 建议的内部服务 API

```proto
syntax = "proto3";

package rms.outbox.v1;
option java_multiple_files = true;
option java_package = "org.tus.tx.rms.outbox.v1";

// This can be internal gRPC or replaced by in-process service interface.
service OutboxRuntimeService {
  rpc AppendOutboxEvent(AppendOutboxEventRequest) returns (AppendOutboxEventResponse);
  rpc QueryOutboxEvent(QueryOutboxEventRequest) returns (OutboxEventView);
  rpc MarkOutboxReady(MarkOutboxReadyRequest) returns (SimpleAck);
}

message AppendOutboxEventRequest {
  string event_id = 1;       // == messageId
  string aggregate_type = 2; // ORDER
  string aggregate_id = 3;   // orderNo
  string biz_key = 4;
  string event_type = 5;
  string destination = 6;
  string payload = 7;
  string headers_json = 8;
  string trace_id = 9;
}

message AppendOutboxEventResponse {
  bool accepted = 1;
  string status = 2; // PENDING
}

message MarkOutboxReadyRequest {
  string event_id = 1;
}

message QueryOutboxEventRequest {
  string event_id = 1;
}

message OutboxEventView {
  string event_id = 1;
  string biz_key = 2;
  string event_type = 3;
  string status = 4; // PENDING/SENDING/SENT/RETRYING/DEAD
  int32 send_times = 5;
  int64 next_retry_at_epoch_ms = 6;
  string last_error = 7;
}

message SimpleAck {
  bool ok = 1;
  string message = 2;
}
```

### 4.3 Outbox Status Semantics / Outbox 状态语义

**EN**
- `PENDING`: inserted in same local transaction with business data
- `SENDING`: selected by relay for dispatch
- `SENT`: dispatch succeeded
- `RETRYING`: dispatch failed, retry scheduled
- `DEAD`: retry exhausted

**中文**
- `PENDING`：与业务数据同事务落库
- `SENDING`：被 relay 选中发送
- `SENT`：发送成功
- `RETRYING`：发送失败，进入重试
- `DEAD`：重试耗尽

---

## 5. Result Return Contract / 结果回传规约

**EN**
For Order -> Bank scenarios, preferred result return path is event-driven:
- Bank publishes `PAYMENT_DEDUCT_RESULT` event
- Order consumes and updates final order state idempotently

Optional alternatives:
- callback endpoint
- polling query API

**中文**
在订单 -> 银行场景，建议结果回传优先使用事件方式：
- 银行发布 `PAYMENT_DEDUCT_RESULT` 事件
- 订单幂等消费并更新最终状态

可选方式：
- callback 回调接口
- 轮询查询接口

---

## 6. API Governance Rules / API 治理规则

**EN**
1. `messageId/eventId` must be globally unique and idempotent.  
2. `bizKey + eventType` should be queryable for operations.  
3. Payload schema must be versioned (`eventType` + version in payload/header).  
4. Every API call should carry `traceId`/`traceparent` for observability.  
5. Status transitions must be monotonic and auditable.

**中文**
1. `messageId/eventId` 必须全局唯一、可幂等。  
2. `bizKey + eventType` 必须可运维查询。  
3. Payload 需要版本化（`eventType` + payload/header 版本）。  
4. 每次 API 调用应带 `traceId`/`traceparent`。  
5. 状态流转必须单调且可审计。

