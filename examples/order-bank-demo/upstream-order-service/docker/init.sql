USE `order`;

CREATE TABLE IF NOT EXISTS demo_order (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          order_no VARCHAR(64) NOT NULL,
                                          status VARCHAR(32) NOT NULL,
                                          amount DECIMAL(19, 2) NOT NULL,
                                          rms_message_id VARCHAR(128) NULL,
                                          created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                                          updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
                                          UNIQUE KEY uk_demo_order_no (order_no)
);
