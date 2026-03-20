# OpenTelemetry Tracing + Logging (End-to-End) Design
（基于当前架构的端到端 OpenTelemetry 链路追踪 + 日志方案设计）

This document defines an end-to-end tracing/logging strategy for the current **Reliable Message Service** architecture:

- Producer → **HTTP REST** → message-service
- message-service → **DB (Outbox)** persist
- Quartz **Outbox Relay** scans → publishes to **RocketMQ**
- Optional: Consumer processing (outside this repo, but we define propagation requirements)

> Goal: every business event has a single **trace** that connects HTTP request, outbox row, relay execution, RocketMQ publish, and downstream consumption. Logs must be **correlated** with trace/span IDs.

---

## 1. Goals / 非目标

### Goals
- **Trace continuity** across: HTTP → DB(outbox) → relay job → MQ publish → downstream consumer
- **Consistent span naming** and attributes for dashboards/alerts
- **Log correlation**: each log line includes `trace_id` / `span_id`
- **Low operational complexity**: deploy once (Collector), standard exporters, minimal custom code

### Non-goals
- Choosing a specific backend vendor (Jaeger/Tempo/OTLP SaaS). We keep it pluggable.
- Full SLO/alert catalog (only foundational signals and recommended metrics).

---

## 2. Terminology / 术语

- **Trace**: a tree of spans for one end-to-end operation.
- **Span**: a timed operation segment (HTTP request, DB query, MQ publish).
- **Context propagation**: transferring trace context between processes (HTTP headers / MQ message properties).
- **Resource attributes**: service identity (`service.name`, `deployment.environment`, etc).

---

## 3. Trace model (what we trace) / 链路模型（追什么）

### 3.1 Entry: HTTP API (producer → message-service)

Operations:
- `POST /messages` (persist outbox, status = `WAITING_CONFIRM`)
- `POST /messages/{id}/confirm` (status → `SENDING`, schedule for relay)
- `GET /messages` (admin query)

Span model:
- Server span: `HTTP {method}` (auto-instrumented)
- Internal spans:
  - `rms.outbox.persist` (create outbox row)
  - `rms.outbox.confirm` (confirm transition)

Key attributes (recommended):
- `rms.message_id`
- `rms.consumer_queue`
- `rms.status_before`, `rms.status_after`
- `http.route`, `http.method`, `http.status_code`

### 3.2 DB / Outbox

We trace DB operations via:
- auto-instrumentation for JDBC/Hibernate (preferred)
- plus one semantic span around state transitions

Semantic spans:
- `rms.outbox.persist`
- `rms.outbox.state_transition`
- `rms.outbox.scan_due` (relay scanning)

Attributes:
- `db.system=mysql`
- `rms.outbox.table=transaction_message`
- `rms.rows` (scan batch size)

### 3.3 Quartz relay (message-service internal)

Relay is the boundary between persistence and MQ.

Span model:
- `rms.relay.tick` (one job execution)
  - `rms.outbox.scan_due`
  - For each message (or batch):
    - `rms.mq.publish` (child span)
    - `rms.outbox.update_after_publish`

Attributes:
- `rms.job_name=outboxRelayJob`
- `rms.batch_size`
- `rms.message_id`
- `messaging.system=rocketmq`
- `messaging.destination` (topic/queue)
- `messaging.operation=publish`

### 3.4 RocketMQ publish and downstream consume

We must propagate trace context into MQ message metadata.

Propagation contract (recommended):
- **W3C Trace Context**:
  - `traceparent`
  - `tracestate` (optional)
- Optional baggage keys:
  - `rms.message_id`

Where to store in RocketMQ:
- message user properties (preferred) or headers depending on client library.

Downstream consumer must:
- extract `traceparent` and continue trace
- create consumer span: `rms.mq.consume`

---

## 4. Logging model (how logs correlate) / 日志模型（如何关联）

### 4.1 Required fields in logs / 日志必备字段

All services (producer, message-service, consumer) should print:
- `trace_id`
- `span_id`
- `service.name`
- `env` (dev/stage/prod)
- `message_id` (when available)

### 4.2 Java/Spring Boot approach / Java 侧做法

Preferred (low code):
- Use **OpenTelemetry Java Agent** + logback MDC integration.

Logback pattern example:
- include `%X{trace_id}` and `%X{span_id}` (agent injects into MDC)

If you cannot use agent:
- Use OTel SDK + `logback-mdc` bridge (more work, avoid unless required).

---

## 5. Implementation plan / 落地方案

### 5.1 Deployment topology / 部署拓扑

Recommended:
- Each JVM exports OTLP to a local/sidecar or cluster Collector:
  - `OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector:4318` (HTTP/protobuf)
- Collector exports to:
  - tracing backend (Jaeger/Tempo/OTLP SaaS)
  - logs backend (Loki/ELK/OTLP logs) OR keep logs in stdout and only correlate via IDs
  - metrics backend (Prometheus remote-write / OTLP metrics)

### 5.2 Service identity / 服务标识

Resource attributes:
- `service.name=message-service`
- `service.namespace=tx`
- `deployment.environment=local|dev|prod`

Set via env vars (recommended):
- `OTEL_SERVICE_NAME=message-service`
- `OTEL_RESOURCE_ATTRIBUTES=service.namespace=tx,deployment.environment=local`

