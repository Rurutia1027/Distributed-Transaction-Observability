# Two-Phase MySQL init (Docker)

SQL in this directory is executed **once** when the MySQL data directory is **empty** (first `docker compose up` with a fresh volume).

- **`01-schema.sql`** — creates `rms_message_tx` and `rms_message_result` in database `reliable_message`.

To re-run init after schema changes:

```bash
docker compose -f docker-compose.twophase.yml down -v
docker compose -f docker-compose.twophase.yml up -d --build
```

**Warning:** `-v` removes the MySQL volume and all data.

Outbox tables (`rms_outbox_event`) are **not** included here; add a separate init folder / compose profile when you split outbox.
