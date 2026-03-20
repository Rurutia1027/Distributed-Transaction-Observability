package org.tus.tx.demo.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tus.tx.demo.order.config.RmsProperties;
import org.tus.tx.demo.order.domain.DemoOrder;
import org.tus.tx.demo.order.domain.DemoOrderRepository;
import org.tus.tx.rms.twophase.v1.CommitMessageRequest;
import org.tus.tx.rms.twophase.v1.PrepareMessageRequest;
import org.tus.tx.rms.twophase.v1.ReliableMessageServiceGrpc;

import java.util.UUID;

@Service
public class OrderCheckoutService {

    private static final Logger log = LoggerFactory.getLogger(OrderCheckoutService.class);

    private final DemoOrderRepository orderRepository;
    private final ReliableMessageServiceGrpc.ReliableMessageServiceBlockingStub rms;
    private final RmsProperties rmsProperties;

    public OrderCheckoutService(DemoOrderRepository orderRepository,
                                ReliableMessageServiceGrpc.ReliableMessageServiceBlockingStub rms,
                                RmsProperties rmsProperties) {
        this.orderRepository = orderRepository;
        this.rms = rms;
        this.rmsProperties = rmsProperties;
    }

    @Transactional
    public DemoOrder checkout(String orderNo, int amountCents, String traceId) {
        if (orderRepository.findByOrderNo(orderNo).isPresent()) {
            throw new IllegalArgumentException("order_no already exists");
        }
        String messageId = UUID.randomUUID().toString().replace("-", "");
        String bizKey = "order:" + orderNo;
        String tid = traceId != null && !traceId.isBlank() ? traceId : messageId;

        String payload = "{\"orderNo\":\"" + orderNo + "\",\"amountCents\":" + amountCents + "}";

        rms.prepareMessage(PrepareMessageRequest.newBuilder()
                .setMessageId(messageId)
                .setBizKey(bizKey)
                .setEventType("ORDER_CHECKOUT")
                .setDestination(rmsProperties.getPayment().getDestination())
                .setPayload(payload)
                .setTraceId(tid)
                .setTtlSeconds(rmsProperties.getPayment().getTtlSeconds())
                .build());

        DemoOrder order = new DemoOrder();
        order.setOrderNo(orderNo);
        order.setAmountCents(amountCents);
        order.setStatus("CREATED");
        order.setRmsMessageId(messageId);
        orderRepository.save(order);

        rms.commitMessage(CommitMessageRequest.newBuilder()
                .setMessageId(messageId)
                .setBizKey(bizKey)
                .setTraceId(tid)
                .build());

        log.info("checkout committed orderNo={} messageId={}", orderNo, messageId);
        return orderRepository.findByOrderNo(orderNo).orElseThrow();
    }
}
