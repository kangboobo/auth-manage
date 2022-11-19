package com.byd.auth.manage.dao.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/19 10:05
 * @description
 */
public class UserGroupRoleDto {

    @JSONField(name = "user_group_id")
    private Long userGroupId;

    @JSONField(name = "role_id")
    private Long roleId;

    @JSONField(name = "role_name")
    private String roleName;

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
