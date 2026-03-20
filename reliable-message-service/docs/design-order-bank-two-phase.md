# Order ↔ Bank：Two-Phase 可靠消息（简易设计）

> 范围：**仅 Two-Phase + 中间件 RMS**；**不包含 Outbox**。  
> 目标：用「下单 → 请求银行扣款」说明 **主动方 Order**、**被动方 Bank**、**RMS**、**队列归属** 与 **异步投递 API 语义**。

---

## 1. 业务原型

- **Order（上游主动方）**：创建订单后，通过 **RMS 两阶段** 登记「请求银行扣款」意图；本地事务提交后再 **Commit**，由 RMS 异步投递。
- **Bank（下游被动方）**：在**自己系统边界内**消费「扣款指令」消息，更新账务/支付状态；处理完成后通过 **回传**（本设计：gRPC `ReportConsumeResult` 或后续事件）通知结果。

---

## 2. 系统与队列归属

```
Order ──gRPC──► RMS ──DownstreamPublisher──► [ Bank 拥有的入口 ]
                                                  │
                                                  ├─ 生产：RocketMQ/Kafka Topic（推荐）
                                                  └─ 演示：HTTP 入站（仅本地联调）
                                                      Bank 服务 URL
```

- **逻辑上**：Topic / Subscription **归 Bank 团队治理**（消费者部署在 Bank、消费组 ID、重试与死信策略在 Bank）。
- **技术上**：RMS 的 `destination` 字段填写 **Bank 侧约定的 Topic 名** 或 **Bank 提供的入站 URL（仅演示）**；`DownstreamPublisher` 负责把消息送进去。
- **RMS 不负责** Bank 业务规则；只保证「已 Commit 的消息尽力投递 + 可观测状态」。

---

## 3. 两阶段交互主流程（主角）

| 步骤 | 参与方 | 说明 |
|------|--------|------|
| 1 | Order → RMS | `PrepareMessage`：`message_id` 幂等键、`biz_key`=订单号、`event_type`=`PAYMENT_DEDUCT_REQUESTED`、`destination`、`payload`（JSON）、`trace_id` |
| 2 | Order | **本地事务**：插入/更新订单状态（如 `PAYMENT_IN_FLIGHT`）等 |
| 3a | Order → RMS | 成功：`CommitMessage` → RMS 标记 `COMMITTED`，进入 relay |
| 3b | Order → RMS | 失败：`RollbackMessage` |
| 4 | RMS | 调度器 `relay`：`COMMITTED` → `DownstreamPublisher.publish(...)` |
| 5 | Bank | 从队列（或演示 HTTP）收到消息，**幂等**处理（按 `message_id` / 订单号） |
| 6 | Bank → RMS（可选） | `ReportConsumeResult`：审计 / 对账 / 触发 Order 侧轮询或后续 Saga |

**注意**：步骤 1 与步骤 2 的顺序在业界有两种习惯（先 Prepare 再本地 TX，或先本地 TX 再 Prepare）；本仓库 RMS 实现是 **先 Prepare，再本地事务，再 Commit/Rollback**，与 `docs/api-contract-two-phase.md` 一致。

---

## 4. 「异步投递」与 API 语义（当前缺口与建议）

投递到 MQ **在 RMS 内部是异步任务**；但 **一次 `publish` 调用** 仍可区分两层反馈：

| 层级 | 含义 | 典型实现 | 与现 API 关系 |
|------|------|-----------|----------------|
| **L1：投递回执（Transport / Broker ACK）** | 中间件把消息交给了 **Broker**（或演示里 **Bank HTTP 返回 2xx**） | MQ producer `send` 成功；HTTP POST 成功 | RMS 将状态置为 `SENT`、失败则重试/`DEAD`，**已覆盖「发布是否成功」** |
| **L2：业务处理结果（End-to-End）** | Bank **是否扣款成功** | Bank 处理完后调 `ReportConsumeResult` 或发 **结果事件** | 用 **`ReportConsumeResult`** 或 **独立事件**；**不是** `CommitMessage` 的同步返回值 |