### 5.3 Spring Boot instrumentation / Spring Boot 采集方式

Option A (recommended): **Java Agent**
- Zero/low code, captures HTTP/JDBC, common libs.
- Start with:
  - `-javaagent:/path/opentelemetry-javaagent.jar`

Option B: Spring Boot Micrometer Tracing + OTel bridge
- Works, but more configuration; choose only if you need deep Spring-native customizations.

Given this repo emphasizes simplicity, choose **Option A**.

### 5.4 Manual spans (only where needed) / 必要的手动埋点

Even with agent, we should add minimal semantic spans around outbox transitions and relay publish to make dashboards meaningful.

Minimum recommended manual spans in message-service:
- around `createWaitingConfirm(...)`
- around confirm transition
- around relay tick + publish

> Note: this requires adding `io.opentelemetry:opentelemetry-api` and using `Tracer`.
> If you want to avoid adding code dependencies now, we can defer manual spans and rely on HTTP/JDBC spans first.

### 5.5 RocketMQ propagation / RocketMQ 透传

Producer side (message-service relay):
- inject `traceparent` into message properties before publish

Consumer side:
- extract context and start consumer span

If RocketMQ client lacks standard OTel instrumentation, we implement a small wrapper:
- `TraceContextPropagator` for set/get string properties

---

## 6. Configuration templates / 配置模板

### 6.1 `message-service` env vars (local)

```bash
export OTEL_SERVICE_NAME="message-service"
export OTEL_RESOURCE_ATTRIBUTES="service.namespace=tx,deployment.environment=local"
export OTEL_TRACES_EXPORTER="otlp"
export OTEL_METRICS_EXPORTER="otlp"
export OTEL_LOGS_EXPORTER="otlp"   # optional (see section 6.3)
export OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:4318"
```

### 6.2 Collector minimal config (example)

```yaml
receivers:
  otlp:
    protocols:
      http:
      grpc:

processors:
  batch:

exporters:
  otlp:
    endpoint: your-backend:4317
    tls:
      insecure: true

service:
  pipelines:
    traces:
      receivers: [otlp]
      processors: [batch]
      exporters: [otlp]
    metrics:
      receivers: [otlp]
      processors: [batch]
      exporters: [otlp]
```

### 6.3 Logging export choices / 日志导出选择

Choice 1 (recommended initially): **stdout logs + trace correlation**
- Keep application logs to stdout (K8s/container log collection)
- Only export traces/metrics to OTel backend
- Benefit: simplest, robust

Choice 2: **OTel logs pipeline**
- Export logs via OTel SDK/agent to Collector and then to Loki/Elastic
- Benefit: unified pipeline; cost/complexity higher

---

## 7. Naming conventions / 命名规范

Span names:
- `rms.outbox.persist`
- `rms.outbox.confirm`
- `rms.outbox.scan_due`
- `rms.relay.tick`
- `rms.mq.publish`
- `rms.mq.consume` (consumer side)

Attributes prefix:
- Custom business attrs use `rms.*`
- Standard messaging attrs use `messaging.*` where applicable

---

## 8. Dashboard & troubleshooting guide / 看板与排障

### Typical debugging flow
1) Start from producer log line → get `trace_id`
2) Search trace in backend → locate HTTP server span in message-service
3) Check outbox persist span → verify state transition
4) Jump to relay tick span → verify publish happened
5) If downstream exists, ensure consumer span continues the same trace

### Common failures
- **Trace breaks at MQ**: propagation not implemented → fix inject/extract
- **No DB spans**: agent not attached or JDBC not instrumented
- **Logs missing trace_id**: logback pattern not using MDC keys or agent MDC integration not enabled

---

# 中文版（与英文一致）

## 1. 目标 / 非目标
- **目标**：将 producer → HTTP → message-service → outbox(DB) → Quartz relay → RocketMQ → consumer 串成一条 trace；日志能通过 `trace_id/span_id` 关联。
- **非目标**：绑定特定可观测性厂商；完整告警/SLO 体系。

## 2. 端到端链路应该包含哪些 span
- HTTP 入口：自动采集 `HTTP {method}`
- 语义 span（建议补充）：
  - `rms.outbox.persist`（落库）
  - `rms.outbox.confirm`（确认发送）
  - `rms.relay.tick`（一次调度周期）
  - `rms.mq.publish`（发送到 RocketMQ）
  - `rms.mq.consume`（消费侧，需下游配合）

## 3. MQ 透传（关键）
- 统一采用 **W3C Trace Context**：`traceparent` / `tracestate`
- message-service relay 在发送消息时把这些字段写入 RocketMQ 消息 properties
- consumer 在消费时提取并继续 trace

## 4. 日志关联
- 每行日志至少包含：`trace_id` / `span_id` / `service.name` / `env` / `message_id(可选)`
- 最推荐：**OTel Java Agent + logback MDC**，几乎零侵入。

## 5. 落地选型建议
- 优先使用 **OpenTelemetry Java Agent**（简单、覆盖 HTTP/JDBC 等）
- Collector 作为统一入口，后端随时可替换（Jaeger/Tempo/OTLP SaaS）
- 日志先走 stdout + trace 关联；需要统一日志链路再上 OTel logs pipeline

