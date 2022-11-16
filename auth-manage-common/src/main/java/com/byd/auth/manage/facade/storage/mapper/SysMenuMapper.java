package com.byd.auth.manage.facade.storage.mapper;

import com.byd.auth.manage.facade.storage.entity.dao.SysMenu;
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

//    /**
//     * 查询系统菜单列表
//     *
//     * @param menu 菜单信息
//     * @return 菜单列表
//     */
//     List<SysMenu> selectMenuList(SysMenu menu);
//
//     List<SysMenu> selectMenuListByUserId(SysMenu menu);
//
//    /**
//     * 根据用户ID查询菜单
//     *
//     * @return 菜单列表
//     */
//    List<SysMenu> selectMenuTreeAll();
//
//    /**
//     * 根据用户ID查询菜单
//     *
//     * @param userId 用户ID
//     * @return 菜单列表
//     */
//     List<SysMenu> selectMenuTreeByUserId(Long userId);
//
//    /**
//     * 校验菜单名称是否唯一
//     *
//     * @param menuName
//     *            菜单名称
//     * @param parentId
//     *            父菜单ID
//     * @return 结果
//     */
//    SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);
//
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
    List<Long> selectMenuIdsByAppIdAndRoleId(@Param("appId") Long appId, @Param("roleId") Long roleId);
//
//    /**
//     * 增加菜单
//     * @param sysMenu
//     * @return
//     */
//     Integer insertSysMenu(@Param("sysMenu") SysMenu sysMenu);
//
//    /**
//     * 根据角色ID查询菜单树信息
//     *
//     * @param roleId 角色ID
//     * @param menuCheckStrictly 菜单树选择项是否关联显示
//     * @return 选中菜单列表
//     */
//     List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);
}
