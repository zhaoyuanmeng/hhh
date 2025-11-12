package com.xaxc.teqin.component;

import com.xaxc.teqin.common.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    private static final String INTERNAL_ERROR_MSG = "系统服务异常，请稍后重试";

    /**
     * Please do not return directly like this, there is a risk.
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error("系统异常： {}", e.getMessage());
        return ResponseResult.error(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseResult nullPointerExceptionHandler(NullPointerException e) {
        log.error("空指针异常： {}", e.getMessage());
        return ResponseResult.error(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseResult methodArgumentNotValidExceptionHandler(BindException e) {
        String errorMsg = Optional.ofNullable(e.getFieldError())
                .map(err -> String.format("%s: %s", err.getField(), err.getDefaultMessage()))
                .orElse("请求参数校验失败");
        log.error("参数校验异常： {} {}", errorMsg, e);
        return ResponseResult.error(errorMsg);
    }

}
