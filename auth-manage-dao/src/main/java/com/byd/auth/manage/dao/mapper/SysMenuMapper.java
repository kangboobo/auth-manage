package com.byd.auth.manage.dao.mapper;

import com.byd.auth.manage.dao.entity.dao.SysMenu;
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
public interface SysMenuMapper extends Mapper<SysMenu> {

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
     Integer hasChildByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    List<SysMenu> selectSysMenuTreeByAppId(@Param("appId") Long appId);


    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    List<SysMenu> selectSysMenuTreeByAppIdAndRoleId(@Param("appId") Long appId, @Param("roleId") Long roleId);

    /**
     * 根据应用id和角色Id查询菜单Id集合
     * @return
     */
    List<Long> selectMenuIdsById(@Param("appId") Long appId, @Param("roleId") Long roleId);
}
