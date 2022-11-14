package cn.coolcollege.fast.authService;

import cn.coolcollege.fast.storage.entity.dao.SysMenu;

/**
 * 菜单 业务层
 * 
 * @author baibin
 */
public interface ISysMenuService {

     /**
     * 根据应用ID查询菜单树信息
     *
     * @param appId 应用id
     * @return 菜单树信息
     */
     Object selectMenuTreeById(Long appId, Long roleId);

    /**
     * 根据应用ID和角色ID查询关联的菜单ID集合
     * @param appId 应用id
     * @param roleId 角色id
     * @return
     */
    Object selectMenuIdsByAppIdAndRoleId(Long appId, Long roleId);

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
}
