package com.xaxc.teqin.common.error;

/**
 * @author sean.zhou
 * @version 0.1
 */
public interface IErrorInfo {

    /**
     * Get error message.
     * @return error message
     */
    String getErrorMsg();

    /**
     * Get error code.
     * @return error code
     */
    Integer getErrorCode();

}
