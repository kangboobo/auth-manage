package com.byd.auth.manage.common.authService.impl;

import java.util.List;
import java.util.Objects;

import com.byd.auth.manage.common.constants.BaseResponse;
import com.byd.auth.manage.common.constants.Constants;
import com.byd.auth.manage.common.exception.AuthManageErrConstant;
import com.byd.auth.manage.common.storage.entity.dao.SysMenu;
import com.byd.auth.manage.common.storage.mapper.SysMenuMapper;
import com.byd.auth.manage.common.storage.mapper.SysRoleMapper;
import com.byd.auth.manage.common.storage.mapper.SysRoleMenuMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byd.auth.manage.common.authService.ISysMenuService;
import lombok.extern.slf4j.Slf4j;

/**
 * 菜单 业务层处理
 *
 * @author baibin
 */
@Service
@Slf4j
public class SysMenuServiceImpl implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    /**
     * 根据应用ID查询菜单树信息
     *
     * @param appId
     *            应用id
     * @param roleId
     *            角色id
     * @return 菜单树信息
     */
    @Override
    public Object selectMenuTreeById(Long appId, Long roleId) {
        log.info("selectMenuTreeByAppId start, appId={}, roleId={}", appId, roleId);
        if (Objects.isNull(appId)) {
            log.error("selectMenuTreeByAppId appId is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        List<SysMenu> menus;
        try {
            if (Objects.isNull(roleId)) {
                menus = menuMapper.selectSysMenuTreeByAppId(appId);
            } else {
                menus = menuMapper.selectSysMenuTreeByAppIdAndRoleId(appId, roleId);
            }
        } catch (Exception e) {
            log.error("selectMenuTreeByAppId err, appId={}", appId, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse(getChildPerms(menus, 0L));
    }

    /**
     * 根据应用ID和角色ID查询关联的菜单ID集合
     *
     * @param appId  应用id
     * @param roleId 角色id
     * @return
     */
    @Override
    public Object selectMenuIdsByAppIdAndRoleId(Long appId, Long roleId) {
        log.info("selectMenuIdsById start, appId={}, roleId={}", appId, roleId);
        if(Objects.isNull(appId) || Objects.isNull(roleId)){
            log.error("selectMenuIdsById appId or roleId is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        List<Long> menuIds = menuMapper.selectMenuIdsByAppIdAndRoleId(appId, roleId);
        return BaseResponse.getSuccessResponse(menuIds);
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId
     *            菜单ID
     * @return 菜单信息
     */
    @Override
    public Object selectSysMenuById(Long menuId) {
        log.info("selectSysMenuById start, menuId:{}", menuId);
        if (Objects.isNull(menuId)) {
            log.error("selectMenuById menuId is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        SysMenu sysMenu;
        try {
            sysMenu = menuMapper.selectByPrimaryKey(menuId);
        } catch (Exception e) {
            log.error("selectMenuById err, menuId:{}", menuId, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse(sysMenu);
    }

    /**
     * 新增菜单
     *
     * @param menu
     *            菜单信息
     * @return 结果
     */
    @Override
    public Object insertOrUpdateSysMenu(SysMenu menu) {
        log.info("insertSysMenu start, menu={}", menu.toString());
        if (StringUtils.isAnyBlank(menu.getMenuName(), menu.getMenuType(), menu.getPath())
            || Objects.isNull(menu.getAppId()) || Objects.isNull(menu.getParentId())
            || Objects.isNull(menu.getOrderNum())) {
            log.error("insertOrUpdateMenu::params invalid, menu={}", menu);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        try {
            if (Objects.isNull(menu.getId())) {
                // 新增菜单
                menu.setCreateUser("");
                menu.setCreateTime(System.currentTimeMillis());
                menuMapper.insertSelective(menu);
            } else {
                menu.setUpdateUser("");
                menu.setUpdateTime(System.currentTimeMillis());
                menuMapper.updateByPrimaryKeySelective(menu);
            }
        } catch (Exception e) {
            log.error("insertOrUpdateMenu err, menuVo={}", menu, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId
     *            菜单ID
     * @return 结果
     */
    @Override
    public Object deleteMenuById(Long menuId) {
        log.info("deleteMenuById start::menuId={}", menuId);
        if (Objects.isNull(menuId)) {
            log.error("deleteMenuById::param is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        try {
            // 如果此菜单还有子级菜单，不允许删除
            Integer result = menuMapper.hasChildByMenuId(menuId);
            if (result > Constants.ZERO_VALUE) {
                log.error("deleteMenuById delete not allowed");
                return BaseResponse.getFailedResponse(AuthManageErrConstant.DELETE_NOT_ALLOWED);
            }
            // 逻辑删除
            SysMenu deleteSysMenu = new SysMenu();
            deleteSysMenu.setId(menuId);
            deleteSysMenu.setDeleteFlag(Constants.BYTE_ONE_VALUE);
            deleteSysMenu.setUpdateUser("");
            deleteSysMenu.setUpdateTime(System.currentTimeMillis());
            menuMapper.updateByPrimaryKeySelective(deleteSysMenu);
        } catch (Exception e) {
            log.error("deleteMenuById err,menuId={}", menuId, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list
     *            分类表
     * @param parentId
     *            传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, Long parentId) {
        List<SysMenu> returnList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return returnList;
        }
        for (SysMenu t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (Objects.equals(t.getParentId(), parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> sysMenus = Lists.newArrayList();
        for (SysMenu n : list) {
            if (Objects.equals(n.getParentId(), t.getId())) {
                sysMenus.add(n);
            }
        }
        return sysMenus;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }
}
