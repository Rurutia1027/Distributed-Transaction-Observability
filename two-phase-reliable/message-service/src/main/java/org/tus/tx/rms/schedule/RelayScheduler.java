package org.tus.tx.rms.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tus.tx.rms.service.TwoPhaseReliableMessageService;

@Component
@RequiredArgsConstructor
public class RelayScheduler {
    private final TwoPhaseReliableMessageService twoPhaseReliableMessageService;

    @Value("${rms.relay.batch-size:50}")
    private int batchSize;

    @Value("${rms.relay.retry-interval-seconds:30}")
    private int retryIntervalSeconds;

    @Scheduled(fixedDelayString = "${rms.relay.fixed-delay-ms:5000}")
    public void relayTwoPhase() {
        twoPhaseReliableMessageService.relayTick(batchSize, retryIntervalSeconds);
    }
}
