package org.tus.tx.rms.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Demo / dev publisher: POST payload to a full URL (simulates handing off to Bank-owned ingress).
 * On non-2xx or network error, throws so RMS relay can retry / DEAD.
 * <p>
 * This models <strong>L1 broker-style ACK</strong>: success means the HTTP endpoint accepted the message.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "rms.downstream", name = "publisher", havingValue = "http")
public class HttpDownstreamPublisher implements DownstreamPublisher {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @Value("${rms.downstream.http.timeout-seconds:15}")
    private int timeoutSeconds;

    @Override
    public void publish(String destination, String payload, String messageId, String traceId) {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("destination URL is required when using http publisher");
        }
        URI uri = URI.create(destination.trim());
        HttpRequest.Builder b = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(Math.max(1, timeoutSeconds)))
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("X-Message-Id", messageId != null ? messageId : "")
                .POST(HttpRequest.BodyPublishers.ofString(
                        payload != null ? payload : "",
                        StandardCharsets.UTF_8));
        if (traceId != null && !traceId.isBlank()) {
            b.header("X-Trace-Id", traceId);
        }
        HttpRequest req = b.build();
        try {
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int code = resp.statusCode();
            if (code < 200 || code >= 300) {
                String body = resp.body();
                log.warn("HTTP downstream non-success status={} messageId={} bodySnippet={}",
                        code, messageId, truncate(body, 500));
                throw new IllegalStateException("downstream HTTP status " + code);
            }
            log.info("HTTP downstream ok status={} messageId={} traceId={}", code, messageId, traceId);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("downstream HTTP failed: " + e.getMessage(), e);
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }
}
