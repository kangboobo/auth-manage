package com.byd.auth.manage.common.exception;


import com.byd.auth.manage.common.constants.ResultCode;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see WebMvcConfigurer
 */
public class ServiceException extends RuntimeException {

    private ResultCode resultCode;

    private ErrorContext errorContext;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public ServiceException(ErrorContext errorContext) {
        super(errorContext.getMessage());
        this.errorContext = errorContext;
    }

    public ErrorContext getErrorContext() {
        return errorContext;
    }

    public void setErrorContext(ErrorContext errorContext) {
        this.errorContext = errorContext;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
