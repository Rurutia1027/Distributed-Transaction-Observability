package org.tus.tx.rms.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingDownstreamPublisher implements DownstreamPublisher {
    // TODO: add details for downstream message deliver
    @Override
    public void publish(String destination, String payload, String messageId, String traceId) {
        log.info("Downstream publish (stub) destination={} messageId={} traceId={} " +
                        "payloadLength={}",
                destination, messageId, traceId, payload != null ? payload.length() : 0);

    }
}
