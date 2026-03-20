package org.tus.tx.rms.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.tus.tx.rms.domain.outbox.RmsOutboxEventEntity;
import org.tus.tx.rms.outbox.v1.*;
import org.tus.tx.rms.service.outbox.OutboxRuntimeService;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rms.outbox.enabled", havingValue = "true", matchIfMissing = true)
public class OutboxRuntimeGrpcService extends OutboxRuntimeServiceGrpc.OutboxRuntimeServiceImplBase {

    private final OutboxRuntimeService outbox;

    @Override
    public void appendOutboxEvent(AppendOutboxEventRequest request, StreamObserver<AppendOutboxEventResponse> responseObserver) {
        try {
            outbox.append(
                    request.getEventId(),
                    request.getAggregateType(),
                    request.getAggregateId(),
                    request.getBizKey(),
                    request.getEventType(),
                    request.getDestination(),
                    request.getPayload(),
                    request.getHeadersJson(),
                    request.getTraceId());
            responseObserver.onNext(AppendOutboxEventResponse.newBuilder()
                    .setAccepted(true)
                    .setStatus(OutboxRuntimeService.PENDING)
                    .build());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            GrpcErrors.toObserver(responseObserver, e);
        }
    }

    @Override
    public void queryOutboxEvent(QueryOutboxEventRequest request, StreamObserver<OutboxEventView> responseObserver) {
        try {
            RmsOutboxEventEntity row = outbox.query(request.getEventId());
            OutboxEventView.Builder b = OutboxEventView.newBuilder()
                    .setEventId(row.getEventId())
                    .setBizKey(row.getBizKey())
                    .setEventType(row.getEventType())
                    .setStatus(row.getStatus())
                    .setSendTimes(row.getSendTimes())
                    .setLastError(row.getLastError() != null ? row.getLastError() : "");
            if (row.getNextRetryAt() != null) {
                b.setNextRetryAtEpochMs(row.getNextRetryAt().toEpochMilli());
            }
            responseObserver.onNext(b.build());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            GrpcErrors.toObserver(responseObserver, e);
        }
    }

    @Override
    public void markOutboxReady(MarkOutboxReadyRequest request, StreamObserver<SimpleAck> responseObserver) {
        try {
            outbox.markReady(request.getEventId());
            responseObserver.onNext(SimpleAck.newBuilder().setOk(true).setMessage("READY").build());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            GrpcErrors.toObserver(responseObserver, e);
        }
    }
}
