CREATE TABLE IF NOT EXISTS transaction_message
(
    id                 VARCHAR(64)  NOT NULL,
    created_date       DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_date       DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    message_id         VARCHAR(128) NOT NULL,
    message_body       LONGTEXT     NULL,
    message_data_type  VARCHAR(64)  NULL,
    consumer_queue     VARCHAR(128) NULL,
    message_send_times INT          NULL     DEFAULT 0,
    already_dead       VARCHAR(16)  NULL     DEFAULT 'N',
    field1             VARCHAR(255) NULL,
    field2             VARCHAR(255) NULL,
    field3             VARCHAR(255) NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_tx_message_message_id (message_id),
    KEY                idx_tx_message_queue(consumer_queue),
    KEY                idx_tx_message_dead(already_dead),
    KEY                idx_tx_message_created(created_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;