package com.eda.tracing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "eda.tracing.sampling")
public class SamplingProperties {
    /**
     * Head sampling strategy applied in-process (SDK).
     *
     * <ul>
     *     <li>{@code parent-based_trace-id-ratio} - respect parent; sample roots by ratio
     *     (production default)</li>
     *     <li>{@code traceidratio} - ratio for every span regardless of parent</li>
     *     <li>{@code always_on} / {@code always_off} -- debug or disable </li>
     *     <li>{@code rule_based} - EDA custom rules (baggage, operation name, then ratio
     *     fallback)</li>
     * </ul>
     */
    private String strategy = "rule_based";

    /**
     * Ratio for trace-id-ratio / parent-based_trace-id-ratio / rule_based fallback.
     */
    private double ratio = 0.2;

    /**
     * Operations (eda.operation attribute or span name fragment) always sampled at root.
     */
    private List<String> alwaysSampleOperations = List.of("commit", "simulate-error");

    /**
     * Baggage key/value pairs that force sampling when matched.
     */
    private Map<String, String> alwaysSampleIfBaggage = new HashMap<>();

    public String getStrategy() {
        return strategy;
    }

    public double getRatio() {
        return ratio;
    }

    public List<String> getAlwaysSampleOperations() {
        return alwaysSampleOperations;
    }

    public Map<String, String> getAlwaysSampleIfBaggage() {
        return alwaysSampleIfBaggage;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public void setAlwaysSampleOperations(List<String> alwaysSampleOperations) {
        this.alwaysSampleOperations = alwaysSampleOperations;
    }

    public void setAlwaysSampleIfBaggage(Map<String, String> alwaysSampleIfBaggage) {
        this.alwaysSampleIfBaggage = alwaysSampleIfBaggage;
    }
}
