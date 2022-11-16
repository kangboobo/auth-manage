package com.byd.auth.manage.dao.entity.dao;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 10:58
 * @description 应用表
 */
@Table(name = "sys_app")
public class SysApp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 应用主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用编码
     */
    @JSONField(name = "app_code")
    @Column(name = "app_code")
    private String appCode;

    /**
     * 应用名称
     */
    @JSONField(name = "app_name'")
    @Column(name = "app_name")
    private String appName;

    /**
     * 工厂代码
     */
    @JSONField(name = "factory_code")
    @Column(name = "factory_code")
    private String factoryCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("appCode", getAppCode())
                .append("appName", getAppName())
                .append("factoryCode", getFactoryCode())
                .append("deleteFlag", getDeleteFlag())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser",getUpdateUser())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }

}
