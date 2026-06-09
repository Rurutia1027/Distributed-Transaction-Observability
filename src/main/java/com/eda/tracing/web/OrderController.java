package com.eda.tracing.web;

import com.eda.tracing.service.OrderService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final Tracer tracer;

    public OrderController(OrderService orderService, Tracer tracer) {
        this.orderService = orderService;
        this.tracer = tracer;
    }

    @PostMapping
    public Map<String, Object> create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request.name(), request.projectId(), request.items());
    }

    @PutMapping("/{orderId}")
    public Map<String, Object> update(
            @PathVariable Long orderId, @Valid @RequestBody UpdateOrderRequest request) {
        return orderService.updateOrder(
                orderId, request.items(), request.changeSummary(), request.expectedVersion());
    }

    @PostMapping("/{orderId}/commit")
    public Map<String, Object> commit(@PathVariable Long orderId) {
        return orderService.commitOrder(orderId);
    }

    @PostMapping("/{orderId}/simulate-error")
    public Map<String, String> simulateError(@PathVariable Long orderId) {
        Span span = tracer.spanBuilder("order.simulate_error")
                .setAttribute("eda.operation", "simulate-error")
                .setAttribute("eda.order.id", orderId)
                .setAttribute("demo", true)
                .startSpan();
        try {
            RuntimeException error = new RuntimeException("Simulated failure for order " + orderId);
            span.recordException(error);
            span.setStatus(StatusCode.ERROR, error.getMessage());
            throw error;
        } finally {
            span.end();
        }
    }

    public record CreateOrderRequest(
            @NotBlank String name, @NotBlank String projectId, String items) {}

    public record UpdateOrderRequest(
            @NotBlank String items, String changeSummary, Integer expectedVersion) {
        public UpdateOrderRequest {
            if (changeSummary == null || changeSummary.isBlank()) {
                changeSummary = "Items update";
            }
        }
    }
}
