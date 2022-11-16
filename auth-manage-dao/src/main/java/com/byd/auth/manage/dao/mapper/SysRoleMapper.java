package com.byd.auth.manage.dao.mapper;

import com.byd.auth.manage.dao.entity.dao.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import tk.mybatis.mapper.common.Mapper;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:31
 * @description
 */
@Repository
public interface SysRoleMapper extends Mapper<SysRole> {

    Long insertSysRole(@Param("sysRole") SysRole sysRole);
}
