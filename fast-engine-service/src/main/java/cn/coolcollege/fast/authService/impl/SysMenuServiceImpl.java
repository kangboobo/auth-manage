package cn.coolcollege.fast.authService.impl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.coolcollege.fast.authService.ISysMenuService;
import cn.coolcollege.fast.constants.BaseResponse;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.exception.AuthManageErrConstant;
import cn.coolcollege.fast.storage.entity.dao.SysMenu;
import cn.coolcollege.fast.storage.mapper.SysMenuMapper;
import cn.coolcollege.fast.storage.mapper.SysRoleMapper;
import cn.coolcollege.fast.storage.mapper.SysRoleMenuMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 菜单 业务层处理
 *
 * @author ruoyi
 */
@Service
@Slf4j
public class SysMenuServiceImpl implements ISysMenuService {

    // public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    // /**
    // * 根据用户查询系统菜单列表
    // *
    // * @param userId
    // * 用户ID
    // * @return 菜单列表
    // */
    // @Override
    // public List<SysMenu> selectMenuList(Long userId) {
    // return selectMenuList(new SysMenu(), userId);
    // }

    // /**
    // * 查询系统菜单列表
    // *
    // * @param menu
    // * 菜单信息
    // * @return 菜单列表
    // */
    // @Override
    // public List<SysMenu> selectMenuList(SysMenu menu, Long userId) {
    // List<SysMenu> menuList;
    // // 管理员显示所有菜单信息
    // if (SysUser.isAdmin(userId)) {
    // menuList = menuMapper.selectMenuList(menu);
    // } else {
    // menu.getParams().put("userId", userId);
    // menuList = menuMapper.selectMenuListByUserId(menu);
    // }
    // return menuList;
    // }

    //
    // /**
    // * 根据用户ID查询权限
    // *
    // * @param userId 用户ID
    // * @return 权限列表
    // */
    // @Override
    // public Set<String> selectMenuPermsByUserId(Long userId)
    // {
    // List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
    // Set<String> permsSet = new HashSet<>();
    // for (String perm : perms)
    // {
    // if (StringUtils.isNotEmpty(perm))
    // {
    // permsSet.addAll(Arrays.asList(perm.trim().split(",")));
    // }
    // }
    // return permsSet;
    // }
    //
    // /**
    // * 根据角色ID查询权限
    // *
    // * @param roleId
    // * 角色ID
    // * @return 权限列表
    // */
    // @Override
    // public Set<String> selectMenuPermsByRoleId(Long roleId) {
    // List<String> perms = menuMapper.selectMenuPermsByRoleId(roleId);
    // Set<String> permsSet = new HashSet<>();
    // for (String perm : perms) {
    // if (StringUtils.isNotEmpty(perm)) {
    // permsSet.addAll(Arrays.asList(perm.trim().split(",")));
    // }
    // }
    // return permsSet;
    // }
    //
    // /**
    // * 根据用户ID查询菜单
    // *
    // * @param userId
    // * 用户名称
    // * @return 菜单列表
    // */
    // @Override
    // public List<SysMenu> selectMenuTreeByUserId(Long userId) {
    // List<SysMenu> menus = null;
    // // if (SecurityUtils.isAdmin(userId)) {
    // if (SysUser.isAdmin(userId)) {
    // menus = menuMapper.selectMenuTreeAll();
    // } else {
    // menus = menuMapper.selectMenuTreeByUserId(userId);
    // }
    // return getChildPerms(menus, 0L);
    // }

    // /**
    // * 根据角色ID查询菜单树信息
    // *
    // * @param roleId
    // * 角色ID
    // * @return 选中菜单列表
    // */
    // @Override
    // public List<Long> selectMenuListByRoleId(Long roleId) {
    // SysRole role = roleMapper.selectByPrimaryKey(roleId);
    // return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    // }
    //
    // /**
    // * 构建前端路由所需要的菜单
    // *
    // * @param menus
    // * 菜单列表
    // * @return 路由列表
    // */
    // @Override
    // public List<RouterVo> buildMenus(List<SysMenu> menus) {
    // List<RouterVo> routers = new LinkedList<RouterVo>();
    // for (SysMenu menu : menus) {
    // RouterVo router = new RouterVo();
    // router.setHidden("1".equals(menu.getVisible()));
    // router.setName(getRouteName(menu));
    // router.setPath(getRouterPath(menu));
    // router.setComponent(getComponent(menu));
    // router.setQuery(menu.getQuery());
    // router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()),
    // menu.getPath()));
    // List<SysMenu> cMenus = menu.getChildren();
    // if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
    // router.setAlwaysShow(true);
    // router.setRedirect("noRedirect");
    // router.setChildren(buildMenus(cMenus));
    // } else if (isMenuFrame(menu)) {
    // router.setMeta(null);
    // List<RouterVo> childrenList = new ArrayList<RouterVo>();
    // RouterVo children = new RouterVo();
    // children.setPath(menu.getPath());
    // children.setComponent(menu.getComponent());
    // children.setName(StringUtils.capitalize(menu.getPath()));
    // children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(),
    // StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
    // children.setQuery(menu.getQuery());
    // childrenList.add(children);
    // router.setChildren(childrenList);
    // } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
    // router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
    // router.setPath("/");
    // List<RouterVo> childrenList = new ArrayList<RouterVo>();
    // RouterVo children = new RouterVo();
    // String routerPath = innerLinkReplaceEach(menu.getPath());
    // children.setPath(routerPath);
    // children.setComponent(UserConstants.INNER_LINK);
    // children.setName(StringUtils.capitalize(routerPath));
    // children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
    // childrenList.add(children);
    // router.setChildren(childrenList);
    // }
    // routers.add(router);
    // }
    // return routers;
    // }

