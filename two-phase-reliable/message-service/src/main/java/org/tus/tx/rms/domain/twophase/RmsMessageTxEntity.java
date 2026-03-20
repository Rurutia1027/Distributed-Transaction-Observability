package org.tus.tx.rms.domain.twophase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "rms_message_tx",
        indexes = {@Index(name = "idx_rms_status_next_retry", columnList = "status,nextRetryAt"),
                @Index(name = "idx_rms_bix_event", columnList = "bizKey,eventType")})
public class RmsMessageTxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", nullable = false, unique = true, length = 128)
    private String messageId;

    @Column(name = "biz_key", nullable = false, length = 128)
    private String bizKey;

    @Column(name = "event_type", nullable = false, length = 128)
    private String eventType;

    @Column(name = "producer_system", nullable = false, length = 64)
    private String producerSystem = "unknown";

    @Column(name = "destination", nullable = false, length = 255)
    private String destination;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "trace_id", length = 128)
    private String traceId;

    @Column(name = "ttl_seconds")
    private Integer ttlSeconds;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "send_times", nullable = false)
    private int sendTimes = 0;

    @Column(name = "max_retry_times", nullable = false)
    private int maxRetryTimes = 16;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;

    @Column(name = "last_error", length = 2000)
    private String lastError;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    void touch() {
        this.updatedAt = Instant.now();
    }
}
