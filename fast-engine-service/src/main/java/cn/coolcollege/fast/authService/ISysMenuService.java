package cn.coolcollege.fast.authService;

import cn.coolcollege.fast.storage.entity.dao.SysMenu;

import java.util.List;
import java.util.Objects;

/**
 * 菜单 业务层
 * 
 * @author ruoyi
 */
public interface ISysMenuService {
    // /**
    // * 根据用户查询系统菜单列表
    // *
    // * @param userId 用户ID
    // * @return 菜单列表
    // */
    // List<SysMenu> selectMenuList(Long userId);
    //
    // /**
    // * 根据用户查询系统菜单列表
    // *
    // * @param menu 菜单信息
    // * @param userId 用户ID
    // * @return 菜单列表
    // */
    // List<SysMenu> selectMenuList(SysMenu menu, Long userId);

    // /**
    // * 根据用户ID查询菜单树信息
    // *
    // * @param userId 用户ID
    // * @return 菜单列表
    // */
    // List<SysMenu> selectMenuTreeByUserId(Long userId);

    // /**
    // * 根据角色ID查询菜单树信息
    // *
    // * @param roleId 角色ID
    // * @return 选中菜单列表
    // */
    // List<Long> selectMenuListByRoleId(Long roleId);

    // /**
    // * 构建前端所需要树结构
    // *
    // * @param menus 菜单列表
    // * @return 树结构列表
    // */
    // List<SysMenu> buildMenuTree(List<SysMenu> menus);

    // /**
    // * 构建前端所需要下拉树结构
    // *
    // * @param menus 菜单列表
    // * @return 下拉树结构列表
    // */
    // List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    //
    // /**
    // * 是否存在菜单子节点
    // *
    // * @param menuId 菜单ID
    // * @return 结果 true 存在 false 不存在
    // */
    // boolean hasChildByMenuId(Integer menuId);
    //
    // /**
    // * 查询菜单是否存在角色
    // *
    // * @param menuId 菜单ID
    // * @return 结果 true 存在 false 不存在
    // */
    // boolean checkMenuExistRole(Long menuId);


     /**
     * 根据应用ID查询菜单树信息
     *
     * @param appId 应用id
     * @return 菜单树信息
     */
     Object selectMenuTreeByAppId(Long appId);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId
     *            菜单ID
     * @return 菜单信息
     */
    Object selectSysMenuById(Long menuId);

    /**
     * 新增保存菜单信息
     *
     * @param menu
     *            菜单信息
     * @return 结果
     */
    Object insertOrUpdateSysMenu(SysMenu menu);

    /**
     * 删除菜单管理信息
     *
     * @param menuId
     *            菜单ID
     * @return 结果
     */
    Object deleteMenuById(Long menuId);
    //
    // /**
    // * 校验菜单名称是否唯一
    // *
    // * @param menu 菜单信息
    // * @return 结果
    // */
    // Object checkMenuNameUnique(SysMenu menu);
}
