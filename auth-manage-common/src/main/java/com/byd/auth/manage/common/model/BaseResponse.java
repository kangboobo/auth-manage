package com.byd.auth.manage.common.model;

import com.byd.auth.manage.common.constants.Constants;
import com.byd.auth.manage.common.exception.ErrorContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 15:07
 * @description
 */
@ApiModel(value = "通用PI接口返回", description = "Common Api Response")
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -8244372110143879374L;

    /**
     * 通用返回状态
     */
    @ApiModelProperty(value = "通用返回状态", required = true)
    private String success = "true";

    /**
     * 通用返回结果码(0:接口响应成功)
     */
    @ApiModelProperty(value = "通用返回结果码", required = true)
    private int code = 0;

    /**
     * 通用返回信息
     */
    @ApiModelProperty(value = "通用返回信息", required = true)
    private String msg;

    /**
     * 通用返回数据
     */
    @ApiModelProperty(value = "通用返回数据", required = true)
    private T data;

    public BaseResponse() {
    }

    public static BaseResponse getSuccessResponse() {
        return baseResponse(true, Constants.SUCCESS_CODE, Constants.SUCCESS_STR, null);
    }


    public static <T> BaseResponse<T> getSuccessResponse(T data) {
        return baseResponse(true, Constants.SUCCESS_CODE, Constants.SUCCESS_STR, data);
    }


    public static BaseResponse getFailedResponse(ErrorContext errorContext) {
        return baseResponse(false, errorContext.getCode(), errorContext.getMessage(), null);
    }

    public static <T> BaseResponse<T> getFailedResponse(ErrorContext errorContext, T data) {
        return baseResponse(false, errorContext.getCode(), errorContext.getMessage(), data);
    }


    private static <T> BaseResponse<T> baseResponse(Boolean success, int code, String msg, T data) {
        BaseResponse result = new BaseResponse();
        result.setSuccess(String.valueOf(success));
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("BaseResponse{");
        sb.append(", success='").append(this.success).append('\'');
        sb.append(", code='").append(this.code).append('\'');
        sb.append(", msg='").append(this.msg).append('\'');
        sb.append(", data=").append(this.data);
        sb.append('}');
        return sb.toString();
    }
}
