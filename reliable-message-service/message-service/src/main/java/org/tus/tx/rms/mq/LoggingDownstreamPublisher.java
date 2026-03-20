package org.tus.tx.rms.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "rms.downstream", name = "publisher", havingValue = "log", matchIfMissing = true)
public class LoggingDownstreamPublisher implements DownstreamPublisher {

    @Override
    public void publish(String destination, String payload, String messageId, String traceId) {
        log.info("Downstream publish (stub) destination={} messageId={} traceId={} payloadLength={}",
                destination, messageId, traceId, payload != null ? payload.length() : 0);
    }
}
