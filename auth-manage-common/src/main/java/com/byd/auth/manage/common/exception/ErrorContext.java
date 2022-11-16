package com.byd.auth.manage.common.exception;

import java.io.Serializable;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 15:27
 * @description
 */
public class ErrorContext implements Serializable {
    private static final long serialVersionUID = -37281567093834869L;
    private int code;
    private String message;

    public ErrorContext(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorContext(int code, String message, Object data) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("ResponseContext{");
        sb.append("code='").append(this.code).append('\'');
        sb.append(", message='").append(this.message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
