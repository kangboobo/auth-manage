package com.byd.auth.manage.dao.mapper;

import com.byd.auth.manage.dao.entity.dao.SysRoleMenu;
import com.byd.auth.manage.dao.entity.dao.SysUserGroupRole;
import com.byd.auth.manage.dao.entity.dto.RoleMenuDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:31
 * @description
 */
@Repository
public interface SysRoleMenuMapper extends Mapper<SysRoleMenu> {

    Integer insertList(@Param("list") List<SysRoleMenu> list);

    List<RoleMenuDto> selectRoleMenuDtoListByRoleIds(@Param("list") List<Long> roleIds);
}
