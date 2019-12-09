package com.tuacy.usermanager.exception;

/**
 * @name: BusinessException
 * @author: tuacy.
 * @date: 2019/12/9.
 * @version: 1.0
 * @Description:
 */
public class BusinessException extends Exception {

    private int errorCode;

    public BusinessException(int errorCode) {
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
