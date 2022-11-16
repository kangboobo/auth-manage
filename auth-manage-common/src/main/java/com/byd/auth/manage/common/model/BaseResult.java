package com.byd.auth.manage.common.model;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/16 11:14
 * @description
 */
public class BaseResult {
    private Boolean success = true;
    private String resultCode;
    private String message;

    public BaseResult() {
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
