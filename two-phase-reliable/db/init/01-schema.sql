-- =============================================================================
-- RMS Two-Phase schema only (no outbox tables).
-- Applied automatically on first MySQL startup when this folder is mounted to
-- /docker-entrypoint-initdb.d (see docker-compose.twophase.yml).
--
-- Aligns with JPA entities: RmsMessageTxEntity, RmsMessageResultEntity.
-- Hibernate ddl-auto should be "none" or "validate" when using this script.
-- =============================================================================

USE `reliable_message`;

SET NAMES utf8mb4;

-- -----------------------------------------------------------------------------
-- Two-phase message state (middleware-owned)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS rms_message_tx
(
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    message_id      VARCHAR(128)  NOT NULL,
    biz_key         VARCHAR(128)  NOT NULL,
    event_type      VARCHAR(128)  NOT NULL,
    producer_system VARCHAR(64)   NOT NULL,
    destination     VARCHAR(255)  NOT NULL,
    payload         TEXT          NOT NULL,
    trace_id        VARCHAR(128)  NULL,
    ttl_seconds     INT           NULL,
    status          VARCHAR(32)   NOT NULL,
    send_times      INT           NOT NULL DEFAULT 0,
    max_retry_times INT           NOT NULL DEFAULT 16,
    next_retry_at   DATETIME(6)   NULL,
    last_error      VARCHAR(2000) NULL,
    created_at      DATETIME(6)   NOT NULL DEFAULT (UTC_TIMESTAMP(6)),
    updated_at      DATETIME(6)   NOT NULL DEFAULT (UTC_TIMESTAMP(6)) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_rms_message_tx_message_id UNIQUE (message_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_rms_status_next_retry ON rms_message_tx (status, next_retry_at);
CREATE INDEX idx_rms_biz_event ON rms_message_tx (biz_key, event_type);

-- -----------------------------------------------------------------------------
-- Optional consume / callback audit (ReportConsumeResult)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS rms_message_result
(
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    message_id     VARCHAR(128)  NOT NULL,
    biz_key        VARCHAR(128)  NOT NULL,
    source_system  VARCHAR(64)   NOT NULL,
    result_code    VARCHAR(32)   NOT NULL,
    result_payload TEXT          NULL,
    error_code     VARCHAR(64)   NULL,
    error_message  VARCHAR(2000) NULL,
    created_at     DATETIME(6)   NOT NULL DEFAULT (UTC_TIMESTAMP(6)),
    updated_at     DATETIME(6)   NOT NULL DEFAULT (UTC_TIMESTAMP(6)) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_rms_result_message_id ON rms_message_result (message_id);
CREATE INDEX idx_rms_result_biz_key ON rms_message_result (biz_key);
