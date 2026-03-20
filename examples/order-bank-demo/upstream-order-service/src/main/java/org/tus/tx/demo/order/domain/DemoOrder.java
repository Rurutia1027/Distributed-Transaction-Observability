package org.tus.tx.demo.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "demo_order")
public class DemoOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true, length = 64)
    private String orderNo;

    @Column(name = "amount_cents", nullable = false)
    private int amountCents;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "rms_message_id", length = 64)
    private String rmsMessageId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() {
        return id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(int amountCents) {
        this.amountCents = amountCents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRmsMessageId() {
        return rmsMessageId;
    }

    public void setRmsMessageId(String rmsMessageId) {
        this.rmsMessageId = rmsMessageId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
