package com.xaxc.teqin.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 * 用于接口返回标准化的数据格式
 */
@Data
public class ResponseResult<T> implements Serializable {

    /**
     * 状态码：0-成功，非0-失败
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    // 成功响应（无数据）
    public static <T> ResponseResult<T> success() {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(0);
        result.setMsg("操作成功");
        return result;
    }

    // 成功响应（带数据）
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(0);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 失败响应（自定义消息）
    public static <T> ResponseResult<T> error(String msg) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(500); // 默认错误码
        result.setMsg(msg);
        return result;
    }

    // 失败响应（自定义错误码和消息）
    public static <T> ResponseResult<T> error(Integer code, String msg) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}