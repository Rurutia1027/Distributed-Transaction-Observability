package org.tus.tx.rms.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tus.tx.rms.service.outbox.OutboxRuntimeService;
import org.tus.tx.rms.service.twophase.TwoPhaseReliableMessageService;

@Component
@RequiredArgsConstructor
public class RelayScheduler {

    private final TwoPhaseReliableMessageService twoPhaseReliableMessageService;
    private final ObjectProvider<OutboxRuntimeService> outboxRuntimeService;

    @Value("${rms.relay.batch-size:50}")
    private int batchSize;

    @Value("${rms.relay.retry-interval-seconds:30}")
    private int retryIntervalSeconds;

    @Scheduled(fixedDelayString = "${rms.relay.fixed-delay-ms:5000}")
    public void relayTwoPhase() {
        twoPhaseReliableMessageService.relayTick(batchSize, retryIntervalSeconds);
    }

    @Scheduled(fixedDelayString = "${rms.relay.fixed-delay-ms:5000}")
    public void relayOutbox() {
        outboxRuntimeService.ifAvailable(s -> s.relayTick(batchSize, retryIntervalSeconds));
    }
}
