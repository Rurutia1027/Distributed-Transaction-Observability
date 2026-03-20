# Order ↔ Bank Two-Phase demo

Two **separate** Spring Boot apps (no Maven dependency between them). Both may depend on **`message-service-api`** (RMS gRPC contract) only.

## Prerequisite

Install RMS API into local Maven repo (from repo root `reliable-message-service/`):

```bash
mvn install -pl message-service-api -am -DskipTests
```

Create tables (same MySQL as RMS or any DB you configure). Script: `db/01-demo-tables.sql`.

## Run RMS (with HTTP publisher → Bank)

```bash
# in reliable-message-service/message-service
export RMS_DOWNSTREAM_PUBLISHER=http
mvn spring-boot:run
```

Ensure `PrepareMessage` / relay uses `destination` = `http://localhost:8082/api/bank/payment-commands` (Order app default).

## Run Bank (8082)

```bash
cd examples/order-bank-demo/downstream-bank-service
mvn spring-boot:run
```

## Run Order (8081)

```bash
cd examples/order-bank-demo/upstream-order-service
mvn spring-boot:run
```

## Try

```bash
curl -s -X POST http://localhost:8081/api/demo/orders/checkout -H 'Content-Type: application/json' \
  -d '{"amount": 12.34}'
```

Then check Bank DB `demo_payment_record` and RMS `rms_message_tx` status → `SENT`.

Design write-up: `docs/design-order-bank-two-phase.md`.
