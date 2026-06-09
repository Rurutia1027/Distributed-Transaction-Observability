package com.eda.tracing.web;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.baggage.BaggageBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Propagates W3C Baggage and maps convenience headers into OTel context.
 *
 * <p>{@link TraceAttributesFilter} copies selected values onto the active span.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BaggageFilter extends OncePerRequestFilter {

    public static final String ATTR_PROJECT_ID = "eda.project.id";
    public static final String ATTR_USER_ID = "eda.user.id";
    public static final String ATTR_FORCE_TRACE = "eda.force_trace";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String projectId = request.getHeader("X-Project-Id");
        String userId = request.getHeader("X-User-Id");
        String forceTrace = request.getHeader("X-Force-Trace");

        if ("true".equalsIgnoreCase(forceTrace)) {
            request.setAttribute(ATTR_FORCE_TRACE, true);
        }
        if (projectId != null) {
            request.setAttribute(ATTR_PROJECT_ID, projectId);
        }
        if (userId != null) {
            request.setAttribute(ATTR_USER_ID, userId);
        }

        BaggageBuilder builder = Baggage.fromContext(Context.current()).toBuilder();
        if (projectId != null) {
            builder.put("project.id", projectId);
        }
        if (userId != null) {
            builder.put("user.id", userId);
        }
        if ("true".equalsIgnoreCase(forceTrace)) {
            builder.put("force.trace", "true");
        }

        Context updated = builder.build().storeInContext(Context.current());
        try (Scope scope = updated.makeCurrent()) {
            filterChain.doFilter(request, response);
        }
    }
}
