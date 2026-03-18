package org.tus.tx.service.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.tus.common.domain.model.PageResponse;
import org.tus.common.domain.persistence.QueryService;
import org.tus.tx.service.message.entity.TransactionMessage;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TransactionMessageApplicationService {

    private final QueryService queryService;

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
