package com.byd.auth.manage.common.storage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.byd.auth.manage.common.storage.entity.dao.SysUserGroup;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:31
 * @description
 */
@Repository
public interface SysUserGroupMapper extends Mapper<SysUserGroup> {

    Long insertSysUserGroup(@Param("userGroup") SysUserGroup userGroup);
}
