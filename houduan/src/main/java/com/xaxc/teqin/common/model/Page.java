package com.xaxc.teqin.common.model;

import com.baomidou.mybatisplus.core.metadata.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Page<T> implements IPage<T> {
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    private long pageNo = 1L;

    private long pageSize = 15L;

    private long totalCount = -1L;

    protected String orderFields = null;

    protected String order = null;

    private List<T> records = new ArrayList<>();
    private List<OrderItem> orders = new ArrayList<>();
    private boolean searchCount = true;

    public Page() {
    }

    public Page(long pageNo, long pageSize, String orderFields, String order) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.orderFields = orderFields;
        this.order = order;
    }

    public Page(long pageNo, long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public void contactOrderAndField(Class clazz) {
        this.contactOrderAndField(this.order, this.orderFields, clazz);
    }

    public void contactOrderAndField(String order, String orderFields, Class clazz) {
        String filedName = this.convertPropToField(orderFields, clazz);
        if (StringUtils.hasText(filedName)) {
            if ("asc".equals(org.apache.commons.lang3.StringUtils.lowerCase(order))) {
                this.orders.add(OrderItem.asc(filedName));
            } else {
                this.orders.add(OrderItem.desc(filedName));
            }
        }

    }

    private String convertPropToField(String orderFields, Class clazz) {
        Class superClass = clazz.getSuperclass();
        if (null != superClass && superClass != Object.class) {
            Class currentClass;
            for(currentClass = clazz; superClass != BaseEntity.class; superClass = superClass.getSuperclass()) {
                currentClass = superClass;
            }

            TableInfo tableInfo = TableInfoHelper.getTableInfo(currentClass);
            Assert.isTrue(tableInfo != null, "tableInfo is null,convertPropToField fail");
            Optional<TableFieldInfo> orderFieldOptional = tableInfo.getFieldList().stream().filter((tableFieldInfo) -> tableFieldInfo.getProperty().equals(orderFields)).findFirst();
            if (orderFieldOptional.isPresent()) {
                return ((TableFieldInfo)orderFieldOptional.get()).getColumn();
            }
        }

        return null;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public boolean searchCount() {
        return this.searchCount;
    }

    public List<OrderItem> orders() {
        return this.orders;
    }

    public List<T> getRecords() {
        return this.records;
    }

    public IPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Transient
    public long getTotal() {
        return this.totalCount;
    }

    public IPage<T> setTotal(long total) {
        this.totalCount = total;
        return this;
    }

    @Transient
    public long getSize() {
        return this.pageSize;
    }

    public IPage<T> setSize(long size) {
        this.pageSize = size;
        return this;
    }

    @Transient
    public long getCurrent() {
        return this.pageNo;
    }

    public IPage<T> setCurrent(long current) {
        this.pageNo = current;
        return this;
    }

    @Transient
    public long getPages() {
        return IPage.super.getPages();
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

    public long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }


    public long getTotalPages() {
        return this.getPages();
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
}

