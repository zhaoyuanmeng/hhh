package com.xaxc.teqin.common.model;

import org.springframework.util.Assert;

public class PageCondition<T extends BaseEntity> extends Condition<T> {
    private PageParam<T> page;

    public PageCondition() {
    }

    public PageCondition(T entity) {
        super(entity);
    }

    public PageCondition(T entity, PageParam<T> pageParam) {
        super(entity);
        this.page = pageParam;
    }

    public void setPage(PageParam<T> pageParam) {
        this.page = pageParam;
    }

    public Page<T> getPage() {
        Assert.isTrue(null != this.page, "PageParam is null please check PageParam");
        return this.page.getPage();
    }
}
