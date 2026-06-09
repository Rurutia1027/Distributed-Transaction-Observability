package com.eda.tracing.service;

import com.eda.tracing.domain.Order;
import com.eda.tracing.domain.OrderRevision;
import com.eda.tracing.exception.OrderNotFoundException;
import com.eda.tracing.exception.TransactionConflictException;
import com.eda.tracing.repository.OrderRepository;
import com.eda.tracing.repository.OrderRevisionRepository;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import java.time.Instant;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderRevisionRepository revisionRepository;
    private final Tracer tracer;

    @Value("${demo.force-error:false}")
    private boolean demoForceError;

    public OrderService(
            OrderRepository orderRepository,
            OrderRevisionRepository revisionRepository,
            Tracer tracer) {
        this.orderRepository = orderRepository;
        this.revisionRepository = revisionRepository;
        this.tracer = tracer;
    }

    @Transactional
    public Map<String, Object> createOrder(String name, String projectId, String items) {
        Span span = tracer.spanBuilder("order.create_transaction")
                .setSpanKind(SpanKind.INTERNAL)
                .setAttribute("eda.operation", "create")
                .setAttribute("eda.project_id", projectId)
                .setAttribute("eda.order.name", name)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            TraceSupport.copyBaggageToSpan(span);
            span.addEvent("transaction.begin");

            if (demoForceError) {
                throw new TransactionConflictException(
                        "Simulated constraint violation (demo.force-error=true)");
            }

            Order order = new Order();
            order.setName(name);
            order.setProjectId(projectId);
            order.setItems(items);
            order.setStatus("draft");
            order.setVersion(1);
            orderRepository.save(order);

            OrderRevision revision = new OrderRevision();
            revision.setOrderId(order.getId());
            revision.setRevision(1);
            revision.setChangeSummary("Initial creation");
            revisionRepository.save(revision);

            span.setAttribute("eda.order.id", order.getId());
            span.addEvent("order.persisted");
            span.setStatus(StatusCode.OK);

            return Map.of(
                    "id", order.getId(),
                    "name", order.getName(),
                    "project_id", order.getProjectId(),
                    "version", order.getVersion(),
                    "status", order.getStatus());
        } catch (Exception ex) {
            TraceSupport.recordError(span, ex);
            throw ex;
        } finally {
            span.end();
        }
    }

    @Transactional
    public Map<String, Object> updateOrder(
            Long orderId, String items, String changeSummary, Integer expectedVersion) {
        Span span = tracer.spanBuilder("order.update_transaction")
                .setSpanKind(SpanKind.INTERNAL)
                .setAttribute("eda.operation", "update")
                .setAttribute("eda.order.id", orderId)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            TraceSupport.copyBaggageToSpan(span);

            Order order = orderRepository
                    .findByIdForUpdate(orderId)
                    .orElseThrow(() -> new OrderNotFoundException("Order " + orderId + " not found"));

            if (expectedVersion != null && order.getVersion() != expectedVersion) {
                throw new TransactionConflictException(
                        "Version mismatch: expected " + expectedVersion + ", got " + order.getVersion());
            }

            span.setAttribute("eda.order.version.before", order.getVersion());
            order.setItems(items);
            order.setVersion(order.getVersion() + 1);
            order.setStatus("modified");
            order.setUpdatedAt(Instant.now());

            OrderRevision revision = new OrderRevision();
            revision.setOrderId(order.getId());
            revision.setRevision(order.getVersion());
            revision.setChangeSummary(changeSummary);
            revisionRepository.save(revision);

            span.setAttribute("eda.order.version.after", order.getVersion());
            span.addEvent("order.updated");
            span.setStatus(StatusCode.OK);

            return Map.of(
                    "id", order.getId(),
                    "name", order.getName(),
                    "version", order.getVersion(),
                    "status", order.getStatus());
        } catch (Exception ex) {
            TraceSupport.recordError(span, ex);
            throw ex;
        } finally {
            span.end();
        }
    }

    @Transactional
    public Map<String, Object> commitOrder(Long orderId) {
        Span span = tracer.spanBuilder("order.commit_transaction")
                .setSpanKind(SpanKind.INTERNAL)
                .setAttribute("eda.operation", "commit")
                .setAttribute("eda.order.id", orderId)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            TraceSupport.copyBaggageToSpan(span);

            Order order = orderRepository
                    .findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException("Order " + orderId + " not found"));

            span.addEvent("savepoint.begin");
            if (order.getItems() == null || order.getItems().isBlank()) {
                throw new TransactionConflictException("Cannot commit order without items");
            }
            span.addEvent("savepoint.release");

            order.setStatus("committed");
            order.setUpdatedAt(Instant.now());

            OrderRevision revision = new OrderRevision();
            revision.setOrderId(order.getId());
            revision.setRevision(order.getVersion());
            revision.setChangeSummary("Order committed");
            revisionRepository.save(revision);

            span.setStatus(StatusCode.OK);
            return Map.of("id", order.getId(), "status", order.getStatus(), "version", order.getVersion());
        } catch (Exception ex) {
            TraceSupport.recordError(span, ex);
            throw ex;
        } finally {
            span.end();
        }
    }
}
