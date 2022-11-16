package cn.coolcollege.fast.authService.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cn.coolcollege.fast.authService.ISysAppService;
import cn.coolcollege.fast.authService.ISysBaseService;
import cn.coolcollege.fast.authService.ISysRoleService;
import cn.coolcollege.fast.authService.ISysUserGroupService;
import cn.coolcollege.fast.constants.BaseResponse;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.exception.AuthManageErrConstant;
import cn.coolcollege.fast.storage.entity.dao.SysApp;
import cn.coolcollege.fast.storage.entity.dao.SysBase;
import cn.coolcollege.fast.storage.entity.dao.SysRole;
import cn.coolcollege.fast.storage.entity.dao.SysUserGroup;
import cn.coolcollege.fast.storage.entity.dao.SysUserGroupRole;
import cn.coolcollege.fast.storage.entity.dao.SysUserGroupUser;
import cn.coolcollege.fast.storage.entity.vo.BaseAuthVo;
import cn.coolcollege.fast.storage.entity.vo.SysUserGroupUserVo;
import cn.coolcollege.fast.storage.entity.vo.SysUserGroupVo;
import cn.coolcollege.fast.storage.entity.vo.SysUserVo;
import cn.coolcollege.fast.storage.mapper.SysUserGroupMapper;
import cn.coolcollege.fast.storage.mapper.SysUserGroupRoleMapper;
import cn.coolcollege.fast.storage.mapper.SysUserGroupUserMapper;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 用户组业务层处理
 *
 * @author baibin
 */
@Service
@Slf4j
public class SysUserGroupServiceImpl implements ISysUserGroupService {

    @Autowired
    private SysUserGroupMapper sysUserGroupMapper;

    @Autowired
    private SysUserGroupRoleMapper userGroupRoleMapper;

    @Autowired
    private SysUserGroupUserMapper userGroupUserMapper;

    @Autowired
    private ISysAppService sysAppService;

