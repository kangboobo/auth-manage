package com.byd.auth.manage.dao.entity.vo;

import com.byd.auth.manage.dao.entity.dao.SysBase;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/9 11:43
 * @description
 */
public class SysBaseVo extends SysBase {

    private static final long serialVersionUID = 1L;

    /**
     * 应用集合
     */
    @JSONField(name = "sys_apps")
    List<BaseAuthVo> sysApps;

    public List<BaseAuthVo> getSysApps() {
        return sysApps;
    }

    public void setSysApps(List<BaseAuthVo> sysApps) {
        this.sysApps = sysApps;
    }
}
