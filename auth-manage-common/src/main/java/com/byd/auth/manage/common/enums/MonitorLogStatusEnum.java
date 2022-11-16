package com.byd.auth.manage.common.enums;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/16 11:13
 * @description
 */
public enum MonitorLogStatusEnum {
    SUCCESS("S"),
    FAILED("F"),
    EXCEPTION("E");

    private String value;

    private MonitorLogStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
