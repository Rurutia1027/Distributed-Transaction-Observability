package com.eda.tracing.config;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.samplers.SamplingDecision;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdaRuleBasedSampleTest {
    @Test
    void samplesCommitOperationRegardlessOfRatio() {
        SamplingProperties props = new SamplingProperties();
        props.setStrategy("rule_based");
        props.setRatio(0.0);
        props.setAlwaysSampleOperations(List.of("commit"));

        EdaRuleBasedSampler sampler = new EdaRuleBasedSampler(props);
        SamplingResult result = sampler.shouldSample(
                Context.root(),
                "00000000000000000000000000000000",
                "order.commit_transaction",
                SpanKind.INTERNAL,
                Attributes.builder().put("eda.operation", "commit").build(),
                List.of());

        assertEquals(SamplingDecision.RECORD_AND_SAMPLE, result.getDecision());
    }

    @Test
    void dropsWhenRatioZeroAndNoRuleMatches() {
        SamplingProperties props = new SamplingProperties();
        props.setRatio(0.0);
        props.setAlwaysSampleOperations(List.of("commit"));

        EdaRuleBasedSampler sampler = new EdaRuleBasedSampler(props);
        SamplingResult result = sampler.shouldSample(
                Context.root(),
                "00000000000000000000000000000000",
                "order.create_transaction",
                SpanKind.INTERNAL,
                Attributes.builder().put("eda.operation", "create").build(),
                List.of());

        assertEquals(SamplingDecision.DROP, result.getDecision());
    }

    @Test
    void samplesWhenBaggageMatches() {
        SamplingProperties props = new SamplingProperties();
        props.setRatio(0.0);
        props.setAlwaysSampleIfBaggage(Map.of("force.trace", "true"));

        Context ctx = io.opentelemetry.api.baggage.Baggage.builder()
                .put("force.trace", "true")
                .build()
                .storeInContext(Context.root());

        EdaRuleBasedSampler sampler = new EdaRuleBasedSampler(props);
        SamplingResult result = sampler.shouldSample(
                ctx,
                "00000000000000000000000000000000",
                "GET /health",
                SpanKind.SERVER,
                Attributes.empty(),
                List.of());

        assertEquals(SamplingDecision.RECORD_AND_SAMPLE, result.getDecision());
    }
}
