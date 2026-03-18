package org.tus.tx.service.message.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.tus.common.domain.persistence.PersistedObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(
        name = "transaction_message",
        indexes = {
                @Index(name = "idx_tm_message_id", columnList = "message_id", unique = true),
                @Index(name = "idx_tm_status_next_retry", columnList = "status,next_retry_at")
        }
)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransactionMessage extends PersistedObject {
    @Column(name = "message_id", length = 128, nullable = false, updatable = false)
    private String messageId;

    @Column(name = "message_body", columnDefinition = "text", nullable = false)
    private String messageBody;

    @Column(name = "message_data_type", length = 64)
    private String messageDataType;

    @Column(name = "consumer_queue", length = 255, nullable = false)
    private String consumerQueue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 32, nullable = false)
    private MessageStatus status = MessageStatus.WAITING_CONFIRM;

    @Column(name = "send_times", nullable = false)
    private int sendTimes = 0;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;

    @Column(name = "last_error", length = 2000)
    private String lastError;

    @Column(name = "field1", length = 255)
    private String field1;
    @Column(name = "field2", length = 255)
    private String field2;
    @Column(name = "field3", length = 255)
    private String field3;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public TransactionMessage() {
        super();
    }

    public TransactionMessage(String messageId, String messageBody,
                              String consumerQueue) {
        super();
        this.messageId = messageId;
        this.messageBody = messageBody;
        this.consumerQueue = consumerQueue;
    }
}

