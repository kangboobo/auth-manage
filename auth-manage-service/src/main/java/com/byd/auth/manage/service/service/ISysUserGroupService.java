package com.byd.auth.manage.service.service;

import com.byd.auth.manage.dao.entity.vo.SysUserGroupUserVo;
import com.byd.auth.manage.dao.entity.vo.SysUserGroupVo;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/11 10:38
 * @description
 */
public interface ISysUserGroupService {

    /**
     * 根据用户组id查询单个用户组信息
     * @param userGroupId 用户组id
     * @return
     */
    Object getSysUserGroupById(Long userGroupId);


    /**
     * 分页查询用户组
     * @param pageNumber
     * @param pageSize
     * @param userGroupName
     * @return
     */
    Object getPageSysUserGroupList(Integer pageNumber, Integer pageSize, String userGroupName);

    /**
     * 增加｜修改用户组
     * @param sysUserGroupVo 用户组vo
     * @return
     */
    Object insertOrUpdateUserGroup(SysUserGroupVo sysUserGroupVo);

    /**
     * 根据用户组id删除用户组
     * @param userGroupId 用户组id
     * @return
     */
    Object deleteSysUserGroupById(Long userGroupId);

    /**
     * 增加｜修改用户组和用户关联关系
     * @return
     */
    Object saveUserGroupUser(SysUserGroupUserVo userGroupUserVo);

    /**
     * 根据用户组id查询对应的用户组和用户信息
     * @return
     */
    Object getUserGroupUserById(Long userGroupId);
}
