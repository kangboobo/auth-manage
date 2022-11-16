package com.byd.auth.manage.facade.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/15 11:39
 * @description 用户中心配置
 */
@Component
@ConfigurationProperties(prefix = "sys.user.config")
@Data
public class SysUserConfig {

    /**
     * 获取部门列表API
     */
    private String getDepartmentListApi;

    /**
     * 获取用户列表API
     */
    private String getUserListApi;

}
