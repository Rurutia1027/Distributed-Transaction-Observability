# Transactional Outbox (API & integration)

English guide to the **outbox** pattern as modeled in this repository. In the **canonical** pattern, the outbox table lives in the **same database** as business data so **business writes + outbox insert** commit in **one local transaction**; a **relay** publishes rows to messaging infrastructure.

## When to use it

- You need **atomicity**: “persist business state” and “record an event to publish” must **commit or roll back together**.
- You prefer **no distributed transaction** between app and broker: publish happens **after** commit, asynchronously.
- You can run a **relay** (same process, sidecar, or separate worker) that reads the outbox table and calls `DownstreamPublisher` / MQ.

## Canonical pattern (recommended production shape)

1. Inside the **business service** `@Transactional` method: `UPDATE/INSERT` business tables **and** `INSERT` into **`rms_outbox_event`** (or your schema equivalent).
2. After commit, a **relay** polls `PENDING` rows that are **ready to send** (`ready_to_send = true` or equivalent), publishes, then updates status to `SENT` / `RETRYING` / `DEAD`.

**Important:** For **true** transactional outbox, the **append** should use the **same JDBC connection / transaction** as business logic (in-process repository or SQL), **not** a separate remote RPC in the middle of that transaction.

## gRPC as a contract (this repo’s demo)

This project also exposes **`OutboxRuntimeService`** over gRPC for **integration testing**, **demos**, or **non–strict-transaction** deployments where RMS holds the outbox table:

- **`AppendOutboxEvent`** — insert `PENDING` (demo: may be remote).
- **`MarkOutboxReady`** — mark row ready and schedule relay (`ready_to_send`, `next_retry_at`).
- **`QueryOutboxEvent`** — operational visibility.

**Proto:** `message-service-api/src/main/proto/rms_outbox_v1.proto`  
**Package / Java:** `rms.outbox.v1` → `org.tus.tx.rms.outbox.v1`

### Remote gRPC vs local transaction

| Approach | Atomic with business DB? | Typical use |
|----------|---------------------------|-------------|
| Business writes outbox **in-process** in same transaction | **Yes** | Production transactional outbox |
| Business calls **gRPC** to RMS to append | **No** (separate service/transaction) | Central RMS DB, or eventual-consistency acceptable |

If you **share the DB** between business and RMS but still use gRPC for append, you still have **two transactions** unless you design otherwise—so document the consistency level explicitly.

## Status semantics (reference)

| Status | Meaning |
|--------|---------|
| `PENDING` | Stored; not yet successfully published (in canonical model, inserted with business commit) |
| `SENDING` | Relay selected row for publish |
| `SENT` | Publish succeeded |
| `RETRYING` | Publish failed; backoff / `next_retry_at` |
| `DEAD` | Retries exhausted |

This demo also uses **`ready_to_send`**: events are not relayed until marked ready (simulates “after transaction visibility” or explicit flush).

## Persistence

- Table: **`rms_outbox_event`** — see `docs/schema-two-phase-vs-outbox.sql`.

## Client integration

- **Strict outbox:** share the **entity/repository/SQL** for `rms_outbox_event` inside the business app (or a small **`outbox-core`** library), same `DataSource` as domain entities.
- **gRPC client:** depend on **`message-service-api`**, use **`OutboxRuntimeServiceGrpc`** stubs when talking to a remote RMS that owns the table.

**Idempotency:** Re-use the same `event_id` with identical append fields where your contract requires idempotent registration.

## Related docs

- **`docs/api-contract-two-phase.md`** — outbox section + governance rules.
- **`docs/README-two-phase-reliable.md`** — centralized two-phase alternative.
- **`README.md`** (repo root) — build, run, relay configuration.
