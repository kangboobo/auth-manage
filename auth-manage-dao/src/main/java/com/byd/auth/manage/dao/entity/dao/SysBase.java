package com.byd.auth.manage.dao.entity.dao;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 10:58
 * @description 基地表
 */
@Table(name = "sys_base")
public class SysBase extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 基地主键
     */
    @Id
    private Long id;

    /**
     * 基地编码
     */
    @JSONField(name = "base_code")
    @Column(name = "base_code")
    private String baseCode;

    /**
     * 基地名称
     */
    @JSONField(name = "base_name'")
    @Column(name = "base_name")
    private String baseName;

    /**
     * 工厂代码
     */
    @JSONField(name = "factory_code")
    @Column(name = "factory_code")
    private String factoryCode;

    /**
     * 基地面积
     */
    private String area;

    /**
     * 基地介绍
     */
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("baseCode", getBaseCode())
                .append("baseName", getBaseName())
                .append("factoryCode", getFactoryCode())
                .append("area", getArea())
                .append("description", getDescription())
                .append("deleteFlag", getDeleteFlag())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser",getUpdateUser())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
