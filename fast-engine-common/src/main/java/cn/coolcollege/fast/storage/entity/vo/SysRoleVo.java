package cn.coolcollege.fast.storage.entity.vo;

import cn.coolcollege.fast.storage.entity.dao.SysRole;
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

    @JSONField(name = "sys_base_list")
    private List<BaseAuthVo> sysBaseList;

    public BaseAuthVo getSysApp() {
        return sysApp;
    }

    public void setSysApp(BaseAuthVo sysApp) {
        this.sysApp = sysApp;
    }

    public List<BaseAuthVo> getSysBaseList() {
        return sysBaseList;
    }

    public void setSysBaseList(List<BaseAuthVo> sysBaseList) {
        this.sysBaseList = sysBaseList;
    }
}
