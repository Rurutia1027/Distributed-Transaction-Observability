# Reliable Message System (RMS)

Spring Boot service that exposes **Two-Phase Reliable Messaging** and a **demo Outbox Runtime** over **gRPC**, with JPA persistence and a scheduled relay that publishes to a pluggable downstream (`DownstreamPublisher`; default is log-only).

## Contract & docs

- **`docs/design-order-bank-two-phase.md`** — Order ↔ Bank 场景与两阶段投递语义（中文，含 L1/L2 回执说明）。
- **`examples/order-bank-demo/`** — 上游 Order + 下游 Bank 两个独立工程（仅依赖 `message-service-api`）。
- **`docs/README-two-phase-reliable.md`** — Two-Phase Reliable Messaging (English).
- **`docs/README-outbox.md`** — Transactional Outbox pattern & gRPC contract notes (English).
- **`docs/api-contract-two-phase.md`** — canonical API description (prepare / commit / rollback / status / consume result + outbox append / query / mark ready).
- **`docs/schema-two-phase-vs-outbox.sql`** — reference DDL for `rms_message_tx`, `rms_message_result`, `rms_outbox_event`.
- **`docs/observability-otel-tracing-logging.md`** — tracing/logging design notes.

## Modules

| Module | Role |
|--------|------|
| **message-service-api** | Protobuf + generated Java / gRPC stubs (`org.tus.tx.rms.twophase.v1`, `org.tus.tx.rms.outbox.v1`). |
| **message-service** | Spring Boot app: JPA entities, application services, **gRPC on port 9090** (Netty), HTTP **8080** (actuator/health only unless you extend), `@Scheduled` relay. |

### gRPC services (port `9090`)

- **`ReliableMessageService`** — `PrepareMessage`, `CommitMessage`, `RollbackMessage`, `GetMessageStatus`, `ReportConsumeResult`.
- **`OutboxRuntimeService`** — `AppendOutboxEvent`, `QueryOutboxEvent`, `MarkOutboxReady` (outbox rows live in RMS DB for this demo).

### Runtime configuration

See `message-service/src/main/resources/application.yml`:

- **`grpc.server.port`** — gRPC (default `9090`).
- **`rms.relay.*`** — batch size, retry interval, scheduler fixed delay.
- **MySQL** — aligned with `docker-compose.yml` (`reliable_message`, user `rms`/`rms`).

## Build & run

```bash
cd reliable-message-service
mvn clean install -DskipTests
mvn spring-boot:run -pl message-service
```

Local infra:

```bash
docker compose up -d
```

Then start the app; ensure JDBC URL matches your MySQL host (compose maps `3306`).

### Docker: Two-Phase middleware (MySQL + RMS in containers)

Build and run **MySQL + Spring Boot RMS** (gRPC **9090**, HTTP **8080**):

```bash
cd reliable-message-service
docker compose -f docker-compose.twophase.yml up -d --build
```

- **Schema**: `db/init/twophase/*.sql` is mounted into MySQL `docker-entrypoint-initdb.d` and runs **only on first startup** (empty volume). Two-phase tables: `rms_message_tx`, `rms_message_result`. Re-init: `docker compose ... down -v` (drops data).
- **Hibernate**: `spring.jpa.hibernate.ddl-auto=none` (schema owned by SQL, not auto-DDL).
- **Outbox**: this compose sets `RMS_OUTBOX_ENABLED=false` (no `rms_outbox_event` yet). Local all-in-one: set `rms.outbox.enabled=true` and create the outbox table (see `docs/schema-two-phase-vs-outbox.sql` Part B / future `db/init/outbox`).

Requires Docker with Compose v2 (`depends_on: condition: service_healthy`). JDBC inside the app uses hostname `mysql` from this compose file.

**Run without Docker:** apply `db/init/twophase/01-schema.sql` to your MySQL `reliable_message` database, then start the app with the same `ddl-auto: none`.

## Implementation notes

- **Two-phase state machine**: `PREPARED` → `COMMITTED` / `ROLLED_BACK`; relay moves `COMMITTED` → `SENDING` → `SENT` or retries → `DEAD`.
- **Outbox (demo)**: `Append` creates `PENDING` with `ready_to_send = false`; `MarkOutboxReady` sets ready and schedules relay; `PENDING`/`RETRYING` → `SENDING` → `SENT` / `RETRYING` / `DEAD`.
- Replace **`LoggingDownstreamPublisher`** with a real RocketMQ/Kafka/Rabbit producer when integrating.

**Demo HTTP publisher (L1 ACK):** set `rms.downstream.publisher=http` so relay POSTs to the URL in `destination` (e.g. Bank ingress). See `docs/design-order-bank-two-phase.md`.

## Legacy README content

Earlier revisions described REST + `transaction_message` + Quartz; that design is **not** what this tree implements. Use the gRPC contracts above.