    // /**
    // * 构建前端所需要树结构
    // *
    // * @param menus
    // * 菜单列表
    // * @return 树结构列表
    // */
    // @Override
    // public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
    // List<SysMenu> returnList = Lists.newArrayList();
    // List<Long> tempList = Lists.newArrayList();
    // for (SysMenu menu : menus) {
    // tempList.add(menu.getId());
    // }
    // for (SysMenu menu : menus) {
    // // 如果是顶级节点, 遍历该父节点的所有子节点
    // if (!tempList.contains(menu.getParentId())) {
    // recursionFn(menus, menu);
    // returnList.add(menu);
    // }
    // }
    // if (returnList.isEmpty()) {
    // returnList = menus;
    // }
    // return returnList;
    // }

    // /**
    // * 构建前端所需要下拉树结构
    // *
    // * @param menus
    // * 菜单列表
    // * @return 下拉树结构列表
    // */
    // @Override
    // public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
    // List<SysMenu> menuTrees = buildMenuTree(menus);
    // return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    // }

    // /**
    // * 是否存在菜单子节点
    // *
    // * @param menuId
    // * 菜单ID
    // * @return 结果
    // */
    // @Override
    // public boolean hasChildByMenuId(Integer menuId) {
    // return menuMapper.hasChildByMenuId(menuId) > 0;
    // }

    // /**
    // * 查询菜单使用数量
    // *
    // * @param menuId
    // * 菜单ID
    // * @return 结果
    // */
    // @Override
    // public boolean checkMenuExistRole(Long menuId) {
    // int result = roleMenuMapper.checkMenuExistRole(menuId);
    // return result > 0;
    // }

    /**
     * 根据应用ID查询菜单树信息
     *
     * @param appId
     *            应用id
     * @return 菜单树信息
     */
    @Override
    public Object selectMenuTreeByAppId(Long appId) {
        log.info("selectMenuTreeByAppId start, appId={}", appId);
        if (Objects.isNull(appId)) {
            log.error("selectMenuTreeByAppId appId is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        List<SysMenu> menus;
        try {
            menus = menuMapper.selectSysMenuTreeByAppId(appId);
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

    // /**
    // * 校验菜单名称是否唯一
    // *
    // * @param menu
    // * 菜单信息
    // * @return 结果
    // */
    // @Override
    // public Object checkMenuNameUnique(SysMenu menu) {
    // log.info("checkMenuNameUnique::menu={}", menu.toString());
    // Long menuId = Objects.isNull(menu.getId()) ? -1L : menu.getId();
    // SysMenu info;
    // try {
    // info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
    // } catch (Exception e) {
    // log.error("checkMenuNameUnique err, menu={}", menu, e);
    // return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
    // }
    // if (Objects.nonNull(info) && !Objects.equals(info.getId(), menuId)) {
    // return BaseResponse.getSuccessResponse();
    // }
    // return BaseResponse.getFailedResponse(AuthManageErrConstant.NAME_DUPLICATE);
    // }

    //
    // /**
    // * 获取路由名称
    // *
    // * @param menu 菜单信息
    // * @return 路由名称
    // */
    // public String getRouteName(SysMenu menu)
    // {
    // String routerName = StringUtils.capitalize(menu.getPath());
    // // 非外链并且是一级目录（类型为目录）
    // if (isMenuFrame(menu))
    // {
    // routerName = StringUtils.EMPTY;
    // }
    // return routerName;
    // }
    //
    // /**
    // * 获取路由地址
    // *
    // * @param menu 菜单信息
    // * @return 路由地址
    // */
    // public String getRouterPath(SysMenu menu)
    // {
    // String routerPath = menu.getPath();
    // // 内链打开外网方式
    // if (menu.getParentId().intValue() != 0 && isInnerLink(menu))
    // {
    // routerPath = innerLinkReplaceEach(routerPath);
    // }
    // // 非外链并且是一级目录（类型为目录）
    // if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
    // && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
    // {
    // routerPath = "/" + menu.getPath();
    // }
    // // 非外链并且是一级目录（类型为菜单）
    // else if (isMenuFrame(menu))
    // {
    // routerPath = "/";
    // }
    // return routerPath;
    // }
    //
    // /**
    // * 获取组件信息
    // *
    // * @param menu 菜单信息
    // * @return 组件信息
    // */
    // public String getComponent(SysMenu menu)
    // {
    // String component = UserConstants.LAYOUT;
    // if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu))
    // {
    // component = menu.getComponent();
    // }
    // else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu))
    // {
    // component = UserConstants.INNER_LINK;
    // }
    // else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
    // {
    // component = UserConstants.PARENT_VIEW;
    // }
    // return component;
    // }
    //
    // /**
    // * 是否为菜单内部跳转
    // *
    // * @param menu 菜单信息
    // * @return 结果
    // */
    // public boolean isMenuFrame(SysMenu menu)
    // {
    // return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
    // && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    // }
    //
    // /**
    // * 是否为内链组件
    // *
    // * @param menu 菜单信息
    // * @return 结果
    // */
    // public boolean isInnerLink(SysMenu menu)
    // {
    // return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    // }
    //
    // /**
    // * 是否为parent_view组件
    // *
    // * @param menu 菜单信息
    // * @return 结果
    // */
    // public boolean isParentView(SysMenu menu)
    // {
    // return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    // }
    //
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

    // /**
    // * 内链域名特殊字符替换
    // *
    // * @return
    // */
    // public String innerLinkReplaceEach(String path)
    // {
    // return StringUtils.replaceEach(path, new String[] { Constants.HTTP, Constants.HTTPS, Constants.WWW, "." },
    // new String[] { "", "", "", "/" });
    // }
}
