package com.byd.auth.manage.dao.mapper;

import java.util.List;

import com.byd.auth.manage.dao.entity.dao.SysUserGroupRole;
import com.byd.auth.manage.dao.entity.dto.UserGroupRoleDto;
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

    List<UserGroupRoleDto> selectUserGroupRoleDtoList(@Param("list") List<Long> userGroupIds);
}
