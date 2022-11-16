package com.byd.auth.manage.facade.storage.entity.dao;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/14 14:41
 * @description
 */
@Table(name = "sys_user_group_user")
public class SysUserGroupUser {

    private Long id;

    /**
     * 用户组id
     */
    @Column(name = "user_group_id")
    private Long userGroupId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
