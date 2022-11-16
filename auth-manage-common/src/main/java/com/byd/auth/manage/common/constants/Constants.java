package com.byd.auth.manage.common.constants;

/**
 * 常量类
 *
 * @author bai bin
 */
public interface Constants {

    /**
     * 记录日志的时候隐式传递 msgId, 方便定位问题(设置的地方和使用的地方需要在同一个线程)
     */
    String MSG_ID = "MSG-ID";

    /**
     * between类型值个数
     */
    int BETWEEN_VALUE_SIZE = 2;

    /**
     * 获取数据大小
     */
    int DATA_SIZE = 100;

    String COMMA = ",";

    String FILL_POINT = ".";

    String EMPTY_STR = "";

    String SUCCESS_STR = "success";

    int SUCCESS_CODE = 0;

    int INTEGER_ONE_VALUE = 1;

    byte BYTE_ZERO_VALUE = 0;

    byte BYTE_ONE_VALUE = 1;

    int ZERO_VALUE = 0;

    int MINUS_ONE = -1;
}
