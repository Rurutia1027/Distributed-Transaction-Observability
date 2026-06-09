package com.eda.tracing.web;

import com.eda.tracing.config.SamplingProperties;
import com.eda.tracing.config.TracingSamplerConfig;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes active sampling strategy and whether the current request trace was sampled.
 *
 * <p>Use with Jaeger: compare responses where {@code sampled=true/false} after setting
 * {@code EDA_SAMPLING_RATIO=0.01} and sending many requests without force-trace headers.
 */
@RestController
@RequestMapping("/api/v1/tracing")
public class SamplingController {

    private final SamplingProperties properties;
    private final Sampler sampler;

    public SamplingController(SamplingProperties properties, Sampler sampler) {
        this.properties = properties;
        this.sampler = sampler;
    }

    @GetMapping("/sampling-info")
    public Map<String, Object> samplingInfo() {
        Span span = Span.current();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("strategy", properties.getStrategy());
        body.put("ratio", properties.getRatio());
        body.put("sampler_description", sampler.getDescription());
        body.put("always_sample_operations", properties.getAlwaysSampleOperations());
        body.put("always_sample_if_baggage", properties.getAlwaysSampleIfBaggage());
        body.put(
                "current_trace_sampled",
                span.getSpanContext().isValid() && span.getSpanContext().isSampled());
        body.put("current_trace_id", span.getSpanContext().getTraceId());
        body.put("head_sampling", Map.of(
                "layer", "SDK (application)",
                "note", "Tail sampling runs in otel-collector after export"));
        body.put("builtin_strategies", TracingSamplerConfig.buildSampler(properties).getDescription());
        return body;
    }
}
