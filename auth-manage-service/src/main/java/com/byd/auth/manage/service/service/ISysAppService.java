package com.byd.auth.manage.service.service;


import com.byd.auth.manage.dao.entity.dao.SysApp;

import java.util.List;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:54
 * @description
 */
public interface ISysAppService {

    /**
     * 查询sysApp不分页
     * @return
     */
    Object getSysAppList(String appName);

    /**
     * 分页查询应用
     * @return
     */
    Object getSysAppPageList(String appName, Integer pageNumber, Integer pageSize);

    /**
     * 查询单个应用信息
     */
    Object getSysAppById(Long id);


    /**
     * 新增｜修改权限应用
     * @param sysApp
     * @return
     */
    Object insertOrUpdateSysApp(SysApp sysApp);

    /**
     * 删除权限应用
     */
    Object deleteSysAppById(Long id);

    List<SysApp> selectSysAppList(List<Long> appIds);

}
