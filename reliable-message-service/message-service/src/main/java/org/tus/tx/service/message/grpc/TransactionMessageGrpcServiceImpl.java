package org.tus.tx.service.message.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tus.tx.service.message.converter.TransactionMessageProtoConverter;
import org.tus.tx.service.message.exceptions.MessageBizException;
import org.tus.tx.service.message.service.TransactionMessageApplicationService;

import java.util.function.Supplier;

/**
 * gRPC server implementation (business logic to be filled later).
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionMessageGrpcServiceImpl extends TransactionMessageServiceGrpc.TransactionMessageServiceImplBase {

    private final TransactionMessageApplicationService transactionMessageApplicationService;

    @Override
    public void saveMessageWaitingConfirm(SaveMessageWaitingConfirmRequest request,
                                          StreamObserver<SaveMessageWaitingConfirmResponse> responseObserver) {
        runAndReply(responseObserver, () -> {
            // TODO: persist message and set to WAITING_CONFIRM
            TransactionMessageProtoConverter.toEntity(request.getMessage());
            return SaveMessageWaitingConfirmResponse.newBuilder().setResult(0).build();
        });
    }

    @Override
    public void confirmAndSendMessage(ConfirmAndSendRequest request,
                                      StreamObserver<ConfirmAndSendResponse> responseObserver) {
        runAndReply(responseObserver, () -> ConfirmAndSendResponse.getDefaultInstance(), () -> {
            // TODO: confirm message + publish to MQ
        });
    }

    @Override
    public void saveAndSendMessage(SaveAndSendRequest request,
                                   StreamObserver<SaveAndSendResponse> responseObserver) {
        runAndReply(responseObserver, () -> {
            // TODO: persist message + publish to MQ
            TransactionMessageProtoConverter.toEntity(request.getMessage());
            return SaveAndSendResponse.newBuilder().setResult(0).build();
        });
    }

    @Override
    public void directSendMessage(DirectSendRequest request,
                                  StreamObserver<DirectSendResponse> responseObserver) {
        runAndReply(responseObserver, () -> DirectSendResponse.getDefaultInstance(), () -> {
            // TODO: publish to MQ directly (no DB state change)
            TransactionMessageProtoConverter.toEntity(request.getMessage());
        });
    }

    @Override
    public void reSendMessage(ReSendMessageRequest request,
                              StreamObserver<ReSendMessageResponse> responseObserver) {
        runAndReply(responseObserver, () -> ReSendMessageResponse.getDefaultInstance(), () -> {
            // TODO: resend (increment send times, publish)
            TransactionMessageProtoConverter.toEntity(request.getMessage());
        });
    }

    @Override
    public void reSendMessageByMessageId(ReSendByMessageIdRequest request,
                                         StreamObserver<ReSendByMessageIdResponse> responseObserver) {
        runAndReply(responseObserver, () -> ReSendByMessageIdResponse.getDefaultInstance(), () -> {
            // TODO: query by messageId then resend
        });
    }

    @Override
    public void setMessageToAlreadyDead(SetMessageToAlreadyDeadRequest request,
                                       StreamObserver<SetMessageToAlreadyDeadResponse> responseObserver) {
        runAndReply(responseObserver, () -> SetMessageToAlreadyDeadResponse.getDefaultInstance(), () -> {
            // TODO: mark message as dead
        });
    }

    @Override
    public void deleteMessageByMessageId(DeleteMessageRequest request,
                                         StreamObserver<DeleteMessageResponse> responseObserver) {
        runAndReply(responseObserver, () -> DeleteMessageResponse.getDefaultInstance(), () -> {
            // TODO: delete by messageId
        });
    }

    @Override
    public void reSendAllDeadMessageByQueueName(ReSendAllDeadRequest request,
                                                StreamObserver<ReSendAllDeadResponse> responseObserver) {
        runAndReply(responseObserver, () -> ReSendAllDeadResponse.getDefaultInstance(), () -> {
            // TODO: scan dead messages by queue name then resend in batches
        });
    }

    @Override
    public void listPage(ListPageRequest request, StreamObserver<ListPageResponse> responseObserver) {
        runAndReply(responseObserver, () -> {
            var page = transactionMessageApplicationService.listPage(
                    request.getStart(),
                    request.getPageSize(),
                    request.getSortBy(),
                    request.getAsc()
            );
            return TransactionMessageProtoConverter.toListPageProto(page);
        });
    }

    private <T> void runAndReply(StreamObserver<T> responseObserver, Supplier<T> responseSupplier) {
        try {
            responseObserver.onNext(responseSupplier.get());
            responseObserver.onCompleted();
        } catch (MessageBizException e) {
            log.warn("MessageBizException: code={}, msg={}", e.getErrorCode(), e.getMessage());
            responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .withCause(e)
                    .asException());
        } catch (Exception e) {
            log.error("Unexpected error", e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription(e.getMessage())
                    .withCause(e)
                    .asException());
        }
    }

    private <T> void runAndReply(StreamObserver<T> responseObserver, Supplier<T> responseSupplier, Runnable action) {
        try {
            action.run();
            responseObserver.onNext(responseSupplier.get());
            responseObserver.onCompleted();
        } catch (MessageBizException e) {
            log.warn("MessageBizException: code={}, msg={}", e.getErrorCode(), e.getMessage());
            responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .withCause(e)
                    .asException());
        } catch (Exception e) {
            log.error("Unexpected error", e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription(e.getMessage())
                    .withCause(e)
                    .asException());
        }
    }
}
