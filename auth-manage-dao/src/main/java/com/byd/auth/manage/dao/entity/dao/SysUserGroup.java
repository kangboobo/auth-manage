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
 * @date 2022/11/14 14:29
 * @description 用户组实体
 */
@Table(name = "sys_user_group")
public class SysUserGroup extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /** 用户组ID */
    @Id
    private Long id;

    /**
     * 应用id
     */
    @Column(name = "app_id")
    @JSONField(name = "app_id")
    private Long appId;

    /**
     * 基地id
     */
    @Column(name = "base_id_str")
    @JSONField(name = "base_id_str")
    private String baseIdStr;

    /**
     * 用户组名称
     */
    @Column(name = "user_group_name")
    @JSONField(name = "user_group_name")
    private String userGroupName;

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

    public String getBaseIdStr() {
        return baseIdStr;
    }

    public void setBaseIdStr(String baseIdStr) {
        this.baseIdStr = baseIdStr;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("appId", getAppId())
                .append("baseIdStr", getBaseIdStr())
                .append("userGroupName", getUserGroupName())
                .append("deleteFlag", getDeleteFlag())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getCreateUser())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
