package cn.coolcollege.fast.authService.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.coolcollege.fast.authService.ISysAppService;
import cn.coolcollege.fast.authService.ISysBaseService;
import cn.coolcollege.fast.authService.ISysRoleService;
import cn.coolcollege.fast.constants.BaseResponse;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.exception.AuthManageErrConstant;
import cn.coolcollege.fast.storage.entity.dao.SysApp;
import cn.coolcollege.fast.storage.entity.dao.SysAppBaseRole;
import cn.coolcollege.fast.storage.entity.dao.SysBase;
import cn.coolcollege.fast.storage.entity.dao.SysRole;
import cn.coolcollege.fast.storage.entity.dao.SysRoleMenu;
import cn.coolcollege.fast.storage.entity.vo.BaseAuthVo;
import cn.coolcollege.fast.storage.entity.vo.SysRoleVo;
import cn.coolcollege.fast.storage.mapper.SysAppBaseRoleMapper;
import cn.coolcollege.fast.storage.mapper.SysRoleMapper;
import cn.coolcollege.fast.storage.mapper.SysRoleMenuMapper;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 菜单 业务层处理
 *
 * @author ruoyi
 */
@Service
@Slf4j
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysAppBaseRoleMapper appBaseRoleMapper;

    @Autowired
    private ISysAppService sysAppService;

    @Autowired
    private ISysBaseService sysBaseService;

    @Override
    public Object getSysRoleList(String roleName) {
        log.info("getSysRoleList start, roleName={}", roleName);
        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag", Constants.BYTE_ZERO_VALUE);
        if (StringUtils.isNotBlank(roleName)) {
            criteria.andLike("roleName", roleName);
        }
        List<SysRoleVo> sysRoleVos = Lists.newArrayList();
        List<SysRole> sysRoleList = sysRoleMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(sysRoleList)) {
            log.info("getSysRoleList::query sysRoleList is empty");
            return BaseResponse.getSuccessResponse(sysRoleVos);
        }
        // 角色集合
        List<Long> roleIds = sysRoleList.stream().map(SysRole::getId).collect(Collectors.toList());
        // 查询角色对应的应用-基地对应关系
        List<SysAppBaseRole> appBaseRoleList = appBaseRoleMapper.selectAppBaseRoleList(roleIds);
        
        Map<Long, List<SysAppBaseRole>> appBaseRoleMap =
            appBaseRoleList.stream().collect(Collectors.groupingBy(SysAppBaseRole::getRoleId));
        Set<Long> appIds = appBaseRoleList.stream().map(SysAppBaseRole::getAppId).collect(Collectors.toSet());
        Set<Long> baseIds = appBaseRoleList.stream().map(SysAppBaseRole::getBaseId).collect(Collectors.toSet());
        // 根据appId集合查询应用名称
        List<SysApp> sysApps = sysAppService.selectSysAppList(Lists.newArrayList(appIds));
        List<SysBase> sysBases = sysBaseService.selectSysBaseList(Lists.newArrayList(baseIds));
        Map<Long, String> appIdNameMap = null;
        if (CollectionUtils.isNotEmpty(sysApps)) {
            appIdNameMap =
                sysApps.stream().collect(Collectors.toMap(SysApp::getId, SysApp::getAppName, (k1, k2) -> k1));
        }
        Map<Long, String> baseIdNameMap = null;
        if (CollectionUtils.isNotEmpty(sysBases)) {
            baseIdNameMap =
                sysBases.stream().collect(Collectors.toMap(SysBase::getId, SysBase::getBaseName, (k1, k2) -> k1));
        }
        Map<Long, String> finalAppIdNameMap = appIdNameMap;
        Map<Long, String> finalBaseIdNameMap = baseIdNameMap;
        sysRoleList.forEach(sysRole -> {
            SysRoleVo roleVo = convertParamsToSysRoleVo(sysRole);
            Long roleId = sysRole.getId();

            List<SysAppBaseRole> appBaseRoles = appBaseRoleMap.get(roleId);
            Long appId = null;
            BaseAuthVo sysAppAuth = new BaseAuthVo();
            List<BaseAuthVo> sysBaseAuths = Lists.newArrayList();
            for (SysAppBaseRole appBaseRole : appBaseRoles) {
                appId = appBaseRoles.get(0).getAppId();
                Long baseId = appBaseRole.getBaseId();
                String baseName = finalBaseIdNameMap.get(baseId);
                BaseAuthVo sysBaseAuth = new BaseAuthVo();
                sysBaseAuth.setId(baseId);
                sysBaseAuth.setName(baseName);
                sysBaseAuths.add(sysBaseAuth);
            }
            String appName = finalAppIdNameMap.get(appId);
            sysAppAuth.setId(appId);
            sysAppAuth.setName(appName);
            roleVo.setSysApp(sysAppAuth);
            roleVo.setSysBaseList(sysBaseAuths);
            sysRoleVos.add(roleVo);
        });
        return BaseResponse.getSuccessResponse(sysRoleVos);
    }

    /**
     * 增加｜修改角色
     *
     * @param sysRoleVo
     *            角色vo
     * @return
     */
    @Override
    public Object insertOrUpdateRole(SysRoleVo sysRoleVo) {
        String sysRoleVoStr = JSON.toJSONString(sysRoleVo);
        log.error("insertOrUpdateRole start, sysRoleVo={}", sysRoleVoStr);
        if (Objects.isNull(sysRoleVo) || StringUtils.isBlank(sysRoleVo.getRoleName())
            || Objects.isNull(sysRoleVo.getSysApp()) || CollectionUtils.isEmpty(sysRoleVo.getSysBaseList())) {
            log.error("insertOrUpdateRole params invalid, sysRoleVo={}", sysRoleVo);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        try {
            SysRole sysRole = convertParamsToSysRole(sysRoleVo);
            if (Objects.isNull(sysRoleVo.getId())) {
                // 新增角色
                sysRole.setCreateUser("");
                sysRole.setCreateTime(System.currentTimeMillis());
                sysRoleMapper.insertSysRole(sysRole);
                Long roleId = sysRole.getId();
                if (Objects.isNull(roleId)) {
                    log.error("insertOrUpdateRole::roleId error return");
                    throw new IllegalArgumentException();
                }
                // 新增应用-基地-角色关联关系
                addAppBaseRoleList(sysRoleVo.getSysApp().getId(), sysRoleVo.getSysBaseList(), roleId);
                // 新增角色-关联关系
                if (CollectionUtils.isNotEmpty(sysRoleVo.getMenuIds())) {
                    addRoleMenuList(sysRoleVo.getMenuIds(), roleId);
                }
            } else {
                // 编辑角色
                sysRole.setUpdateUser("");
                sysRole.setUpdateTime(System.currentTimeMillis());
                sysRoleMapper.updateByPrimaryKeySelective(sysRole);
                // 删除该角色关联的应用-基地关系
                deleteAppBaseRoleMappingByRoleId(sysRoleVo.getId());
                // 再新增该角色关联的应用-基地关系
                addAppBaseRoleList(sysRoleVo.getSysApp().getId(), sysRoleVo.getSysBaseList(), sysRoleVo.getId());
                // 删除该角色和菜单的关联关系
                deleteRoleMenuMappingByRoleId(sysRoleVo.getId());
                // 再新增角色和菜单的关联关系
                if (CollectionUtils.isNotEmpty(sysRoleVo.getMenuIds())) {
                    addRoleMenuList(sysRoleVo.getMenuIds(), sysRoleVo.getId());
                }
            }
        } catch (Exception e) {
            log.error("");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return null;
    }

    @Override
    public Object deleteSysRoleById(Long roleId) {
        log.info("deleteSysRoleById start, roleId:{}", roleId);
        if (Objects.isNull(roleId)) {
            log.error("deleteSysRoleById roleId is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        // 逻辑删除角色
        SysRole sysRole = new SysRole();
        sysRole.setId(roleId);
        sysRole.setDeleteFlag(Constants.BYTE_ONE_VALUE);
        sysRole.setUpdateTime(System.currentTimeMillis());
        sysRole.setUpdateUser("");
        try {
            sysRoleMapper.updateByPrimaryKeySelective(sysRole);
            // 删除应用-基地-角色关联关系
            deleteAppBaseRoleMappingByRoleId(roleId);
            // 删除角色-菜单关联关系表
            deleteRoleMenuMappingByRoleId(roleId);
        } catch (Exception e) {
            log.error("deleteSysRoleById err, roleId={}", roleId, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    private SysRole convertParamsToSysRole(SysRoleVo sysRoleVo) {
        SysRole sysRole = new SysRole();
        sysRole.setId(sysRoleVo.getId());
        sysRole.setRoleName(sysRoleVo.getRoleName());
        sysRole.setRoleKey(sysRoleVo.getRoleKey());
        sysRole.setRoleSort(sysRoleVo.getRoleSort());
        sysRole.setDataScope(sysRoleVo.getDataScope());
        sysRole.setDeptCheckStrictly(sysRoleVo.isDeptCheckStrictly());
        sysRole.setMenuCheckStrictly(sysRoleVo.isMenuCheckStrictly());
        sysRole.setStatus(sysRoleVo.getStatus());
        sysRole.setFlag(sysRoleVo.isFlag());
        sysRole.setMenuIds(sysRoleVo.getMenuIds());
        sysRole.setDeptIds(sysRoleVo.getDeptIds());
        sysRole.setPermissions(sysRoleVo.getPermissions());
        sysRole.setRemark(sysRoleVo.getRemark());
        return sysRole;
    }

    private SysRoleVo convertParamsToSysRoleVo(SysRole sysRole) {
        SysRoleVo sysRoleVo = new SysRoleVo();
        sysRoleVo.setId(sysRole.getId());
        sysRoleVo.setRoleName(sysRole.getRoleName());
        sysRoleVo.setRoleKey(sysRole.getRoleKey());
        sysRoleVo.setRoleSort(sysRole.getRoleSort());
        sysRoleVo.setDataScope(sysRole.getDataScope());
        sysRoleVo.setDeptCheckStrictly(sysRole.isDeptCheckStrictly());
        sysRoleVo.setMenuCheckStrictly(sysRole.isMenuCheckStrictly());
        sysRoleVo.setStatus(sysRole.getStatus());
        sysRoleVo.setFlag(sysRole.isFlag());
        sysRoleVo.setMenuIds(sysRole.getMenuIds());
        sysRoleVo.setDeptIds(sysRole.getDeptIds());
        sysRoleVo.setPermissions(sysRole.getPermissions());
        sysRoleVo.setRemark(sysRole.getRemark());
        return sysRoleVo;
    }

    private void addAppBaseRoleList(Long appId, List<BaseAuthVo> baseAuthVoList, Long roleId) {
        // 新增应用-基地-角色关联关系
        List<SysAppBaseRole> appBaseRoleList = Lists.newArrayList();
        baseAuthVoList.forEach(v -> {
            SysAppBaseRole appBaseRole = new SysAppBaseRole();
            appBaseRole.setAppId(appId);
            appBaseRole.setBaseId(v.getId());
            appBaseRole.setRoleId(roleId);
            appBaseRoleList.add(appBaseRole);
        });
    }

    private void addRoleMenuList(List<Long> menuIds, Long roleId) {
        // 新增应用-基地-角色关联关系
        List<SysRoleMenu> roleMenuList = Lists.newArrayList();
        menuIds.forEach(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        });
        roleMenuMapper.insertList(roleMenuList);
    }

    private void deleteAppBaseRoleMappingByRoleId(Long roleId) {
        Example appBaseRoleExample = new Example(SysAppBaseRole.class);
        appBaseRoleExample.createCriteria().andEqualTo("roleId", roleId);
        appBaseRoleMapper.deleteByExample(appBaseRoleExample);
    }

    private void deleteRoleMenuMappingByRoleId(Long roleId) {
        Example roleMenuExample = new Example(SysRoleMenu.class);
        roleMenuExample.createCriteria().andEqualTo("roleId", roleId);
        roleMenuMapper.deleteByExample(roleMenuExample);
    }
}
