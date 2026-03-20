# Two-Phase Reliable Messaging

Guide to the **centralized** two-phase reliable message flow in this repository. The message system (RMS) owns
persistence and delivery; upstream business coordinates with **Prepare -> local business transaction -> Commit or
Rollback**.

## When to use it

- You want a **dedicated message middleware** that stores intent **before** the business finalizes its local
  transaction.
- Cross-service flows (e.g., Order -> Bank) where the business first **reserves** or **registers** a message, then *
  *commit** or **rolls back** after its own DB work.
- You accept that **Prepare / Commit** are separate RPCs to RMS (not the same JDBC transaction as business tables unless
  you add extra coupling).

## Interaction model

1. **`PrepareMessage`** - RMS records the message as `PREPARED` (idempotent on same `message_id` + same payload fields).
2. **Business local transaction** - update your domain tables as usual.
3. **`CommitMessage`** or **`RollbackMessage`** - business tells RMS to mark the message `COMMITTED` (eligible for
   relay) or `ROLL_BACK` (discarded).
4. **Relay** - RMS asynchronous publishes `COMMITTED` messages to the downstream destination (MQ topic/queue, etc.) via
   `DownstreamPublisher`.

Optional:

- **`GetMessageStatus`** - observe lifecycle / retries / errors.
- **`ReportConsumeResult`** - downstream or orchestrator records final processing outcome in RMS (audit/callbacks).

## gRPC API

- **Package/Java**: `rms.twophase.v1` -> `org.tus.tx.rms.twophase.v1`
- **Service**: `ReliableMessageService`
- **Proto**: `message-service-api/src/main/proto/rms_twophase_v1.proto`

| RPC                   | Purpose                                       |
|-----------------------|-----------------------------------------------|
| `PrepareMessage`      | Register payload + routing; status `PREPARED` |
| `CommitMessage`       | Confirm send; status `COMMITTED`              |
| `RollbackMessage`     | Cancel; status `ROLLED_BACK`                  |
| `GetMessageStatus`    | Read status, retries, `next_retry_at`, errors |
| `ReportConsumeResult` | Persist consumer / handler result             |

## Status semantics (reference)

| Status        | Meaning                                      |
|---------------|----------------------------------------------|
| `PREPARED`    | Accepted by RMS; awaiting commit or rollback |
| `COMMITTED`   | Business confirmed; relay may publish        |
| `ROLLED_BACK` | Canceled; not published                      |
| `SENDING`     | Selected by relay for publish attempt        |
| `SENT`        | Published successfully                       |
| `DEAD`        | Retries exhausted                            |

## Persistence

- Table: **`rms_message_tx`**
- Results / audit: **`rms_message_result`** (when using `ReportConsumeResult`)

## Client integration

1. Add dependency on **`message-service-api`** (same version as RMS).
2. Build a gRPC channel to RMS (`grpc.server.port`, default: **9090**).
3. Use the generated **`ReliableMessageServiceGrpc**` blocking/async stubs from business code.

**Idempotency**: Re-sending the same `message_id` with **identical** prepare fields should be accepted without duplicate
side effects.

## Limitations / design note

Two-phase here coordinates **business decision** with **RMS state** via RPC. It does **not** by itself guarantee that
your business DB commit and RMS commit are one atomic transaction across database. If you need strict single-transaction
guarantees with business data, compare with the **transactional outbox** pattern. 




