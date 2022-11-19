package com.byd.auth.manage.service.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.byd.auth.manage.common.constants.Constants;
import com.byd.auth.manage.common.exception.AuthManageErrConstant;
import com.byd.auth.manage.common.model.BaseResponse;
import com.byd.auth.manage.dao.entity.dao.SysApp;
import com.byd.auth.manage.dao.entity.dao.SysBase;
import com.byd.auth.manage.dao.entity.dao.SysRole;
import com.byd.auth.manage.dao.entity.dao.SysRoleMenu;
import com.byd.auth.manage.dao.entity.dto.RoleMenuDto;
import com.byd.auth.manage.dao.entity.vo.BaseAuthVo;
import com.byd.auth.manage.dao.entity.vo.SysRoleVo;
import com.byd.auth.manage.dao.mapper.SysRoleMapper;
import com.byd.auth.manage.dao.mapper.SysRoleMenuMapper;
import com.byd.auth.manage.service.service.ISysAppService;
import com.byd.auth.manage.service.service.ISysBaseService;
import com.byd.auth.manage.service.service.ISysRoleService;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 菜单 业务层处理
 *
 * @author baibin
 */
@Service
@Slf4j
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private ISysAppService sysAppService;

    @Autowired
    private ISysBaseService sysBaseService;

    /**
     * 根据角色id查询单个角色信息
     *
     * @param roleId 角色id
     * @return
     */
    @Override
    public Object getSysRoleById(Long roleId) {
        log.info("getSysRoleById start, roleId={}", roleId);
        if (Objects.isNull(roleId)) {
            log.error("getSysRoleById roleId is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleId);
        List<SysRoleVo> sysRoleVos = querySysRoleVos(Lists.newArrayList(sysRole));
        if (CollectionUtils.isEmpty(sysRoleVos)) {
            log.error("getSysRoleById result is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.RESPONSE_IS_NULL);
        }
        SysRoleVo sysRoleVo = sysRoleVos.get(Constants.ZERO_VALUE);
        return BaseResponse.getSuccessResponse(sysRoleVo);
    }

    @Override
    public Object getSysRoleList(String roleName) {
        log.info("getSysRoleList start, roleName={}", roleName);
        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag", Constants.BYTE_ZERO_VALUE);
        if (StringUtils.isNotBlank(roleName)) {
            criteria.andLike("roleName", "%" + roleName + "%");
        }
        example.orderBy("createTime").desc();
        List<SysRole> sysRoleList = sysRoleMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(sysRoleList)) {
            log.info("getSysRoleList::query sysRoleList is empty");
            return BaseResponse.getSuccessResponse();
        }
        List<SysRoleVo> sysRoleVos = querySysRoleVos(sysRoleList);
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
            || Objects.isNull(sysRoleVo.getSysApp()) || CollectionUtils.isEmpty(sysRoleVo.getSysBases())) {
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
                // 新增角色-菜单关联关系
                if (CollectionUtils.isNotEmpty(sysRoleVo.getSysMenus())) {
                    addRoleMenuList(sysRoleVo.getSysMenus(), roleId);
                }
            } else {
                // 编辑角色
                sysRole.setUpdateUser("");
                sysRole.setUpdateTime(System.currentTimeMillis());
                sysRoleMapper.updateByPrimaryKeySelective(sysRole);
                // 删除该角色和菜单的关联关系
                deleteRoleMenuMappingByRoleId(sysRoleVo.getId());
                // 再新增角色和菜单的关联关系
                if (CollectionUtils.isNotEmpty(sysRoleVo.getSysMenus())) {
                    addRoleMenuList(sysRoleVo.getSysMenus(), sysRoleVo.getId());
                }
            }
        } catch (Exception e) {
            log.error("insertOrUpdateRole err", e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
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
        sysRole.setDeleteFlag(Constants.INTEGER_ONE_VALUE);
        sysRole.setUpdateTime(System.currentTimeMillis());
        sysRole.setUpdateUser("");
        try {
            sysRoleMapper.updateByPrimaryKeySelective(sysRole);
            // 删除角色-菜单关联关系表
            deleteRoleMenuMappingByRoleId(roleId);
        } catch (Exception e) {
            log.error("deleteSysRoleById err, roleId={}", roleId, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    @Override
    public List<SysRole> selectSysRoleList(List<Long> roleIds) {
        List<SysRole> sysRoles;
        if (CollectionUtils.isEmpty(roleIds)) {
            sysRoles = sysRoleMapper.selectAll();
        } else {
            Example example = new Example(SysRole.class);
            example.createCriteria().andIn("id", roleIds);
            sysRoles = sysRoleMapper.selectByExample(example);
        }
        return sysRoles;
    }

    private SysRole convertParamsToSysRole(SysRoleVo sysRoleVo) {
        SysRole sysRole = new SysRole();
        sysRole.setId(sysRoleVo.getId());
        sysRole.setAppId(sysRoleVo.getAppId());
        sysRole.setBaseIdStr(sysRoleVo.getBaseIdStr());
        sysRole.setRoleName(sysRoleVo.getRoleName());
        sysRole.setRoleKey(sysRoleVo.getRoleKey());
        sysRole.setRoleSort(sysRoleVo.getRoleSort());
        sysRole.setDataScope(sysRoleVo.getDataScope());
        sysRole.setRemark(sysRoleVo.getRemark());
        if (Objects.nonNull(sysRoleVo.getSysApp())) {
            sysRole.setAppId(sysRoleVo.getSysApp().getId());
        }
        if (CollectionUtils.isNotEmpty(sysRoleVo.getSysBases())) {
            String baseIdStr = sysRoleVo.getSysBases().stream().map(v -> String.valueOf(v.getId()))
                .collect(Collectors.joining(Constants.COMMA));
            sysRole.setBaseIdStr(baseIdStr);
        }
        return sysRole;
    }

    private SysRoleVo convertParamsToSysRoleVo(SysRole sysRole) {
        SysRoleVo sysRoleVo = new SysRoleVo();
        sysRoleVo.setId(sysRole.getId());
        sysRoleVo.setAppId(sysRole.getAppId());
        sysRoleVo.setBaseIdStr(sysRole.getBaseIdStr());
        sysRoleVo.setRoleName(sysRole.getRoleName());
        sysRoleVo.setRoleKey(sysRole.getRoleKey());
        sysRoleVo.setRoleSort(sysRole.getRoleSort());
        sysRoleVo.setDataScope(sysRole.getDataScope());
        sysRoleVo.setRemark(sysRole.getRemark());
        return sysRoleVo;
    }

    private void addRoleMenuList(List<BaseAuthVo> sysMenus, Long roleId) {
        // 新增应用-基地-角色关联关系
        List<Long> menuIds = sysMenus.stream().map(BaseAuthVo::getId).collect(Collectors.toList());
        List<SysRoleMenu> roleMenuList = Lists.newArrayList();
        menuIds.forEach(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        });
        roleMenuMapper.insertList(roleMenuList);
    }

    private void deleteRoleMenuMappingByRoleId(Long roleId) {
        Example roleMenuExample = new Example(SysRoleMenu.class);
        roleMenuExample.createCriteria().andEqualTo("roleId", roleId);
        roleMenuMapper.deleteByExample(roleMenuExample);
    }

    private List<SysRoleVo> querySysRoleVos(List<SysRole> sysRoleList) {
        List<SysRoleVo> sysRoleVos = Lists.newArrayList();
        List<Long> roleIds = sysRoleList.stream().map(SysRole::getId).collect(Collectors.toList());
        Set<Long> appIds = sysRoleList.stream().map(SysRole::getAppId).collect(Collectors.toSet());
        Set<Long> baseIds = Sets.newHashSet();
        sysRoleList.forEach(sysRole -> {
            if (StringUtils.isNotBlank(sysRole.getBaseIdStr())) {
                baseIds.addAll(Arrays.stream(sysRole.getBaseIdStr().split(Constants.COMMA)).map(Long::parseLong)
                    .collect(Collectors.toSet()));
            }
        });
        // 根据appId集合查询应用名称
        List<SysApp> sysApps = sysAppService.selectSysAppList(Lists.newArrayList(appIds));
        List<SysBase> sysBases = sysBaseService.selectSysBaseList(Lists.newArrayList(baseIds));
        // 根据roleIds查询所有角色-菜单集合
        List<RoleMenuDto> roleMenuDtoList = roleMenuMapper.selectRoleMenuDtoListByRoleIds(roleIds);
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
        Map<Long, List<RoleMenuDto>> roleMenuMap = null;
        if (CollectionUtils.isNotEmpty(roleMenuDtoList)) {
            roleMenuMap = roleMenuDtoList.stream().collect(Collectors.groupingBy(RoleMenuDto::getRoleId));
        }
        Map<Long, String> finalAppIdNameMap = appIdNameMap;
        Map<Long, String> finalBaseIdNameMap = baseIdNameMap;
        Map<Long, List<RoleMenuDto>> finalRoleMenuMap = roleMenuMap;
        sysRoleList.forEach(sysRole -> {
            SysRoleVo roleVo = convertParamsToSysRoleVo(sysRole);
            if (MapUtils.isNotEmpty(finalAppIdNameMap)) {
                Long subAppId = sysRole.getAppId();
                BaseAuthVo sysAppAuth = new BaseAuthVo();
                String appName = finalAppIdNameMap.getOrDefault(subAppId, Constants.EMPTY_STR);
                sysAppAuth.setId(subAppId);
                sysAppAuth.setName(appName);
                roleVo.setSysApp(sysAppAuth);
            }
            if (MapUtils.isNotEmpty(finalBaseIdNameMap)) {
                List<Long> subBaseIds = Arrays.stream(sysRole.getBaseIdStr().split(Constants.COMMA))
                    .map(Long::parseLong).collect(Collectors.toList());
                List<BaseAuthVo> sysBaseAuths = Lists.newArrayList();
                for (Long baseId : subBaseIds) {
                    String baseName = finalBaseIdNameMap.getOrDefault(baseId, Constants.EMPTY_STR);
                    BaseAuthVo sysBaseAuth = new BaseAuthVo();
                    sysBaseAuth.setId(baseId);
                    sysBaseAuth.setName(baseName);
                    sysBaseAuths.add(sysBaseAuth);
                }
                roleVo.setSysBases(sysBaseAuths);
            }
            if (MapUtils.isNotEmpty(finalRoleMenuMap) && finalRoleMenuMap.containsKey(sysRole.getId())) {
                List<RoleMenuDto> subRoleMenus = finalRoleMenuMap.get(sysRole.getId());
                List<BaseAuthVo> sysMenuAuths = Lists.newArrayList();
                for (RoleMenuDto roleMenu : subRoleMenus) {
                    BaseAuthVo subMenuAuth = new BaseAuthVo();
                    subMenuAuth.setId(roleMenu.getMenuId());
                    subMenuAuth.setName(roleMenu.getMenuName());
                    sysMenuAuths.add(subMenuAuth);
                }
                roleVo.setSysMenus(sysMenuAuths);
            }
            sysRoleVos.add(roleVo);
        });
        return sysRoleVos;
    }

}
