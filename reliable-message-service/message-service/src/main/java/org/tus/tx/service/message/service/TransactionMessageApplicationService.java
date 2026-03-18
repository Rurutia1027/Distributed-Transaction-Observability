package org.tus.tx.service.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.tus.common.domain.model.PageResponse;
import org.tus.common.domain.persistence.QueryService;
import org.tus.tx.service.message.entity.TransactionMessage;
import org.tus.tx.service.message.entity.MessageStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionMessageApplicationService {

    private final QueryService queryService;

    public TransactionMessage createWaitingConfirm(TransactionMessage msg) {
        if (msg.getMessageId() == null || msg.getMessageId().isBlank()) {
            msg.setMessageId(UUID.randomUUID().toString());
        }
        msg.setStatus(MessageStatus.WAITING_CONFIRM);
        msg.setSendTimes(0);
        msg.setNextRetryAt(null);
        msg.setLastError(null);
        queryService.save(msg);
        return msg;
    }

    public TransactionMessage markConfirmToSend(String messageId) {
        TransactionMessage msg = findByMessageId(messageId);
        msg.setStatus(MessageStatus.SENDING);
        msg.setNextRetryAt(Instant.now());
        queryService.save(msg);
        return msg;
    }

    public TransactionMessage findByMessageId(String messageId) {
        String hql = "from TransactionMessage m where m.messageId = :messageId";
        Map<String, Object> params = Map.of("messageId", messageId);
        Object result = queryService.querySingle(hql, params);
        if (result == null) {
            throw new IllegalArgumentException("messageId not found: " + messageId);
        }
        return (TransactionMessage) result;
    }

    public PageResponse<TransactionMessage> listPage(int start, int pageSize, String sortBy, boolean asc) {
        if (pageSize <= 0) {
            pageSize = 20;
        }
        if (start < 0) {
            start = 0;
        }
        String orderField = (sortBy == null || sortBy.isEmpty()) ? "createdDate" : sortBy;
        String orderDir = asc ? "asc" : "desc";

        String dataHql = "from TransactionMessage m order by m." + orderField + " " + orderDir;
        String countHql = "select count(m.id) from TransactionMessage m";

        Map<String, Object> params = Collections.emptyMap();

        @SuppressWarnings("unchecked")
        List<TransactionMessage> elements = queryService.pagedQuery(dataHql, params, start, pageSize);

        Number totalNumber = (Number) queryService.querySingle(countHql, params);
        int total = totalNumber != null ? totalNumber.intValue() : 0;

        PageResponse<TransactionMessage> page = new PageResponse<>();
        page.setStart(start);
        page.setPageSize(pageSize);
        page.setTotal(total);
        page.setElements(elements);
        return page;
    }
}
