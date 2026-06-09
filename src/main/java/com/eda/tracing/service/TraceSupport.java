package com.eda.tracing.service;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;

public final class TraceSupport {

    private TraceSupport() {}

    public static void copyBaggageToSpan(Span span) {
        String projectId = Baggage.current().getEntryValue("project.id");
        String userId = Baggage.current().getEntryValue("user.id");
        if (projectId != null && !projectId.isBlank()) {
            span.setAttribute("baggage.project_id", projectId);
        }
        if (userId != null && !userId.isBlank()) {
            span.setAttribute("baggage.user_id", userId);
        }
    }

    public static void recordError(Span span, Throwable error) {
        span.recordException(error);
        span.setStatus(StatusCode.ERROR, error.getMessage());
        span.addEvent(
                "transaction.failed",
                io.opentelemetry.api.common.Attributes.of(
                        io.opentelemetry.api.common.AttributeKey.stringKey("exception.type"),
                        error.getClass().getSimpleName(),
                        io.opentelemetry.api.common.AttributeKey.stringKey("exception.message"),
                        error.getMessage() != null ? error.getMessage() : ""));
    }
}
