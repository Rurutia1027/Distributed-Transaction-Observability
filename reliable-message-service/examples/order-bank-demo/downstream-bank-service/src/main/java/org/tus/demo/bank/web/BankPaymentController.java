package org.tus.demo.bank.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.tus.demo.bank.service.BankIngestService;

/**
 * Bank-owned ingress (demo stand-in for MQ consumer). RMS {@code HttpDownstreamPublisher} POSTs here.
 */
@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
public class BankPaymentController {

    private final BankIngestService ingestService;

    @PostMapping("/payment-commands")
    public void paymentCommand(
            @RequestHeader(value = "X-Message-Id", required = false) String messageId,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId,
            @RequestBody String body) {
        if (messageId == null || messageId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "X-Message-Id required");
        }
        try {
            ingestService.ingest(messageId, traceId, body);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
