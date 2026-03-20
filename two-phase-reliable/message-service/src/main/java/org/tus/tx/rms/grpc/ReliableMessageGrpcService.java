package org.tus.tx.rms.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.tus.tx.rms.domain.enums.MessageStatus;
import org.tus.tx.rms.service.TwoPhaseReliableMessageService;
import org.tus.tx.rms.twophase.v1.CommitMessageRequest;
import org.tus.tx.rms.twophase.v1.GetMessageStatusRequest;
import org.tus.tx.rms.twophase.v1.MessageAck;
import org.tus.tx.rms.twophase.v1.MessageStatusResponse;
import org.tus.tx.rms.twophase.v1.PrepareMessageRequest;
import org.tus.tx.rms.twophase.v1.ReliableMessageServiceGrpc;
import org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest;
import org.tus.tx.rms.twophase.v1.RollbackMessageRequest;

@Component
@RequiredArgsConstructor
public class ReliableMessageGrpcService extends ReliableMessageServiceGrpc.ReliableMessageServiceImplBase {
    private final TwoPhaseReliableMessageService twoPhase;

    @Override
    public void prepareMessage(PrepareMessageRequest request, StreamObserver<MessageAck> responseObserver) {
        try {
            twoPhase.prepare(
                    request.getMessageId(),
                    request.getBizKey(),
                    request.getEventType(),
                    request.getDestination(),
                    request.getPayload(),
                    request.getTraceId(),
                    request.getTtlSeconds());
            responseObserver.onNext(
                    MessageAck.newBuilder().
                            setAccepted(true)
                            .setMessage(MessageStatus.PREPARED.getValue())
                            .build());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            GrpcErrors.toObserver(responseObserver, e);
        }
    }

    @Override
    public void commitMessage(CommitMessageRequest request, StreamObserver<MessageAck> responseObserver) {
        try {
            twoPhase.commit(request.getMessageId(), request.getBizKey());
            responseObserver.onNext(
                    MessageAck.newBuilder().
                            setAccepted(true)
                            .setMessage(MessageStatus.COMMITTED.getValue())
                            .build());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            GrpcErrors.toObserver(responseObserver, e);
        }
    }

    @Override
    public void rollbackMessage(RollbackMessageRequest request, StreamObserver<MessageAck> responseObserver) {
        try {
            twoPhase.rollback(request.getMessageId(), request.getReason());
            responseObserver.onNext(
                    MessageAck.newBuilder()
                            .setAccepted(true)
                            .setMessage(MessageStatus.ROLLED_BACK.getValue())
                            .build());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            GrpcErrors.toObserver(responseObserver, e);
        }
    }

    @Override
    public void getMessageStatus(GetMessageStatusRequest request, StreamObserver<MessageStatusResponse> responseObserver) {
        try {
            var row = twoPhase.get(request.getMessageId());
            MessageStatusResponse.Builder b = MessageStatusResponse.newBuilder()
                    .setMessageId(row.getMessageId())
                    .setBizKey(row.getBizKey())
                    .setEventType(row.getEventType())
                    .setStatus(row.getStatus())
                    .setSendTimes(row.getSendTimes())
                    .setLastError(row.getLastError() != null ? row.getLastError() : "")
                    .setUpdatedAtEpochMs(row.getUpdatedAt().toEpochMilli());

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
    public void reportConsumeResult(ReportConsumeResultRequest request, StreamObserver<MessageAck> responseObserver) {
        try {
            twoPhase.reportConsumeResult(
                    request.getMessageId(),
                    request.getBizKey(),
                    request.getResultCode(),
                    request.getResultPayload(),
                    request.getErrorCode(),
                    request.getErrorMessage());
            responseObserver.onNext(
                    MessageAck.newBuilder()
                            .setAccepted(true)
                            .setMessage(MessageStatus.RECORDED.getValue())
                            .build());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            GrpcErrors.toObserver(responseObserver, e);
        }
    }
}