    @Autowired
    private ISysBaseService sysBaseService;

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 根据用户组id查询单个用户组信息
     *
     * @param userGroupId
     *            用户组id
     * @return
     */
    @Override
    public Object getSysUserGroupById(Long userGroupId) {
        log.info("getSysUserGroupById start, userGroupId={}", userGroupId);
        if (Objects.isNull(userGroupId)) {
            log.error("getSysUserGroupById userGroupId is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        SysUserGroup sysUserGroup = sysUserGroupMapper.selectByPrimaryKey(userGroupId);
        List<SysUserGroupVo> userGroupVos = querySysRoleVos(Lists.newArrayList(sysUserGroup));
        SysUserGroupVo result = null;
        if (CollectionUtils.isNotEmpty(userGroupVos)) {
            result = userGroupVos.get(Constants.ZERO_VALUE);
        }
        return BaseResponse.getSuccessResponse(result);
    }

    /**
     * 分页查询用户组
     *
     * @param pageNumber
     * @param pageSize
     * @param userGroupName
     *            用户组名称
     * @return
     */
    @Override
    public Object getPageSysUserGroupList(Integer pageNumber, Integer pageSize, String userGroupName) {
        log.info("getPageSysUserGroupList start, pageNumber={}, pageSize={}, userGroupName={}", pageNumber, pageSize,
            userGroupName);
        if (pageNumber < Constants.INTEGER_ONE_VALUE || pageSize < Constants.INTEGER_ONE_VALUE) {
            log.error("getPageSysUserGroupList params invalid");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        Example example = new Example(SysUserGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag", Constants.BYTE_ZERO_VALUE);
        if (StringUtils.isNotBlank(userGroupName)) {
            criteria.andLike("userGroupName", userGroupName);
        }
        List<SysUserGroup> sysUserGroupList = sysUserGroupMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(sysUserGroupList)) {
            log.info("getPageSysUserGroupList::query sysUserGroupList is empty");
            return BaseResponse.getSuccessResponse();
        }
        List<SysUserGroupVo> sysUserGroupVos = querySysRoleVos(sysUserGroupList);
        PageHelper.startPage(pageNumber, pageSize);
        return BaseResponse.getSuccessResponse(new PageInfo<>(sysUserGroupVos));
    }

    /**
     * 增加｜修改用户组
     *
     * @param sysUserGroupVo
     *            用户组vo
     * @return
     */
    @Override
    public Object insertOrUpdateUserGroup(SysUserGroupVo sysUserGroupVo) {
        log.info("insertOrUpdateUserGroup start, sysUserGroupVo={}", JSON.toJSONString(sysUserGroupVo));
        if (Objects.isNull(sysUserGroupVo) || StringUtils.isBlank(sysUserGroupVo.getUserGroupName())
            || Objects.isNull(sysUserGroupVo.getSysApp()) || Objects.isNull(sysUserGroupVo.getSysBases())) {
            log.error("insertOrUpdateUserGroup params invalid");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        try {
            SysUserGroup sysUserGroup = convertParamsToSysUserGroup(sysUserGroupVo);
            if (Objects.isNull(sysUserGroupVo.getId())) {
                // 新增用户组
                sysUserGroup.setCreateUser("");
                sysUserGroup.setCreateTime(System.currentTimeMillis());
                sysUserGroupMapper.insertSysUserGroup(sysUserGroup);
                Long userGroupId = sysUserGroup.getId();
                if (Objects.isNull(userGroupId)) {
                    log.error("insertOrUpdateUserGroup::userGroupId error return");
                    throw new IllegalArgumentException();
                }
                // 新增用户组-角色关联关系
                if (CollectionUtils.isNotEmpty(sysUserGroupVo.getSysRoles())) {
                    List<Long> roleIds =
                        sysUserGroupVo.getSysRoles().stream().map(BaseAuthVo::getId).collect(Collectors.toList());
                    addUserGroupRoleList(roleIds, userGroupId);
                }
            } else {
                // 编辑角色
                sysUserGroup.setUpdateUser("");
                sysUserGroup.setUpdateTime(System.currentTimeMillis());
                sysUserGroupMapper.updateByPrimaryKeySelective(sysUserGroup);
                // 删除用户组和角色的关联关系
                deleteUserGroupRoleMappingByUserGroupId(sysUserGroupVo.getId());
                // 再新增用户组和角色关联关系
                if (CollectionUtils.isNotEmpty(sysUserGroupVo.getSysRoles())) {
                    List<Long> roleIds =
                        sysUserGroupVo.getSysRoles().stream().map(BaseAuthVo::getId).collect(Collectors.toList());
                    addUserGroupRoleList(roleIds, sysUserGroupVo.getId());
                }
            }
        } catch (Exception e) {
            log.error("");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 根据用户组id删除用户组
     *
     * @param userGroupId
     *            用户组id
     * @return
     */
    @Override
    public Object deleteSysUserGroupById(Long userGroupId) {
        log.info("deleteSysUserGroupById start, userGroupId={}", userGroupId);
        if (Objects.isNull(userGroupId)) {
            log.error("deleteSysUserGroupById userGroupId is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        // 逻辑删除角色
        SysUserGroup sysUserGroup = new SysUserGroup();
        sysUserGroup.setId(userGroupId);
        sysUserGroup.setDeleteFlag(Constants.BYTE_ONE_VALUE);
        sysUserGroup.setUpdateTime(System.currentTimeMillis());
        sysUserGroup.setUpdateUser("");
        try {
            sysUserGroupMapper.updateByPrimaryKeySelective(sysUserGroup);
            // 删除用户组-角色关联关系
            deleteUserGroupRoleMappingByUserGroupId(userGroupId);
            // 删除用户组-用户关联关系
            deleteUserGroupUserMappingByUserGroupId(userGroupId);
        } catch (Exception e) {
            log.error("deleteSysRoleById err, userGroupId={}", userGroupId, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 修改用户组和用户关联关系
     *
     * @param userGroupUserVo
     * @return
     */
    @Override
    public Object saveUserGroupUser(SysUserGroupUserVo userGroupUserVo) {
        log.info("updateUserGroupUser start, userGroupUserVo={}", JSON.toJSONString(userGroupUserVo));
        Long userGroupId = userGroupUserVo.getId();
        if (Objects.isNull(userGroupId)) {
            log.error("insertUserGroupUserMapping params invalid");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        try {
            // 删除该用户组和用户的关联关系
            Example example = new Example(SysUserGroupUser.class);
            example.createCriteria().andEqualTo("userGroupId", userGroupId);
            userGroupUserMapper.deleteByExample(example);
            // 新增用户组和用户关联关系
            if (CollectionUtils.isNotEmpty(userGroupUserVo.getSysUsers())) {
                List<SysUserGroupUser> addUserGroupUsers = Lists.newArrayList();
                for (SysUserVo userVo : userGroupUserVo.getSysUsers()) {
                    SysUserGroupUser userGroupUser = new SysUserGroupUser();
                    userGroupUser.setUserId(userVo.getUserId());
                    userGroupUser.setUserGroupId(userGroupUserVo.getId());
                    addUserGroupUsers.add(userGroupUser);
                }
                userGroupUserMapper.insertList(addUserGroupUsers);
            }
        } catch (Exception e) {
            log.error("insertUserGroupUserMapping err, userGroupVo={}", userGroupUserVo, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 根据用户组id查询对应的用户组和用户信息
     *
     * @param userGroupId
     * @return
     */
    @Override
    public Object getUserGroupUserById(Long userGroupId) {
        log.info("getUserGroupUserById start, userGroupId={}", userGroupId);
        if (Objects.isNull(userGroupId)) {
            log.error("getUserGroupUserById params invalid");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        // 根据userGroupId查询其对应的所有用户id
        List<SysUserGroupUser> userGroupUserList;
        SysUserGroupUserVo userGroupUserVo = null;
        try {
            Example example = new Example(SysUserGroupUser.class);
            example.createCriteria().andEqualTo("userGroupId", userGroupId);
            userGroupUserList = userGroupUserMapper.selectByExample(example);

            SysUserGroup sysUserGroup = sysUserGroupMapper.selectByPrimaryKey(userGroupId);
            userGroupUserVo = convertParamsToSysUserGroupUserVo(sysUserGroup);
        } catch (Exception e) {
            log.error("getUserGroupUserById err, userGroupId={}", userGroupId, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        if (CollectionUtils.isNotEmpty(userGroupUserList)) {
            List<String> userIds =
                userGroupUserList.stream().map(SysUserGroupUser::getUserId).collect(Collectors.toList());
            // 根据userIds查询到全部用户姓名
            Map<String, String> userIdNameMap = getSysUserIdNameMap(userIds);
            List<SysUserVo> sysUserVos = Lists.newArrayList();
            if (MapUtils.isNotEmpty(userIdNameMap)) {
                for (Map.Entry<String, String> entry : userIdNameMap.entrySet()) {
                    SysUserVo sysUserVo = new SysUserVo();
                    sysUserVo.setUserId(entry.getKey());
                    sysUserVo.setUserName(entry.getValue());
                    sysUserVos.add(sysUserVo);
                }
                userGroupUserVo.setSysUsers(sysUserVos);
            }
        }
        return BaseResponse.getSuccessResponse(userGroupUserVo);
    }

    private SysUserGroup convertParamsToSysUserGroup(SysUserGroupVo userGroupVo) {
        SysUserGroup userGroup = new SysUserGroup();
        userGroup.setId(userGroupVo.getId());
        userGroup.setUserGroupName(userGroupVo.getUserGroupName());
        userGroup.setRemark(userGroupVo.getRemark());
        if (Objects.nonNull(userGroupVo.getSysApp())) {
            userGroup.setAppId(userGroupVo.getSysApp().getId());
        }
        if (CollectionUtils.isNotEmpty(userGroupVo.getSysBases())) {
            String baseIdStr = userGroupVo.getSysBases().stream().map(v -> String.valueOf(v.getId()))
                .collect(Collectors.joining(Constants.COMMA));
            userGroup.setBaseIdStr(baseIdStr);
        }
        return userGroup;
    }

    private void addUserGroupRoleList(List<Long> roleIds, Long userGroupId) {
        // 新增应用-基地-角色关联关系
        List<SysUserGroupRole> userGroupRoleList = Lists.newArrayList();
        roleIds.forEach(roleId -> {
            SysUserGroupRole userGroupRole = new SysUserGroupRole();
            userGroupRole.setUserGroupId(userGroupId);
            userGroupRole.setRoleId(roleId);
            userGroupRoleList.add(userGroupRole);
        });
        userGroupRoleMapper.insertList(userGroupRoleList);
    }

    private void addUserGroupUserList(List<String> userIds, Long userGroupId) {
        // 新增应用-基地-角色关联关系
        List<SysUserGroupUser> userGroupUserList = Lists.newArrayList();
        userIds.forEach(userId -> {
            SysUserGroupUser userGroupUser = new SysUserGroupUser();
            userGroupUser.setUserGroupId(userGroupId);
            userGroupUser.setUserId(userId);
            userGroupUserList.add(userGroupUser);
        });
        userGroupUserMapper.insertList(userGroupUserList);
    }

    /**
     * 根据用户组id删除用户组和角色关联关系
     * 
     * @param userGroupId
     *            用户组id
     */
    private void deleteUserGroupRoleMappingByUserGroupId(Long userGroupId) {
        Example example = new Example(SysUserGroupRole.class);
        example.createCriteria().andEqualTo("userGroupId", userGroupId);
        userGroupRoleMapper.deleteByExample(example);
    }

    /**
     * 根据用户组id删除用户组和用户关联关系
     *
     * @param userGroupId
     *            用户组id
     */
    private void deleteUserGroupUserMappingByUserGroupId(Long userGroupId) {
        Example example = new Example(SysUserGroupUser.class);
        example.createCriteria().andEqualTo("userGroupId", userGroupId);
        userGroupUserMapper.deleteByExample(example);
    }

    private List<SysUserGroupVo> querySysRoleVos(List<SysUserGroup> sysUserGroupList) {
        List<SysUserGroupVo> sysUserGroupVos = Lists.newArrayList();
        List<Long> userGroupIds = sysUserGroupList.stream().map(SysUserGroup::getId).collect(Collectors.toList());
        Set<Long> appIds = sysUserGroupList.stream().map(SysUserGroup::getAppId).collect(Collectors.toSet());
        Set<Long> baseIds = Sets.newHashSet();
        sysUserGroupList.forEach(sysUserGroup -> {
            if (StringUtils.isNotBlank(sysUserGroup.getBaseIdStr())
                && sysUserGroup.getBaseIdStr().contains(Constants.COMMA)) {
                baseIds.addAll(Arrays.stream(sysUserGroup.getBaseIdStr().split(Constants.COMMA)).map(Long::parseLong)
                    .collect(Collectors.toSet()));
            }
        });
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
        // 根据userGroupIds查询userGroupId和roleId对应关系
        List<SysUserGroupRole> userGroupRoles = userGroupRoleMapper.selectUserGroupRoles(userGroupIds);
        Map<Long, List<SysUserGroupRole>> userGroupRoleMap = null;
        Map<Long, String> roleIdNameMap = null;
        if (CollectionUtils.isNotEmpty(userGroupRoles)) {
            userGroupRoleMap = userGroupRoles.stream().collect(Collectors.groupingBy(SysUserGroupRole::getUserGroupId));
            Set<Long> roleIds = userGroupRoles.stream().map(SysUserGroupRole::getRoleId).collect(Collectors.toSet());
            List<SysRole> sysRoleList = sysRoleService.selectSysRoleList(Lists.newArrayList(roleIds));
            roleIdNameMap =
                sysRoleList.stream().collect(Collectors.toMap(SysRole::getId, SysRole::getRoleName, (k1, k2) -> k1));
        }
        Map<Long, String> finalAppIdNameMap = appIdNameMap;
        Map<Long, String> finalBaseIdNameMap = baseIdNameMap;
        Map<Long, List<SysUserGroupRole>> finalUserGroupRoleMap = userGroupRoleMap;
        Map<Long, String> finalRoleIdNameMap = roleIdNameMap;
        sysUserGroupList.forEach(userGroup -> {
            SysUserGroupVo userGroupVo = convertParamsToSysUserGroupVo(userGroup);
            if (MapUtils.isNotEmpty(finalAppIdNameMap)) {
                Long subAppId = userGroup.getAppId();
                BaseAuthVo sysAppAuth = new BaseAuthVo();
                sysAppAuth.setId(subAppId);
                sysAppAuth.setName(finalAppIdNameMap.getOrDefault(subAppId, Constants.EMPTY_STR));
                userGroupVo.setSysApp(sysAppAuth);
            }
            if (MapUtils.isNotEmpty(finalBaseIdNameMap)) {
                List<Long> subBaseIds = Arrays.stream(userGroup.getBaseIdStr().split(Constants.COMMA))
                    .map(Long::parseLong).collect(Collectors.toList());
                List<BaseAuthVo> sysBaseAuths = Lists.newArrayList();
                for (Long baseId : subBaseIds) {
                    BaseAuthVo sysBaseAuth = new BaseAuthVo();
                    sysBaseAuth.setId(baseId);
                    sysBaseAuth.setName(finalBaseIdNameMap.getOrDefault(baseId, Constants.EMPTY_STR));
                    sysBaseAuths.add(sysBaseAuth);
                }
                userGroupVo.setSysBases(sysBaseAuths);
            }
            if (MapUtils.isNotEmpty(finalUserGroupRoleMap) && finalUserGroupRoleMap.containsKey(userGroup.getId())) {
                List<SysUserGroupRole> subUserGroupRoles = finalUserGroupRoleMap.get(userGroup.getId());
                List<BaseAuthVo> roleAuths = Lists.newArrayList();
                subUserGroupRoles.forEach(subRole -> {
                    BaseAuthVo roleAuth = new BaseAuthVo();
                    roleAuth.setId(subRole.getRoleId());
                    roleAuth.setName(finalRoleIdNameMap.getOrDefault(subRole.getRoleId(), Constants.EMPTY_STR));
                    roleAuths.add(roleAuth);
                });
                userGroupVo.setSysRoles(roleAuths);
            }
            sysUserGroupVos.add(userGroupVo);
        });
        return sysUserGroupVos;
    }

    private SysUserGroupVo convertParamsToSysUserGroupVo(SysUserGroup userGroup) {
        SysUserGroupVo userGroupVo = new SysUserGroupVo();
        userGroupVo.setId(userGroup.getId());
        userGroupVo.setAppId(userGroup.getAppId());
        userGroupVo.setBaseIdStr(userGroup.getBaseIdStr());
        userGroupVo.setUserGroupName(userGroup.getUserGroupName());
        userGroupVo.setRemark(userGroup.getRemark());
        userGroupVo.setCreateTime(userGroup.getCreateTime());
        userGroupVo.setUpdateTime(userGroup.getUpdateTime());
        return userGroupVo;
    }

    private SysUserGroupUserVo convertParamsToSysUserGroupUserVo(SysUserGroup userGroup) {
        SysUserGroupUserVo userGroupUserVo = new SysUserGroupUserVo();
        userGroupUserVo.setId(userGroup.getId());
        userGroupUserVo.setAppId(userGroup.getAppId());
        userGroupUserVo.setBaseIdStr(userGroup.getBaseIdStr());
        userGroupUserVo.setUserGroupName(userGroup.getUserGroupName());
        userGroupUserVo.setRemark(userGroup.getRemark());
        userGroupUserVo.setCreateTime(userGroup.getCreateTime());
        userGroupUserVo.setUpdateTime(userGroup.getUpdateTime());
        return userGroupUserVo;
    }

    private Map<String, String> getSysUserIdNameMap(List<String> userIds) {
        return Maps.newHashMap();
    }
}
