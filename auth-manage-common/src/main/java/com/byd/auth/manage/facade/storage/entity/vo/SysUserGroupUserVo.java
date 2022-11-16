package com.byd.auth.manage.facade.storage.entity.vo;

import com.byd.auth.manage.facade.storage.entity.dao.SysUserGroup;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/15 13:11
 * @description
 */
public class SysUserGroupUserVo extends SysUserGroup {

    private static final long serialVersionUID = 1L;

    @JSONField(name = "sys_users")
    private List<SysUserVo> sysUsers;

    public List<SysUserVo> getSysUsers() {
        return sysUsers;
    }

    public void setSysUsers(List<SysUserVo> sysUsers) {
        this.sysUsers = sysUsers;
    }
}
