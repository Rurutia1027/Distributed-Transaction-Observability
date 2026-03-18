# Cloud Native Reliable Message Service

This project is a refactored, standalone version of the original `roncoo-pay` messaging subsystem. It focuses purely on
**reliable message logging and delivery** to support **eventual consistency in distributed systems**.

## Modules

- **common-core**: Shared core utilities, base entities, exceptions, and common infrastructure used by all modules.
- **common-config**: Shared configuration (Spring Boot, MQ, Hibernate, etc.) used by the message service.
- **message-service-api**: Public API for the reliable message service (entities, enums, and service interfaces).
- **message-service**: Implementation of the reliable message service, including:
    - Persistence of transactional message to the `rp_transaction_message` table
    - Tracking
    - Tracking send attempts, dead-letter status, and message state
    - Integration with MQ (e.g., RocketMQ), Consul for service discovery, and gRPC for RPC

## Purpose

The service implements a **reliable messaging** pattern to achieve **eventual consistency** across microservices:

- Message are first **persisted to the database** with status and send-attempt counters.
- A background process **retries sending** messages until success or until they are marked as dead.
- Downstream service consume messages from MQ, ensuring that business actions are eventually applied even when remote
  systems are temporarily unavailable.

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

This module is intended to run as a Spring Boot microservice with external dependencies:

- **MySQL**: persistent store for `rp_transaction_message`
- **RocketMQ**: message broker for publish/consume
- **Consul**: service discovery
- **OpenTelemetry Collector**: telemetry pipeline

#### Docker Compose (local dev)

The recommended `docker-compose` topology:

- `mysql` (with a volume)
- `rocketmq-namesrv`, `rocketmq-broker` (with a volume)
- `consul`
- `otel-collector`
- `message-service` (built image) configured with:
    - `SPRING_DATASOURCE_*` for MySQL
    - RocketMQ client config
    - Consul discovery config
    - `OTEL_EXPORTER_OTLP_ENDPOINT` pointing to `otel-collector`

#### Kubernetes (production-like)

Recommended Kubernetes layout:

- **Stateful dependencies** (usually managed services in real prod):
    - MySQL (or RDS) + migration/bootstrap for `rp_transaction_message`
    - RocketMQ cluster (or managed MQ)
    - Consul (or replace with Kubernetes-native discovery if you later decide)
- **Observability**:
    - OpenTelemetry Collector as a Deployment (or DaemonSet) + ConfigMap
- **Application**:
    - `message-service` Deployment
    - Service (ClusterIP) exposing gRPC
    - Config via ConfigMap + Secret
    - HPA scaling on CPU + business metrics (optional)

Operational considerations:

- **Idempotency**: consumers should be idempotent; message table status transitions must tolerate retries.
- **Outbox pattern**: DB write + publish should be made reliable (transactional outbox + relay job).
- **Backpressure**: protect DB and MQ with bounded retry policies and dead-letter handling.
