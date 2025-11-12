package com.xaxc.teqin.common.error;

public enum CommonErrorEnum implements IErrorInfo {
    NO_TOKEN(600101, "token is null");



    private String msg;

    private int code;

    CommonErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }

    @Override
    public Integer getErrorCode() {
        return this.code;
    }
}