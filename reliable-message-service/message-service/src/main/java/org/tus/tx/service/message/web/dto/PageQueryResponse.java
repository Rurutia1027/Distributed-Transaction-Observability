package org.tus.tx.service.message.web.dto;

import java.util.List;

public class PageQueryResponse<T> {
    private int start;
    private int pageSize;
    private long total;
    private List<T> elements;

    public int getStart() { return start; }
    public void setStart(int start) { this.start = start; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public List<T> getElements() { return elements; }
    public void setElements(List<T> elements) { this.elements = elements; }
}
