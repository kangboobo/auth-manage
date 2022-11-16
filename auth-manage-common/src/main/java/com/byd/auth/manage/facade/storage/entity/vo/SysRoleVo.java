package com.byd.auth.manage.facade.storage.entity.vo;

import com.byd.auth.manage.facade.storage.entity.dao.SysRole;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/11 14:35
 * @description
 */
public class SysRoleVo extends SysRole {

    private static final long serialVersionUID = 1L;

    @JSONField(name = "sys_app")
    private BaseAuthVo sysApp;

    @JSONField(name = "sys_bases")
    private List<BaseAuthVo> sysBases;

    @JSONField(name = "sys_menus")
    private List<BaseAuthVo> sysMenus;

    public BaseAuthVo getSysApp() {
        return sysApp;
    }

    public void setSysApp(BaseAuthVo sysApp) {
        this.sysApp = sysApp;
    }

    public List<BaseAuthVo> getSysBases() {
        return sysBases;
    }

    public void setSysBases(List<BaseAuthVo> sysBases) {
        this.sysBases = sysBases;
    }

    public List<BaseAuthVo> getSysMenus() {
        return sysMenus;
    }

    public void setSysMenus(List<BaseAuthVo> sysMenus) {
        this.sysMenus = sysMenus;
    }
}
