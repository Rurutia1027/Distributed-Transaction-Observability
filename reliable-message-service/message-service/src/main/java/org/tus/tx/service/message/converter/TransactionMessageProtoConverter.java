package org.tus.tx.service.message.converter;

import org.tus.common.domain.model.PageResponse;
import org.tus.tx.service.message.entity.TransactionMessage;
import org.tus.tx.service.message.grpc.ListPageResponse;
import org.tus.tx.service.message.grpc.TransactionMessageProto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts between gRPC proto messages and domain entities.
 */
public final class TransactionMessageProtoConverter {

    private TransactionMessageProtoConverter() {}

    public static TransactionMessage toEntity(TransactionMessageProto proto) {
        if (proto == null) {
            return null;
        }
        TransactionMessage entity = new TransactionMessage();
        entity.setMessageId(proto.getMessageId());
        entity.setMessageBody(proto.getMessageBody());
        entity.setMessageDataType(proto.getMessageDataType());
        entity.setConsumerQueue(proto.getConsumerQueue());
        entity.setMessageSendTimes(proto.getMessageSendTimes() == 0 ? null : proto.getMessageSendTimes());
        entity.setAlreadyDead(proto.getAlreadyDead());
        entity.setField1(proto.getField1());
        entity.setField2(proto.getField2());
        entity.setField3(proto.getField3());
        if (proto.getId() != null && !proto.getId().isEmpty()) {
            entity.setId(proto.getId());
        }
        if (proto.getCreatedDate() != 0L) {
            entity.setCreatedDate(new java.util.Date(proto.getCreatedDate()));
        }
        return entity;
    }

    public static TransactionMessageProto toProto(TransactionMessage entity) {
        if (entity == null) {
            return TransactionMessageProto.getDefaultInstance();
        }
        TransactionMessageProto.Builder b = TransactionMessageProto.newBuilder()
                .setMessageId(nullToEmpty(entity.getMessageId()))
                .setMessageBody(nullToEmpty(entity.getMessageBody()))
                .setMessageDataType(nullToEmpty(entity.getMessageDataType()))
                .setConsumerQueue(nullToEmpty(entity.getConsumerQueue()))
                .setAlreadyDead(nullToEmpty(entity.getAlreadyDead()))
                .setField1(nullToEmpty(entity.getField1()))
                .setField2(nullToEmpty(entity.getField2()))
                .setField3(nullToEmpty(entity.getField3()));
        if (entity.getMessageSendTimes() != null) {
            b.setMessageSendTimes(entity.getMessageSendTimes());
        }
        if (entity.getId() != null) {
            b.setId(entity.getId());
        }
        if (entity.getCreatedDate() != null) {
            b.setCreatedDate(entity.getCreatedDate().getTime());
        }
        return b.build();
    }

    public static List<TransactionMessageProto> toProtoList(List<TransactionMessage> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(TransactionMessageProtoConverter::toProto).collect(Collectors.toList());
    }

    public static ListPageResponse toListPageProto(PageResponse<TransactionMessage> page) {
        if (page == null) {
            return ListPageResponse.getDefaultInstance();
        }
        return ListPageResponse.newBuilder()
                .setStart(page.getStart())
                .setPageSize(page.getPageSize())
                .setTotal(page.getTotal())
                .addAllElements(toProtoList(page.getElements() != null ? new ArrayList<>(page.getElements()) : new ArrayList<>()))
                .build();
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
