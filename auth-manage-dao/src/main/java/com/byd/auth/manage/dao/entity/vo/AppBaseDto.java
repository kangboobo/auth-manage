package com.byd.auth.manage.dao.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/17 16:20
 * @description
 */
public class AppBaseDto {

    @JSONField(name = "app_id")
    private Long appId;

    @JSONField(name = "app_name")
    private String appName;

    @JSONField(name = "base_id")
    private Long baseId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }
}
