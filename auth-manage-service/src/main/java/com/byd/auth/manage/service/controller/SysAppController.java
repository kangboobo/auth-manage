package com.byd.auth.manage.common.controller;

import com.byd.auth.manage.common.authService.ISysAppService;
import com.byd.auth.manage.common.storage.entity.dao.SysApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:38
 * @description 应用管理controller
 */
@RestController
@RequestMapping("/v1/sys/app")
public class SysAppController {

    @Autowired
    private ISysAppService sysAppService;

    /**
     * 查询app列表不分页
     */
    @GetMapping(value = "/list")
    public Object getSysAppList(@RequestParam(value = "app_name", required = false) String appName) {
        return sysAppService.getSysAppList(appName);
    }

    /**
     * 查询app列表(分页)
     */
    @GetMapping(value = "/page/list")
    public Object getPageSysAppList(@RequestParam(value = "app_name", required = false) String appName,
                                    @RequestParam(value = "page_number", defaultValue = "1") Integer pageNumber,
                                    @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        return sysAppService.getSysAppPageList(appName, pageNumber, pageSize);
    }

    /**
     * 根据单独appId查询
     */
    @GetMapping(value = "/{app_id:\\d+}/info")
    public Object getSysAppById(@PathVariable(value = "app_id") Long appId) {
        return sysAppService.getSysAppById(appId);
    }

    /**
     * 新建｜修改应用
     * 
     * @return
     */
    @PostMapping(value = "/save")
    public Object insertOrUpdateSysApp(@RequestBody SysApp sysApp) {
        return sysAppService.insertOrUpdateSysApp(sysApp);
    }

    /**
     * 删除app
     */
    @DeleteMapping(value = "/{app_id:\\d+}/delete")
    public Object deleteSysAppById(@PathVariable(value = "app_id") Long appId) {
        return sysAppService.deleteSysAppById(appId);
    }
}
