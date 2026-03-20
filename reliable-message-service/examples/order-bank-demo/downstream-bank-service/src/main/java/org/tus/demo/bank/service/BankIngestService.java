package org.tus.demo.bank.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tus.demo.bank.domain.DemoPaymentRecordEntity;
import org.tus.demo.bank.domain.DemoPaymentRecordRepository;
import org.tus.tx.rms.twophase.v1.ReliableMessageServiceGrpc;
import org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest;

import java.math.BigDecimal;

/**
 * Simulates Bank-owned queue consumer: idempotent by {@code messageId}.
 */
@Service
@RequiredArgsConstructor
public class BankIngestService {

    public static final String STATUS_SUCCESS = "SUCCESS";

    private final DemoPaymentRecordRepository repository;
    private final ObjectProvider<ReliableMessageServiceGrpc.ReliableMessageServiceBlockingStub> rmsReportStub;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void ingest(String messageId, String traceId, String rawJson) throws Exception {
        if (repository.findByMessageId(messageId).isPresent()) {
            return;
        }
        JsonNode root = objectMapper.readTree(rawJson);
        String orderNo = root.path("orderNo").asText(null);
        BigDecimal amount = new BigDecimal(root.path("amount").asText());

        DemoPaymentRecordEntity row = new DemoPaymentRecordEntity();
        row.setMessageId(messageId);
        row.setOrderNo(orderNo);
        row.setAmount(amount);
        row.setStatus(STATUS_SUCCESS);
        row.setTraceId(traceId);
        row.setRawPayload(rawJson);
        repository.save(row);

        ReliableMessageServiceGrpc.ReliableMessageServiceBlockingStub stub = rmsReportStub.getIfAvailable();
        if (stub != null && orderNo != null) {
            stub.reportConsumeResult(ReportConsumeResultRequest.newBuilder()
                    .setMessageId(messageId)
                    .setBizKey(orderNo)
                    .setResultCode("SUCCESS")
                    .setResultPayload("{\"deducted\":true}")
                    .build());
        }
    }
}
