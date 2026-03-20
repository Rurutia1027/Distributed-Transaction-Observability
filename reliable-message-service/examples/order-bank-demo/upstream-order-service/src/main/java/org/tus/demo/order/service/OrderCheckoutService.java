package org.tus.demo.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tus.demo.order.domain.DemoOrderEntity;
import org.tus.demo.order.domain.DemoOrderRepository;
import org.tus.tx.rms.twophase.v1.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Two-phase flow: Prepare → local TX → Commit (or Rollback on failure).
 */
@Service
@RequiredArgsConstructor
public class OrderCheckoutService {

    public static final String STATUS_CREATED = "CREATED";
    public static final String STATUS_PAYMENT_REQUESTED = "PAYMENT_REQUESTED";
    public static final String EVENT_PAYMENT_DEDUCT_REQUESTED = "PAYMENT_DEDUCT_REQUESTED";

    private final ReliableMessageServiceGrpc.ReliableMessageServiceBlockingStub rms;
    private final DemoOrderRepository orderRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${rms.payment.destination}")
    private String paymentDestination;

    @Transactional
    public String checkout(BigDecimal amount) throws Exception {
        String orderNo = "O-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        String messageId = UUID.randomUUID().toString();
        String traceId = UUID.randomUUID().toString();

        String payload = buildPayloadJson(orderNo, amount);

        PrepareMessageRequest prepare = PrepareMessageRequest.newBuilder()
                .setMessageId(messageId)
                .setBizKey(orderNo)
                .setEventType(EVENT_PAYMENT_DEDUCT_REQUESTED)
                .setDestination(paymentDestination)
                .setPayload(payload)
                .setTraceId(traceId)
                .setTtlSeconds(3600)
                .build();
        rms.prepareMessage(prepare);

        try {
            DemoOrderEntity row = new DemoOrderEntity();
            row.setOrderNo(orderNo);
            row.setAmount(amount);
            row.setStatus(STATUS_PAYMENT_REQUESTED);
            row.setRmsMessageId(messageId);
            orderRepository.save(row);
        } catch (Exception e) {
            rms.rollbackMessage(RollbackMessageRequest.newBuilder()
                    .setMessageId(messageId)
                    .setReason("order_db_failed: " + e.getMessage())
                    .build());
            throw e;
        }

        rms.commitMessage(CommitMessageRequest.newBuilder()
                .setMessageId(messageId)
                .setBizKey(orderNo)
                .setTraceId(traceId)
                .build());

        return orderNo;
    }

    private String buildPayloadJson(String orderNo, BigDecimal amount) throws Exception {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("orderNo", orderNo);
        m.put("amount", amount);
        m.put("currency", "CNY");
        return objectMapper.writeValueAsString(m);
    }
}
