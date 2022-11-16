package com.byd.auth.manage.facade.storage.mapper;

import java.util.List;

import com.byd.auth.manage.facade.storage.entity.dao.SysUserGroupRole;
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
public interface SysUserGroupRoleMapper extends Mapper<SysUserGroupRole> {

    Integer insertList(@Param("list") List<SysUserGroupRole> list);

    List<SysUserGroupRole> selectUserGroupRoles(@Param("list") List<Long> userGroupIds);
}
