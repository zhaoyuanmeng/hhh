package com.xaxc.teqin.common.model;

import java.io.Serializable;

public class Condition<T extends BaseEntity> implements Serializable {

    protected T entity;

    public Condition() {
    }

    public Condition(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return this.entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
