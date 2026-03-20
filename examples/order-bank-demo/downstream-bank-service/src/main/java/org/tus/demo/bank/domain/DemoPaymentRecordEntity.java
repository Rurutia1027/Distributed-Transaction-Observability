package org.tus.demo.bank.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "demo_payment_record")
public class DemoPaymentRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "message_id", nullable = false, unique = true, length = 128)
    private String messageId;
    @Column(name = "order_no", nullable = false, length = 64)
    private String orderNo;
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(name = "status", nullable = false, length = 32)
    private String status;
    @Column(name = "trace_id", length = 128)
    private String traceId;
    @Column(name = "raw_payload", columnDefinition = "TEXT")
    private String rawPayload;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
