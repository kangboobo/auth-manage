package com.byd.auth.manage.dao.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/19 10:05
 * @description
 */
public class RoleMenuDto {

    @JSONField(name = "role_id")
    private Long roleId;

    @JSONField(name = "menu_id")
    private Long menuId;

    @JSONField(name = "menu_name")
    private String menuName;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
