package com.xaxc.teqin.common.model;


public class PageParam<T> {

    private long pageNo = 1L;

    private long pageSize = 15L;

    private String orderFields = null;

    private String order = null;

    public PageParam() {
    }

    public PageParam(long pageNo, long pageSize, String orderFields, String order) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.orderFields = orderFields;
        this.order = order;
    }

    public PageParam(long pageNo, long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public long getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public long getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderFields() {
        return this.orderFields;
    }

    public void setOrderFields(String orderFields) {
        this.orderFields = orderFields;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Page<T> getPage() {
        return new Page<T>(this.pageNo, this.pageSize, this.orderFields, this.order);
    }
}
