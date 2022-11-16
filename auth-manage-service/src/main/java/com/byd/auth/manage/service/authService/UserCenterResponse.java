package com.byd.auth.manage.common.authService;

import lombok.Data;

import java.util.Objects;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/15 13:49
 * @description 用户中心接口响应
 */
@Data
public class UserCenterResponse {
    private Integer code;

    private String msg;

    private Objects data;
}
