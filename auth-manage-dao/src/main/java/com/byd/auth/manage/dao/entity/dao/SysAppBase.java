package com.byd.auth.manage.dao.entity.dao;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 11:58
 * @description 应用-基地关联表
 */
@Table(name = "sys_app_base")
public class SysAppBase {

    /**
     * 应用-基地关联表主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用id
     */
    @Column(name = "app_id")
    private Long appId;

    /**
     * 基地id
     */
    @Column(name = "base_id")
    private Long baseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }
}
