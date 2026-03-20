-- Bank-owned schema (separate from RMS / reliable_message)
USE `bank`;

CREATE TABLE IF NOT EXISTS demo_payment_record
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    message_id  VARCHAR(128)   NOT NULL,
    order_no    VARCHAR(64)    NOT NULL,
    amount      DECIMAL(19, 2) NOT NULL,
    status      VARCHAR(32)    NOT NULL,
    trace_id    VARCHAR(128)   NULL,
    raw_payload TEXT           NULL,
    created_at  DATETIME(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    UNIQUE KEY uk_demo_payment_message (message_id)
);
