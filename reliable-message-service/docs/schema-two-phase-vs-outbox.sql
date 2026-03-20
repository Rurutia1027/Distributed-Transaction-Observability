-- ==========================================================
-- Schema Draft: Two-Phase Reliable Messaging vs Outbox Pattern
-- 双方案 SQL 初稿：Two-Phase 可靠消息 vs Outbox
-- ==========================================================

-- ==========================================================
-- Part A: Two-Phase Reliable Messaging System
-- A 部分：Two-Phase 可靠消息系统（中心化中间件）
--
-- Runnable subset (aligned with JPA entities + Docker init):
--   db/init/twophase/01-schema.sql
-- ==========================================================

-- EN: Main transaction message table managed by middleware
-- 中文：中间件维护的事务消息主表
CREATE TABLE IF NOT EXISTS rms_message_tx (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    message_id VARCHAR(128) NOT NULL,
    biz_key VARCHAR(128) NOT NULL,
    event_type VARCHAR(128) NOT NULL,
    producer_system VARCHAR(64) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    trace_id VARCHAR(128) NULL,

    -- PREPARED / COMMITTED / ROLLED_BACK / SENDING / SENT / DEAD
    status VARCHAR(32) NOT NULL,
    send_times INT NOT NULL DEFAULT 0,
    max_retry_times INT NOT NULL DEFAULT 16,
    next_retry_at DATETIME NULL,
    last_error VARCHAR(2000) NULL,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_rms_message_id UNIQUE (message_id)
);

CREATE INDEX idx_rms_status_next_retry ON rms_message_tx(status, next_retry_at);
CREATE INDEX idx_rms_biz_event ON rms_message_tx(biz_key, event_type);
CREATE INDEX idx_rms_created_at ON rms_message_tx(created_at);


-- EN: Optional final consume/callback result table
-- 中文：可选的消费结果/回调结果表
CREATE TABLE IF NOT EXISTS rms_message_result (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    message_id VARCHAR(128) NOT NULL,
    biz_key VARCHAR(128) NOT NULL,
    source_system VARCHAR(64) NOT NULL,

    -- SUCCESS / FAIL / TIMEOUT
    result_code VARCHAR(32) NOT NULL,
    result_payload TEXT NULL,
    error_code VARCHAR(64) NULL,
    error_message VARCHAR(2000) NULL,

    -- PENDING / SUCCESS / FAILED
    callback_status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
    callback_retry_times INT NOT NULL DEFAULT 0,
    next_callback_at DATETIME NULL,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_rms_result_message_id ON rms_message_result(message_id);
CREATE INDEX idx_rms_result_biz_key ON rms_message_result(biz_key);
CREATE INDEX idx_rms_callback_status_next ON rms_message_result(callback_status, next_callback_at);


-- EN: Optional dispatch attempt log for diagnostics
-- 中文：可选的投递尝试日志表（用于排障）
CREATE TABLE IF NOT EXISTS rms_dispatch_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    message_id VARCHAR(128) NOT NULL,
    attempt_no INT NOT NULL,
    destination VARCHAR(255) NOT NULL,

    -- SUCCESS / FAIL
    dispatch_status VARCHAR(32) NOT NULL,
    error_message VARCHAR(2000) NULL,
    request_snapshot TEXT NULL,
    response_snapshot TEXT NULL,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_rms_dispatch_message_attempt ON rms_dispatch_log(message_id, attempt_no);
CREATE INDEX idx_rms_dispatch_created_at ON rms_dispatch_log(created_at);


-- ==========================================================
-- Part B: Outbox Pattern (business-local)
-- B 部分：Outbox 模式（业务本地）
-- ==========================================================

-- EN: Outbox event table in active business service database
-- 中文：主动业务服务本地数据库中的 outbox 事件表
CREATE TABLE IF NOT EXISTS order_outbox_event (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_id VARCHAR(128) NOT NULL,       -- == messageId
    aggregate_type VARCHAR(64) NOT NULL,  -- e.g. ORDER
    aggregate_id VARCHAR(128) NOT NULL,   -- e.g. orderNo
    biz_key VARCHAR(128) NOT NULL,
    event_type VARCHAR(128) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    headers_json TEXT NULL,
    trace_id VARCHAR(128) NULL,

    -- PENDING / SENDING / SENT / RETRYING / DEAD
    status VARCHAR(32) NOT NULL,
    send_times INT NOT NULL DEFAULT 0,
    max_retry_times INT NOT NULL DEFAULT 16,
    next_retry_at DATETIME NULL,
    last_error VARCHAR(2000) NULL,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_outbox_event_id UNIQUE (event_id)
);

CREATE INDEX idx_outbox_status_next_retry ON order_outbox_event(status, next_retry_at);
CREATE INDEX idx_outbox_aggregate ON order_outbox_event(aggregate_type, aggregate_id);
CREATE INDEX idx_outbox_biz_event ON order_outbox_event(biz_key, event_type);
CREATE INDEX idx_outbox_created_at ON order_outbox_event(created_at);


-- EN: Optional inbox table for idempotent result/event consumption
-- 中文：可选 inbox 表，用于结果事件幂等消费
CREATE TABLE IF NOT EXISTS order_inbox_event (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_id VARCHAR(128) NOT NULL,
    source_system VARCHAR(64) NOT NULL,
    event_type VARCHAR(128) NOT NULL,
    payload TEXT NOT NULL,
    processed TINYINT(1) NOT NULL DEFAULT 0,
    processed_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_inbox_event_id UNIQUE (event_id)
);

CREATE INDEX idx_inbox_processed ON order_inbox_event(processed, created_at);


-- ==========================================================
-- Notes / 说明
-- ==========================================================
-- EN:
-- 1) Use UTC timestamps in all environments.
-- 2) Keep payload schema version in event_type or headers_json.
-- 3) Purge/archive SENT and DEAD records with retention policy.
-- 4) Consumers must be idempotent by event_id/message_id.
--
-- 中文：
-- 1) 全环境统一使用 UTC 时间。
-- 2) payload 版本信息放在 event_type 或 headers_json 中。
-- 3) 对 SENT/DEAD 建立保留与归档策略。
-- 4) 消费端必须基于 event_id/message_id 做幂等。