**结论**：

- **不要求**在 `DownstreamPublisher.publish()` 的 Java 方法上再设计一套「新业务 API」才能异步；异步的是 **Bank 处理**，不是 Broker 接收。
- **建议在产品上显式区分**：  
  - **Order** 通过 `GetMessageStatus` / 订单状态机知道 **是否已从 RMS 发出**（L1）；  
  - **最终结果** 通过 **Bank 回调 RMS**（`ReportConsumeResult`）或 **Bank → Order 领域事件**（推荐长期形态）完成（L2）。
- 若将来要在 **gRPC 层**增加「投递任务 ID + 查询投递尝试明细」，可扩展表 `rms_dispatch_log`（文档 `schema-two-phase-vs-outbox.sql` 已有草案），与业务结果解耦。

---

## 5. 最小数据模型（MySQL）

### 5.1 Order 库（上游）

| 表 | 字段（示例） | 说明 |
|----|----------------|------|
| `demo_order` | `id`, `order_no` UK, `status`, `amount`, `created_at`, `updated_at` | `status`: `CREATED` → `PAYMENT_REQUESTED` → `CONFIRMED` / `FAILED`（可按演示简化） |

### 5.2 Bank 库（下游）

| 表 | 字段（示例） | 说明 |
|----|----------------|------|
| `demo_payment_record` | `id`, `message_id`, `order_no`, `amount`, `status`, `trace_id`, `raw_payload`, `created_at` | 幂等键：`message_id` 或 `(order_no, event)` |

**不做**抽象层：Entity + Repository 直接映射即可。

---

## 6. 两个 Maven 工程（彼此不依赖）

| 工程 | 职责 | 允许依赖 |
|------|------|-----------|
| `upstream-order-service` | REST 演示下单 + 调 RMS gRPC | **仅** `message-service-api`（协议）+ Spring + MySQL + gRPC 客户端运行时 |
| `downstream-bank-service` | 接收「队列」消息（演示为 HTTP）+ 可选 `ReportConsumeResult` | **仅**同上协议模块 + Spring + MySQL |

Order **不依赖** Bank 的 jar；Bank **不依赖** Order 的 jar。  
**Payload** 使用约定 JSON 字符串（可在设计文档中固定示例）；不强制共享 Java DTO 模块。

代码位置：`examples/order-bank-demo/`（与 RMS 同仓库，**不**挂入 RMS 父 `pom.xml` modules，避免版本强绑定；本地先 `mvn install` RMS 的 `message-service-api`）。

---

## 7. 本地联调顺序（HTTP 模拟队列）

1. MySQL：两个库或同一实例两个 schema（演示可用同一库不同表前缀）。  
2. 启动 **RMS**（`message-service`），配置：  
   - `rms.downstream.publisher=http`  
   - `PrepareMessage` 里 `destination` 填 Bank 入站完整 URL，例如 `http://localhost:8082/api/bank/payment-commands`  
3. 启动 **Bank**（8082）。  
4. 启动 **Order**（8081），配置 RMS 地址 `localhost:9090`。  
5. 调用 Order 的下单接口 → 观察 RMS 日志 / `GetMessageStatus` → Bank 库表写入。

生产环境将 `DownstreamPublisher` 换为 **RocketMQ/Kafka** 实现，`destination` 改为 **Bank 订阅的 Topic**。

---

## 8. 与 Outbox 的边界

- **Outbox** 解决的是 **Order 库与「发消息」同事务**；本设计 **刻意不展开**。  
- 后续你可单独仓库实现：**Order 进程内写 outbox 表** + **Relay**；与本文 RMS Two-Phase **二选一或组合**（产品选型问题）。

---

## 9. 参考

- `docs/api-contract-two-phase.md`  
- `docs/README-two-phase-reliable.md`  
- `examples/order-bank-demo/README.md`  
