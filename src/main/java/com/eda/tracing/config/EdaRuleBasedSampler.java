package com.eda.tracing.config;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingDecision;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Custom head sampler for EDA monolith.
 *
 * <p>Evaluation order for root spans:
 * <ol>
 *   <li>Baggage rules ({@code force.trace=true}, {@code project.tier=premium})
 *   <li>Force-trace HTTP header (via {@code eda.force_trace} span attribute)
 *   <li>Operation rules ({@code commit}, {@code simulate-error})
 *   <li>Ratio fallback
 * </ol>
 */
public final class EdaRuleBasedSampler implements Sampler {

    private static final AttributeKey<String> EDA_OPERATION =
            AttributeKey.stringKey("eda.operation");
    private static final AttributeKey<String> HTTP_TARGET =
            AttributeKey.stringKey("http.target");
    private static final AttributeKey<Boolean> FORCE_TRACE_HEADER =
            AttributeKey.booleanKey("eda.force_trace");

    private final Sampler ratioFallback;
    private final SamplingProperties properties;
    private final String description;

    public EdaRuleBasedSampler(SamplingProperties properties) {
        this.properties = properties;
        this.ratioFallback = Sampler.traceIdRatioBased(clampRatio(properties.getRatio()));
        this.description = "EdaRuleBasedSampler{ratio=" + properties.getRatio() + "}";
    }

    @Override
    public SamplingResult shouldSample(
            Context parentContext,
            String traceId,
            String name,
            SpanKind spanKind,
            Attributes attributes,
            List<LinkData> parentLinks) {

        for (Map.Entry<String, String> rule :
                properties.getAlwaysSampleIfBaggage().entrySet()) {
            String baggageValue =
                    io.opentelemetry.api.baggage.Baggage.fromContext(parentContext)
                            .getEntryValue(rule.getKey());
            if (rule.getValue().equalsIgnoreCase(baggageValue)) {
                return sampled(
                        Attributes.builder()
                                .put("sampler.rule", "baggage")
                                .put("sampler.matched_key", rule.getKey())
                                .put("sampler.matched_value", rule.getValue())
                                .build());
            }
        }

        Boolean forceTrace = attributes.get(FORCE_TRACE_HEADER);
        if (Boolean.TRUE.equals(forceTrace)) {
            return sampled(Attributes.builder().put("sampler.rule", "force_trace_header").build());
        }

        String operation = attributes.get(EDA_OPERATION);
        String httpTarget = attributes.get(HTTP_TARGET);
        for (String op : properties.getAlwaysSampleOperations()) {
            if (matchesOperation(op, name, operation, httpTarget)) {
                return sampled(
                        Attributes.builder()
                                .put("sampler.rule", "operation")
                                .put("sampler.matched_operation", op)
                                .build());
            }
        }

        SamplingResult fallback = ratioFallback.shouldSample(
                parentContext, traceId, name, spanKind, attributes, parentLinks);
        if (fallback.getDecision() == SamplingDecision.RECORD_AND_SAMPLE) {
            return sampled(
                    Attributes.builder()
                            .put("sampler.rule", "ratio")
                            .put("sampler.ratio", properties.getRatio())
                            .build());
        }
        return dropped(
                Attributes.builder()
                        .put("sampler.rule", "ratio_dropped")
                        .put("sampler.ratio", properties.getRatio())
                        .build());
    }

    private static SamplingResult sampled(Attributes attributes) {
        return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE, attributes);
    }

    private static SamplingResult dropped(Attributes attributes) {
        return SamplingResult.create(SamplingDecision.DROP, attributes);
    }

    private static boolean matchesOperation(
            String rule, String spanName, String edaOperation, String httpTarget) {
        String r = rule.toLowerCase(Locale.ROOT);
        if (edaOperation != null && edaOperation.toLowerCase(Locale.ROOT).contains(r)) {
            return true;
        }
        if (spanName != null && spanName.toLowerCase(Locale.ROOT).contains(r)) {
            return true;
        }
        return httpTarget != null && httpTarget.toLowerCase(Locale.ROOT).contains(r);
    }

    private static double clampRatio(double ratio) {
        if (ratio < 0.0) {
            return 0.0;
        }
        if (ratio > 1.0) {
            return 1.0;
        }
        return ratio;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
