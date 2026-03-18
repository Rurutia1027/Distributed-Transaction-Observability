package org.tus.tx.service.message.grpc;

import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;
import org.tus.common.domain.model.PageResponse;
import org.tus.tx.service.message.entity.TransactionMessage;
import org.tus.tx.service.message.service.TransactionMessageApplicationService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMessageGrpcService extends TransactionMessageServiceGrpc.TransactionMessageServiceImplBase {

    private final TransactionMessageApplicationService applicationService;

    public TransactionMessageGrpcService(TransactionMessageApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public void publish(PublishRequest request, StreamObserver<MessageResponse> responseObserver) {
        try {
            TransactionMessage msg = new TransactionMessage();
            if (!request.getMessageId().isBlank()) {
                msg.setMessageId(request.getMessageId());
            }
            msg.setConsumerQueue(request.getConsumerQueue());
            msg.setMessageBody(request.getMessageBody());
            msg.setMessageDataType(request.getMessageDataType());
            msg.setField1(request.getField1());
            msg.setField2(request.getField2());
            msg.setField3(request.getField3());

            TransactionMessage saved = applicationService.createWaitingConfirm(msg);
            responseObserver.onNext(toProto(saved));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void confirmSend(ConfirmSendRequest request, StreamObserver<MessageResponse> responseObserver) {
        try {
            TransactionMessage updated = applicationService.markConfirmToSend(request.getMessageId());
            responseObserver.onNext(toProto(updated));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void listPage(ListPageRequest request, StreamObserver<ListPageResponse> responseObserver) {
        try {
            int start = request.getStart();
            int pageSize = request.getPageSize();
            String sortBy = request.getSortBy();
            boolean asc = request.getAsc();
            PageResponse<TransactionMessage> page = applicationService.listPage(start, pageSize, sortBy, asc);

            ListPageResponse.Builder b = ListPageResponse.newBuilder()
                    .setStart(page.getStart())
                    .setPageSize(page.getPageSize())
                    .setTotal(page.getTotal());
            List<TransactionMessage> elements = page.getElements() != null
                    ? new ArrayList<>(page.getElements())
                    : new ArrayList<>();
            for (TransactionMessage m : elements) {
                b.addElements(toProto(m));
            }
            responseObserver.onNext(b.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    private static MessageResponse toProto(TransactionMessage msg) {
        MessageResponse.Builder b = MessageResponse.newBuilder()
                .setMessageId(nullToEmpty(msg.getMessageId()))
                .setSendTimes(msg.getSendTimes())
                .setConsumerQueue(nullToEmpty(msg.getConsumerQueue()))
                .setLastError(nullToEmpty(msg.getLastError()));

        if (msg.getStatus() != null) {
            b.setStatus(MessageStatus.valueOf(msg.getStatus().name()));
        }
        Instant nextRetry = msg.getNextRetryAt();
        if (nextRetry != null) {
            b.setNextRetryAtEpochMs(nextRetry.toEpochMilli());
        }
        return b.build();
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}

