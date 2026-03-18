# Cloud Native Reliable Message Service

This project is a refactored, standalone version of the original `roncoo-pay` messaging subsystem. It focuses purely on
**reliable message logging and delivery** to support **eventual consistency in distributed systems**.

## Modules

- **message-service-api**: gRPC contract (proto) + generated Java stubs for server and client. Callers use this module to talk to the service via gRPC (publish, confirm, list).
- **message-service**: Spring Boot application that implements the reliable message service:
    - **gRPC server** (default port 9090): main API for producers (Publish, ConfirmSend, ListPage).
    - **REST + Swagger** (port 8080): optional admin/query endpoints.
    - Persistence to `transaction_message` (outbox), state machine (WAITING_CONFIRM → SENDING → SENT/DEAD).
    - **Quartz** relay job to send outbox messages to **RocketMQ**.

## Purpose

The service implements a **reliable messaging** pattern to achieve **eventual consistency** across microservices:

- Messages are first **persisted to the database** (outbox) with status and send-attempt counters.
- A background process **retries sending** messages until success or until they are marked as dead.
- Downstream services consume messages from RocketMQ, ensuring that business actions are eventually applied even when remote systems are temporarily unavailable.

## Docs

- `docs/observability-otel-tracing-logging.md`: End-to-end OpenTelemetry tracing + logging design (EN + 中文).

### Observability (OpenTelemetry)

Target observability stack (logging + tracing + metrics):

- **Instrumentation**: OpenTelemetry SDK + auto-instrumentation where possible (Spring Boot, JDBC/Hibernate, gRPC).
- **Traces**: Export OTLP to a collector; correlate message lifecycle spans (persist -> publish -> consume ->
  ack/retry/dead).
- **Metrics**: Export OTLP metrics; track send/consume rates, retry counts, dead-letter counts, DB latency, MQ latency.
- **Logs**: Keep structured logs and include trace/span context for correlation (traceId/spanId).

Recommended pipeline:

- App -> **OpenTelemetry Collector** (OTLP) -> backend(s)
    - Traces: Jaeger / Tempo
    - Metrics: Prometheus (via collector) / OTLP-capable backend / Micrometer App side
    - Logs: Loki

### Deployment design

The service runs as a standalone Spring Boot app with external dependencies:

- **MySQL**: persistent store for `transaction_message` (outbox).
- **RocketMQ**: message broker; relay job publishes to it.
- **OpenTelemetry Collector** (optional): for tracing/logging; see `docs/observability-otel-tracing-logging.md`.

#### Docker Compose (local dev)

The repo includes `docker-compose.yml` for local dev infrastructure only (no app container; run the Spring Boot app on the host):

| Service            | Image                 | Ports              | Notes |
|--------------------|-----------------------|--------------------|--------|
| `mysql`            | mysql:8.4             | 3306               | DB `reliable_message`, user `rms`/`rms` |
| `rocketmq-namesrv` | apache/rocketmq:5.3.0 | 9876               | `platform: linux/amd64` for arm64 hosts (e.g. M1/M2) |
| `rocketmq-broker`  | apache/rocketmq:5.3.0 | 10911, 10909       | Depends on namesrv; `autoCreateTopicEnable=true` |

Run:

```bash
cd reliable-message-service
docker compose up -d
```

Then start the app (e.g. from IDE or `mvn spring-boot:run -pl message-service`). The app connects to:

- MySQL at `localhost:3306`
- RocketMQ at `127.0.0.1:9876` (see `application.yml`: `mq.rocketmq.name-server`)

App ports: **HTTP 8080** (REST/Swagger), **gRPC 9090**.

#### Kubernetes (production-like)

Recommended layout:

- **Stateful dependencies** (managed services or Helm):
    - MySQL (or RDS/TiDB) + schema for `transaction_message`
    - RocketMQ cluster (or managed MQ)
- **Observability** (optional): OpenTelemetry Collector (Deployment or DaemonSet) + ConfigMap.
- **Application**:
    - `message-service` Deployment (single Spring Boot JAR).
    - Service (ClusterIP) exposing **gRPC 9090** and **HTTP 8080** (REST/Swagger).
    - Config via ConfigMap + Secret (DB URL, MQ name-server, etc.).
    - HPA on CPU or custom metrics (optional).

Operational considerations:

- **Idempotency**: consumers should be idempotent; message table status transitions must tolerate retries.
- **Outbox pattern**: DB write + publish should be made reliable (transactional outbox + relay job).
- **Backpressure**: protect DB and MQ with bounded retry policies and dead-letter handling.
