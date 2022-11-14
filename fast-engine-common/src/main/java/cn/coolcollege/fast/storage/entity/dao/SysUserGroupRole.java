package cn.coolcollege.fast.storage.entity.dao;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/14 14:37
 * @description
 */
@Table(name = "sys_user_group_role")
public class SysUserGroupRole {

    private Long id;

    /**
     * 用户组id
     */
    @Column(name = "user_group_id")
    private Long userGroupId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
