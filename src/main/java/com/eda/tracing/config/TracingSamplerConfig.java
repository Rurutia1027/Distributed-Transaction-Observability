package com.eda.tracing.config;

import io.opentelemetry.sdk.trace.samplers.Sampler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the OpenTelemetry {@link Sampler} bean consumed by
 * {@code opentelemetry-spring-boot-starter}.
 */
@Configuration
@EnableConfigurationProperties(SamplingProperties.class)
public class TracingSamplerConfig {

    @Bean
    public Sampler otelSampler(SamplingProperties properties) {
        return buildSampler(properties);
    }

    /**
     * Factory used by tests and the {@link com.eda.tracing.web.SamplingController}.
     */
    public static Sampler buildSampler(SamplingProperties properties) {
        String strategy = properties.getStrategy().toLowerCase().replace('-', '_');

        Sampler root = switch (strategy) {
            case "always_on", "alwayson" -> Sampler.alwaysOn();
            case "always_off", "alwaysoff" -> Sampler.alwaysOff();
            case "traceidratio", "trace_id_ratio" ->
                    Sampler.traceIdRatioBased(clamp(properties.getRatio()));
            case "parentbased_traceidratio", "parent_based_trace_id_ratio" ->
                    Sampler.parentBased(
                            Sampler.traceIdRatioBased(clamp(properties.getRatio())));
            case "rule_based", "rulebased" -> new EdaRuleBasedSampler(properties);
            default -> throw new IllegalArgumentException(
                    "Unknown eda.tracing.sampling.strategy: " + properties.getStrategy());
        };

        // ParentBased: once a trace is sampled, all children are sampled.
        // For rule_based / traceidratio roots, wrap so distributed traces stay coherent.
        if (strategy.equals("rule_based") || strategy.equals("rulebased")) {
            return Sampler.parentBased(root);
        }
        return root;
    }

    private static double clamp(double ratio) {
        return Math.max(0.0, Math.min(1.0, ratio));
    }
}
