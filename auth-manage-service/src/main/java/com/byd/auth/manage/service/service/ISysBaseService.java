package com.byd.auth.manage.service.service;

import com.byd.auth.manage.dao.entity.dao.SysBase;
import com.byd.auth.manage.dao.entity.vo.SysBaseVo;

import java.util.List;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/9 10:55
 * @description
 */
public interface ISysBaseService {

    /**
     * 分页查询基地
     * @return
     */
    Object getSysBasePageList(String baseName, Integer pageNumber, Integer pageSize);

    /**
     * 查询单个基地信息
     */
    Object getSysBaseById(Long id);


    /**
     * 新增｜修改基地
     * @param
     * @return
     */
    Object insertOrUpdateSysBase(SysBaseVo sysBaseVo);

    /**
     * 删除单个基地
     */
    Object deleteSysBaseById(Long id);

    /**
     * 根据基地id集合查询基地信息列表
     * @param baseIds
     * @return
     */
    List<SysBase> selectSysBaseList(List<Long> baseIds);

    /**
     * 查询基地列表(不分页)
     * @return
     */
    Object getSysBaseList(Long appId);
}
