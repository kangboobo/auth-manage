package com.byd.auth.manage.common.enums;

/**
 * 日期类型
 */
public enum DateTypeEnum {

    DAY("day"),
    MONTH("month"),
    YEAR("year");

    private final String value;

    DateTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
