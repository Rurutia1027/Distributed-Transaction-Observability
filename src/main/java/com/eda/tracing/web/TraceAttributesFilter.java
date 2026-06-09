package com.eda.tracing.web;

import io.opentelemetry.api.trace.Span;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** Copies request attributes onto the current server span for sampler + Jaeger visibility. */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class TraceAttributesFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Span span = Span.current();
        if (span != null && span.getSpanContext().isValid()) {
            Object projectId = request.getAttribute(BaggageFilter.ATTR_PROJECT_ID);
            Object userId = request.getAttribute(BaggageFilter.ATTR_USER_ID);
            Object forceTrace = request.getAttribute(BaggageFilter.ATTR_FORCE_TRACE);

            if (projectId instanceof String s && !s.isBlank()) {
                span.setAttribute("baggage.project_id", s);
            }
            if (userId instanceof String s && !s.isBlank()) {
                span.setAttribute("baggage.user_id", s);
            }
            if (Boolean.TRUE.equals(forceTrace)) {
                span.setAttribute("eda.force_trace", true);
            }
        }

        filterChain.doFilter(request, response);
    }
}
