package cn.coolcollege.fast.service;

import cn.coolcollege.fast.constants.FastEngineOrgTypeEnum;
import cn.coolcollege.fast.constants.ResourceVisibleTypeEnum;
import cn.coolcollege.fast.entity.Resource;
import cn.coolcollege.fast.entity.ResourceAuthInfo;
import cn.coolcollege.fast.entity.ResourceClassifyRelationReverse;
import cn.coolcollege.fast.entity.UserResourceDo;
import cn.coolcollege.fast.entity.request.GetDirectResourceIdsByOrgIdsRequest;
import cn.coolcollege.fast.entity.request.GetPageResourceIdByCreateUserRequest;
import cn.coolcollege.fast.entity.request.GetResourceClassifyRelationRequest;
import cn.coolcollege.fast.entity.request.GetResourceDetailsByResourceIdsRequest;
import cn.coolcollege.fast.entity.result.GetDirectResourceIdsByOrgIdsResult;
import cn.coolcollege.fast.entity.result.GetPageBatchResourceInfoResult;
import cn.coolcollege.fast.entity.result.GetResourceClassifyRelationReverseListResult;
import cn.coolcollege.fast.entity.result.GetResourceDetailsByResourceIdsResult;
import cn.coolcollege.fast.model.UserOriginSupervisorInfo;
import cn.coolcollege.fast.storage.mapper.AppResourceMapper;
import cn.coolcollege.fast.util.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alipay.sofa.runtime.api.annotation.SofaReference;
import com.alipay.sofa.runtime.api.annotation.SofaReferenceBinding;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.coolcollege.authority.facade.IAuthorityRangeService;
import net.coolcollege.authority.facade.constants.AuthorityUniqueIdConstants;
import net.coolcollege.authority.facade.constants.RangeTypeEnum;
import net.coolcollege.authority.facade.model.request.BatchGetAuthorityRangeTypeRequest;
import net.coolcollege.authority.facade.model.result.BatchGetAuthorityRangeTypeResult;
import net.coolcollege.platform.util.constants.CommonConstants;
import net.coolcollege.platform.util.constants.SofaBindingTypeConstants;
import net.coolcollege.user.facade.IDepartmentService;
import net.coolcollege.user.facade.IPositionService;
import net.coolcollege.user.facade.IUserGroupService;
import net.coolcollege.user.facade.IUserService;
import net.coolcollege.user.facade.constants.QuerySupervisorDirectionEnum;
import net.coolcollege.user.facade.constants.UserUniqueIdConstants;
import net.coolcollege.user.facade.model.department.request.GetDepartmentRelationRequest;
import net.coolcollege.user.facade.model.department.result.GetDepartmentRelationReverseListResult;
import net.coolcollege.user.facade.model.position.request.GetPositionRelationRequest;
import net.coolcollege.user.facade.model.position.result.GetPositionRelationReverseListResult;
import net.coolcollege.user.facade.model.user.request.*;
import net.coolcollege.user.facade.model.user.result.*;
import net.coolcollege.user.facade.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author pk
 * @date 2021-06-24 14:06
 */
@Service
public class CalculateServiceHelper {

    private static final Logger logger = LoggerFactory.getLogger(CalculateServiceHelper.class);

    @SofaReference(uniqueId = UserUniqueIdConstants.USER_SERVICE_UNIQUE_ID, interfaceType = IUserService.class,
        binding = @SofaReferenceBinding(bindingType = SofaBindingTypeConstants.BOLT, timeout = 5000))
    private IUserService userService;

    @SofaReference(uniqueId = UserUniqueIdConstants.DEPARTMENT_SERVICE_UNIQUE_ID,
        interfaceType = IDepartmentService.class,
        binding = @SofaReferenceBinding(bindingType = SofaBindingTypeConstants.BOLT))
    private IDepartmentService departmentService;

    @SofaReference(uniqueId = UserUniqueIdConstants.POSITION_SERVICE_UNIQUE_ID, interfaceType = IPositionService.class,
        binding = @SofaReferenceBinding(bindingType = SofaBindingTypeConstants.BOLT))
    private IPositionService positionService;

    @SofaReference(uniqueId = UserUniqueIdConstants.USER_GROUP_SERVICE_UNIQUE_ID,
        interfaceType = IUserGroupService.class,
        binding = @SofaReferenceBinding(bindingType = SofaBindingTypeConstants.BOLT))
    private IUserGroupService groupService;

    @SofaReference(uniqueId = AuthorityUniqueIdConstants.AUTHORITY_RANGE_SERVICE_UNIQUE_ID,
        interfaceType = IAuthorityRangeService.class,
        binding = @SofaReferenceBinding(bindingType = SofaBindingTypeConstants.BOLT))
    private IAuthorityRangeService authorityRangeService;

    @Autowired
    private ResourceServiceProxy resourceServiceProxy;

    @Autowired
    private AppResourceMapper appResourceMapper;

    /**
     * 查询资源的详情
     *
     * @param appId
     * @param eid
     * @param domainId
     * @param resourceType
     * @return
     */
    public List<Resource> queryResourceDetails(String appId, Long eid, String domainId, String resourceType,
        List<String> resourceIds) {
        GetResourceDetailsByResourceIdsRequest request = new GetResourceDetailsByResourceIdsRequest();
        request.setAppId(appId);
        request.setEnterpriseId(eid);
        request.setDomainId(domainId);
        request.setResourceType(resourceType);
        request.setResourceIds(resourceIds);

        GetResourceDetailsByResourceIdsResult result;
        try {

            result = resourceServiceProxy.getResourceDetailsByResourceIds(request);
            if (!result.getSuccess()) {
                logger.error(
                    "getResourceDetailsByResourceIds err, appId={}, eid={}, domainId={}, resourceType={}, resourceIds={}, resultCode={}",
                    appId, eid, domainId, resourceType, resourceIds, result.getResultCode());
                return CommonConstants.EMPTY_LIST;
            }
        } catch (Exception e) {
            result = new GetResourceDetailsByResourceIdsResult();
            logger.error("getResourceDetailsByResourceIds err, request={}", request, e);
        }
        return result.getResourceList();
    }

    /**
     * 资源详情构造出es对象
     *
     * @param appId
     * @param eid
     * @param resourceType
     * @param resourceList
     * @return
     */
    public List<UserResourceDo> buildUserResourceDoList(String appId, Long eid, String resourceType,
        List<Resource> resourceList) {
        if (CollectionUtils.isEmpty(resourceList)) {
            return CommonConstants.EMPTY_LIST;
        }

        Set<Long> classifyIdSet = Sets.newHashSet();
        Set<String> createUserIdSet = Sets.newHashSet();
        Set<Long> deptIdSet = Sets.newHashSet();
        Set<Long> positionIdSet = Sets.newHashSet();
        Set<Long> groupIdSet = Sets.newHashSet();
        resourceList.forEach(resource -> {
            if (CollectionUtils.isNotEmpty(resource.getClassifyIds())) {
                classifyIdSet.addAll(resource.getClassifyIds());
            }
            if (resource.getCreateUserId() != null) {
                createUserIdSet.add(resource.getCreateUserId());
            }
            mergeOrgIds(resource, deptIdSet, positionIdSet, groupIdSet);
        });

        // 涉及到的所有orgId和对应userId的映射
        AuthInfoUserMap authInfoUserMap = buildAuthInfoUserMap(appId, eid, deptIdSet, groupIdSet, positionIdSet);

        List<String> createUserIds = Lists.newArrayList(createUserIdSet);
        // 人员和管辖他的所有人以及所有上级领导的映射
        Map<String, List<String>> authoritySupervisorMap = queryUserAuthoritySupervisorMap(appId, eid, createUserIds);

        List<Long> classifyIds = Lists.newArrayList(classifyIdSet);
        logger.info("queryClassifyIdParentIdMap start, appId={}, eid={}, resType={}, classifyIds={}", appId, eid,
            resourceType, classifyIds.stream().map(String::valueOf).collect(Collectors.joining(CommonConstants.COMMA)));
        // 分类id和所有父分类id的映射
        Map<Long, List<Long>> classifyIdParentIdMap = queryClassifyIdParentIdMap(appId, eid, resourceType, classifyIds);

        // 查询企业下所有管理员
        List<String> allAdminUserIds = getAllAdminUserIds(appId, eid);

        List<UserResourceDo> outList = new ArrayList<>();
        resourceList.forEach(resDetail -> {
            BuildUserResourceDoParam param = new BuildUserResourceDoParam();
            param.setAppId(appId);
            param.setEnterpriseId(eid);
            param.setResourceType(resourceType);
            param.setAuthInfoUserMap(authInfoUserMap);
            param.setResDetail(resDetail);
            param.setAuthoritySupervisorMap(authoritySupervisorMap);
            param.setClassifyIdParentIdMap(classifyIdParentIdMap);

            UserResourceDo userResourceDo = buildUserResourceDo(param, allAdminUserIds);
            if (userResourceDo != null) {
                outList.add(userResourceDo);
            }
        });
        return outList;
    }

    /**
     * 合并某个资源的各种权限范围(query, refer, edit)
     *
     * @param resource
     * @param deptIdSet
     * @param positionIdSet
     * @param groupIdSet
     */
    private void mergeOrgIds(Resource resource, Set<Long> deptIdSet, Set<Long> positionIdSet, Set<Long> groupIdSet) {
        if (resource == null || deptIdSet == null || positionIdSet == null || groupIdSet == null) {
            return;
        }
        ResourceAuthInfo queryAuthInfo = resource.getQueryAuthInfo();
        if (queryAuthInfo != null) {
            mergeOrgIdsForSingleAuthInfo(deptIdSet, positionIdSet, groupIdSet, queryAuthInfo);
        }

        ResourceAuthInfo referAuthInfo = resource.getReferAuthInfo();
        if (referAuthInfo != null) {
            mergeOrgIdsForSingleAuthInfo(deptIdSet, positionIdSet, groupIdSet, referAuthInfo);
        }

        ResourceAuthInfo editAuthInfo = resource.getEditAuthInfo();
        if (editAuthInfo != null) {
            mergeOrgIdsForSingleAuthInfo(deptIdSet, positionIdSet, groupIdSet, editAuthInfo);
        }
    }

    /**
     * 合并某个资源的单个权限范围
     *
     * @param positionIdSet
     * @param groupIdSet
     * @param resourceAuthInfo
     */
    private void mergeOrgIdsForSingleAuthInfo(Set<Long> deptIdSet, Set<Long> positionIdSet, Set<Long> groupIdSet,
        ResourceAuthInfo resourceAuthInfo) {
        if (CollectionUtils.isNotEmpty(resourceAuthInfo.getDepartmentIds())) {
            deptIdSet.addAll(resourceAuthInfo.getDepartmentIds());
        }

        if (CollectionUtils.isNotEmpty(resourceAuthInfo.getUserGroupIds())) {
            groupIdSet.addAll(resourceAuthInfo.getUserGroupIds());
        }

        if (CollectionUtils.isNotEmpty(resourceAuthInfo.getPositionIds())) {
            positionIdSet.addAll(resourceAuthInfo.getPositionIds());
        }
    }

    /**
     * 构造一批部门, 岗位, 用户组 下所有 orgId 和 userId 的对象
     *
     * @param appId
     * @param eid
     * @param deptIdSet
     * @param groupIdSet
     * @param positionIdSet
     * @return
     */
    private AuthInfoUserMap buildAuthInfoUserMap(String appId, Long eid, Set<Long> deptIdSet, Set<Long> groupIdSet,
        Set<Long> positionIdSet) {
        AuthInfoUserMap authInfoUserMap = new AuthInfoUserMap();

        Map<Long, Set<Long>> userIdsGroupByDeptIds = getUserIdsGroupByDeptIdsV2(appId, eid, deptIdSet);
        authInfoUserMap.setDeptUserIdMap(userIdsGroupByDeptIds);

        Map<Long, Set<Long>> userIdsGroupByGroupIds = getUserIdsGroupByGroupIds(appId, eid, groupIdSet);
        authInfoUserMap.setGroupUserIdMap(userIdsGroupByGroupIds);

        Map<Long, Set<Long>> userIdsGroupByPositionIds = getUserIdsGroupByPositionIdsV2(appId, eid, positionIdSet);
        authInfoUserMap.setPositionUserIdMap(userIdsGroupByPositionIds);

        return authInfoUserMap;
    }

    /**
     * 调用user-center获取某些部门下的所有人（包括子部门）
     *
     * @param appId
     * @param eid
     * @param deptIds
     * @return
     */
    public Map<Long, Set<Long>> getUserIdsGroupByDeptIds(String appId, Long eid, Set<Long> deptIds) {
        if (StringUtils.isBlank(appId) || eid == null || CollectionUtils.isEmpty(deptIds)) {
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        GetUserIdsByDeptIdsRequest deptIdsRequest = new GetUserIdsByDeptIdsRequest();
        deptIdsRequest.setEnterpriseId(eid);
        deptIdsRequest.setAppId(appId);
        deptIdsRequest.setDeptIds(Lists.newArrayList(deptIds));
        deptIdsRequest.setRecursiveSubDept(true);
        GetUserIdsGroupByDeptResult groupResult = userService.getUserIdsGroupByDeptId(deptIdsRequest);
        if (!groupResult.getSuccess()) {
            logger.error("getUserIdsGroupByDeptIds err, deptIds={}, appId={}, eid={}, errCode={}", deptIds, appId, eid,
                groupResult.getResultCode());
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        return DepartmentUserRelationConverter.convertRelations2Map(groupResult.getDepartmentUserRelations());
    }

    /**
     * 调用user-center获取某些部门下的所有人（包括子部门）
     *
     * @param appId
     * @param eid
     * @param deptIds
     * @return
     */
    public Map<Long, Set<Long>> getUserIdsGroupByDeptIdsV2(String appId, Long eid, Set<Long> deptIds) {
        if (StringUtils.isBlank(appId) || eid == null || CollectionUtils.isEmpty(deptIds)) {
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        logger.info("query user-center getUserIdsGroupByDeptIdsV2 api start, appId={}, eid={}, deptIds={}", appId, eid,
            deptIds);
        GetUserIdsByDeptIdsRequest deptIdsRequest = new GetUserIdsByDeptIdsRequest();
        deptIdsRequest.setEnterpriseId(eid);
        deptIdsRequest.setAppId(appId);
        deptIdsRequest.setDeptIds(Lists.newArrayList(deptIds));
        deptIdsRequest.setRecursiveSubDept(true);
        GetUserIdsGroupByDeptResult result = null;
        try {
            result = userService.getUserIdsGroupByDeptIdV2(deptIdsRequest);
        } catch (Exception e) {
            logger.error("query user-center getUserIdsGroupByDeptIdV2 api catch exception, message={}", e.getMessage());
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        if (result == null) {
            logger.error("query user-center getUserIdsGroupByDeptIdV2 result is null");
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        if (!result.getSuccess()) {
            logger.error("query user-center getUserIdsGroupByDeptIdV2 result error, resultCode={}",
                result.getResultCode());
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        return DepartmentUserRelationConverter.convertRelations2Map(result.getDepartmentUserRelations());
    }

    /**
     * 调用user-center获取一批用户组下的人
     *
     * @param appId
     * @param eid
     * @param groupIds
     * @return
     */
    public Map<Long, Set<Long>> getUserIdsGroupByGroupIds(String appId, Long eid, Set<Long> groupIds) {

        if (StringUtils.isBlank(appId) || eid == null || CollectionUtils.isEmpty(groupIds)) {
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        GetUserIdsByUserGroupIdsRequest groupIdsRequest = new GetUserIdsByUserGroupIdsRequest();
        groupIdsRequest.setEnterpriseId(eid);
        groupIdsRequest.setAppId(appId);
        groupIdsRequest.setUserGroupIds(Lists.newArrayList(groupIds));
        GetUserIdsGroupByUserGroupResult groupResult = userService.getUserIdsGroupByUserGroupIds(groupIdsRequest);
        if (!groupResult.getSuccess()) {
            logger.error("getUserIdsGroupByGroupIds err, groupIds={}, appId={}, eid={}, errCode={}", groupIds, appId,
                eid, groupResult.getResultCode());
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }

        return UserGroupUserRelationConverter.convertRelations2Map(groupResult.getUserGroupUserRelations());
    }

    /**
     * 调用user-center获取一批岗位（组）下的所有人
     *
     * @param appId
     * @param eid
     * @param positionIds
     * @return
     */
    public Map<Long, Set<Long>> getUserIdsGroupByPositionIds(String appId, Long eid, Set<Long> positionIds) {
        if (StringUtils.isBlank(appId) || eid == null || CollectionUtils.isEmpty(positionIds)) {
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        GetUserIdsByPositionIdsRequest positionIdsRequest = new GetUserIdsByPositionIdsRequest();
        positionIdsRequest.setEnterpriseId(eid);
        positionIdsRequest.setAppId(appId);
        positionIdsRequest.setPositionIds(Lists.newArrayList(positionIds));
        GetUserIdsGroupByPositionResult groupResult = userService.getUserIdsGroupByPositionIds(positionIdsRequest);
        if (!groupResult.getSuccess()) {
            logger.error("getUserIdsGroupByPositionIds err, positionIds={}, appId={}, eid={}, errCode={}", positionIds,
                appId, eid, groupResult.getResultCode());
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        return PositionUserRelationConverter.convertRelations2Map(groupResult.getPositionUserRelations());
    }

    /**
     * 调用user-center获取一批岗位（组）下的所有人
     *
     * @param appId
     * @param eid
     * @param positionIds
     * @return
     */
    public Map<Long, Set<Long>> getUserIdsGroupByPositionIdsV2(String appId, Long eid, Set<Long> positionIds) {
        if (StringUtils.isBlank(appId) || eid == null || CollectionUtils.isEmpty(positionIds)) {
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        logger.info("query user-center getUserIdsGroupByPositionIdsV2 api start, appId={}, eid={}, positionIds={}",
            appId, eid, positionIds);
        GetUserIdsByPositionIdsRequest positionIdsRequest = new GetUserIdsByPositionIdsRequest();
        positionIdsRequest.setEnterpriseId(eid);
        positionIdsRequest.setAppId(appId);
        positionIdsRequest.setPositionIds(Lists.newArrayList(positionIds));
        GetUserIdsGroupByPositionResult result;
        try {
            result = userService.getUserIdsGroupByPositionIdsV2(positionIdsRequest);
        } catch (Exception e) {
            logger.error("query user-center getUserIdsGroupByPositionIdsV2 api catch exception, message={}",
                e.getMessage());
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        if (result == null) {
            logger.error("query user-center getUserIdsGroupByPositionIdsV2 result is null");
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        if (!result.getSuccess()) {
            logger.error("query user-center getUserIdsGroupByPositionIdsV2 result error, resultCode={}",
                result.getResultCode());
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        return PositionUserRelationConverter.convertRelations2Map(result.getPositionUserRelations());
    }

    /**
     * 查询管辖一批人的所有人及他们的所有领导 (由于目前查询单个用户的上级领导比较耗时, 批量查询的接口服务方暂未实现, 而且目前调用方在多线程环境, 暂时先采用循环 调用的方式😭)
     *
     * @param appId
     * @param eid
     * @param userIds
     * @return
     */
    private Map<String, List<String>> queryUserAuthoritySupervisorMap(String appId, Long eid, List<String> userIds) {

        if (StringUtils.isBlank(appId) || eid == null || CollectionUtils.isEmpty(userIds)) {
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }

        Map<String, List<String>> resultMap = Maps.newHashMap();
        userIds.forEach(userId -> {
            logger.info("start to queryAuthorityUserAndSupervisor, appI={}, eid={}, userId={}", appId, eid, userId);
            List<String> tmpUserIds = queryAuthorityUserAndSupervisor(appId, eid, userId);
            if (CollectionUtils.isNotEmpty(tmpUserIds)) {
                resultMap.put(userId, tmpUserIds);
            }
        });
        logger.info("queryAuthorityUserAndSupervisor end, query userIds.size={}, resultMap.size={}", userIds.size(),
            resultMap.size());
        return resultMap;
    }

    /**
     * 查询所有可以管理某人以及某人的所有上级领导
     *
     * @param appId
     * @param eid
     * @param userId
     * @return
     */
    private List<String> queryAuthorityUserAndSupervisor(String appId, Long eid, String userId) {
        if (StringUtils.isBlank(appId) || eid == null || StringUtils.isBlank(userId)
            || CommonConstants.ONE_VALUE_STR.equals(userId)) {
            return CommonConstants.EMPTY_LIST;
        }
        GetAuthorityUserRequest request = new GetAuthorityUserRequest();
        request.setAppId(appId);
        request.setEnterpriseId(eid);
        request.setUserId(Long.valueOf(userId));
        request.setWithSupervisor(true);
        GetAuthorityUserIdsResult result;
        try {
            result = userService.getAuthorityUserAndSupervisor(request);
        } catch (Exception e) {
            logger.error("getAuthorityUserAndSupervisor catch exception, message={}", e.getMessage());
            return CommonConstants.EMPTY_LIST;
        }
        if (!result.getSuccess()) {
            logger.error("getAuthorityUserAndSupervisor err, appId={}, eid={}, userId={}, resultCode={}", appId, eid,
                userId, result.getResultCode());
            return CommonConstants.EMPTY_LIST;
        }
        if (CollectionUtils.isEmpty(result.getUserIds())) {
            return CommonConstants.EMPTY_LIST;
        }
        return result.getUserIds().stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 查询一批分类id和所有父分类id的映射
     *
     * @param appId
     * @param eid
     * @param resType
     * @param classifyIds
     * @return
     */
    private Map<Long, List<Long>> queryClassifyIdParentIdMap(String appId, Long eid, String resType,
        List<Long> classifyIds) {
        if (StringUtils.isBlank(appId) || eid == null || StringUtils.isBlank(resType)
            || CollectionUtils.isEmpty(classifyIds)) {
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }

        GetResourceClassifyRelationRequest request = new GetResourceClassifyRelationRequest();
        request.setAppId(appId);
        request.setEnterpriseId(eid);
        request.setResourceType(resType);
        request.setClassifyIds(CommonUtils.convertToStringList(classifyIds));

        GetResourceClassifyRelationReverseListResult relationReverseListResult =
            resourceServiceProxy.getResourceClassifyRelationReverse(request);
        if (!relationReverseListResult.getSuccess()) {
            logger.error("getResourceClassifyRelationReverse err, request={}, resultCode={}", request,
                relationReverseListResult.getResultCode());
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        List<ResourceClassifyRelationReverse> resourceClassifyRelationReverses =
            relationReverseListResult.getResourceClassifyRelationReverses();
        Map<Long, List<Long>> result = Maps.newHashMap();
        resourceClassifyRelationReverses.forEach(relation -> {
            result.put(Long.valueOf(relation.getClassifyId()), CommonUtils.convertToLongList(relation.getParentIds()));
        });
        return result;
    }

    /**
     * 查询企业下所有的管理员
     * 
     * @param appId
     * @param eid
     * @return
     */
    private List<String> getAllAdminUserIds(String appId, Long eid) {
        if (StringUtils.isBlank(appId) || eid == null) {
            return CommonConstants.EMPTY_LIST;
        }
        GetAppEnterpriseAdministratorRequest request = new GetAppEnterpriseAdministratorRequest();
        request.setAppId(appId);
        request.setEnterpriseId(eid);
        GetAppEnterpriseAdministratorResult administratorResult = null;
        try {
            administratorResult = userService.getAppEnterpriseAdministrator(request);
        } catch (Exception e) {
            logger.error("getAppEnterpriseAdministrator err, appId={}, eid={}", appId, eid, e);
        }
        if (administratorResult != null && !administratorResult.getSuccess()) {
            logger.error("getAppEnterpriseAdministrator err, appId={}, eid={}, resultCode={}", appId, eid,
                administratorResult.getResultCode());
            return CommonConstants.EMPTY_LIST;
        }

        List<Long> administrators = administratorResult.getAdministrators();
        if (administrators == null) {
            administrators = CommonConstants.EMPTY_LIST;
        }
        return administrators.stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 构造写入es对象
     *
     * @param param
     * @return
     */
    private UserResourceDo buildUserResourceDo(BuildUserResourceDoParam param, List<String> allAdminUserIds) {
        if (param == null) {
            return null;
        }
        UserResourceDo out = new UserResourceDo();
        String appId = param.getAppId();
        out.setAppId(appId);
        String enterpriseId = String.valueOf(param.getEnterpriseId());
        out.setEnterpriseId(enterpriseId);
        out.setResourceType(param.getResourceType());

        Resource resDetail = param.getResDetail();

        out.setResourceId(resDetail.getResourceId());
        out.setName(resDetail.getName());
        out.setDesc(resDetail.getDesc());
        out.setDataDict(resDetail.getDataDict());
        out.setCreateTs(resDetail.getCreateTs());
        String createUserId = resDetail.getCreateUserId();
        out.setCreateUserId(createUserId);
        out.setCreateUserName(resDetail.getCreateUserName());

        out.setClassifyIds(flatClassifyId(resDetail.getClassifyIds(), param.getClassifyIdParentIdMap()));

        List<String> authorityWithSupervisorUserIds = param.getAuthoritySupervisorMap().get(createUserId);
        AuthInfoUserMap authInfoUserMap = param.getAuthInfoUserMap();

        ResourceAuthInfo queryAuthInfo = resDetail.getQueryAuthInfo();
        List<String> queryUserIds = calAuthUserIds(appId, enterpriseId, queryAuthInfo, authInfoUserMap,
            authorityWithSupervisorUserIds, allAdminUserIds, createUserId);

        out.setVisibleType(queryAuthInfo.getVisibleType());
        out.setUserIds(queryUserIds);

        ResourceAuthInfo referAuthInfo = resDetail.getReferAuthInfo();
        if (referAuthInfo != null) {
            List<String> referUserIds = calAuthUserIds(appId, enterpriseId, referAuthInfo, authInfoUserMap,
                authorityWithSupervisorUserIds, allAdminUserIds, createUserId);
            out.setReferUserIds(referUserIds);
            out.setReferVisibleType(referAuthInfo.getVisibleType());
        }

        ResourceAuthInfo editAuthInfo = resDetail.getEditAuthInfo();
        if (editAuthInfo != null) {
            List<String> editUserIds = calAuthUserIds(appId, enterpriseId, editAuthInfo, authInfoUserMap,
                authorityWithSupervisorUserIds, allAdminUserIds, createUserId);
            out.setEditUserIds(editUserIds);
            out.setEditVisibleType(editAuthInfo.getVisibleType());
        }

        return out;
    }

    /**
     * 把一批资源分类id和它们的所有父分类id展开
     *
     * @param classifyIds
     * @param classifyIdParentIdMap
     * @return
     */
    private List<Long> flatClassifyId(List<Long> classifyIds, Map<Long, List<Long>> classifyIdParentIdMap) {
        if (CollectionUtils.isEmpty(classifyIds) || classifyIdParentIdMap == null) {
            return CommonConstants.EMPTY_LIST;
        }

        Set<Long> classifyIdSet = Sets.newHashSet();
        classifyIds.forEach(classifyId -> {
            classifyIdSet.add(classifyId);
            if (classifyIdParentIdMap.containsKey(classifyId)) {
                classifyIdSet.addAll(classifyIdParentIdMap.get(classifyId));
            }
        });
        return Lists.newArrayList(classifyIdSet);
    }

    /**
     * 计算资源本身可见的所有人以及可以管辖资源创建人以及资源创建人的所有上级领导的人
     *
     * @param appId
     * @param enterpriseId
     * @param resourceAuthInfo
     * @param authInfoUserMap
     * @param authorityWithSupervisorUserIds
     *            (创建人的所有上级领导以及可以管辖创建人的所有人)
     * @param adminUserIds
     *            (公司下所有的管理员用户id)
     * @param createUserId
     * @return
     */
    private List<String> calAuthUserIds(String appId, String enterpriseId, ResourceAuthInfo resourceAuthInfo,
        AuthInfoUserMap authInfoUserMap, List<String> authorityWithSupervisorUserIds, List<String> adminUserIds,
        String createUserId) {
        if (resourceAuthInfo == null) {
            return CommonConstants.EMPTY_LIST;
        }
        String visibleType = resourceAuthInfo.getVisibleType();

        // 资源本身可见的所有用户id
        List<String> resourceVisibleUserIds = null;
        if (ResourceVisibleTypeEnum.ALL.getValue().equals(visibleType)) {
            if (CollectionUtils.isNotEmpty(resourceAuthInfo.getUserIds())) {
                resourceVisibleUserIds =
                    resourceAuthInfo.getUserIds().stream().map(String::valueOf).collect(Collectors.toList());
            }
        } else {
            resourceVisibleUserIds = getCombinedUserIds(appId, enterpriseId, resourceAuthInfo, authInfoUserMap);
        }
        Set<String> userIdSet = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(authorityWithSupervisorUserIds)) {
            userIdSet.addAll(authorityWithSupervisorUserIds);
        }
        if (CollectionUtils.isNotEmpty(resourceVisibleUserIds)) {
            userIdSet.addAll(resourceVisibleUserIds);
        }

        Set<String> adminUserIdSet;
        if (adminUserIds == null) {
            adminUserIdSet = Sets.newHashSet();
        } else {
            adminUserIdSet = Sets.newHashSet(adminUserIds);
        }
        // 创建人拥有资源所有权限
        List<String> result = Lists.newArrayList(createUserId);
        for (String userId : userIdSet) {
            // 过滤掉主管理员, 主管理员一定可见所有的资源, 可以避免处理角色变更事件
            if (!adminUserIdSet.contains(userId)) {
                result.add(userId);
            }
        }
        return result;
    }

    /**
     * 把部门, 岗位, 用户组, 用户 下的所有用户展开
     *
     * @param appId
     * @param eid
     * @param resourceAuthInfo
     * @param authInfoUserMap
     * @return
     */
    private List<String> getCombinedUserIds(String appId, String eid, ResourceAuthInfo resourceAuthInfo,
        AuthInfoUserMap authInfoUserMap) {

        if (StringUtils.isBlank(eid) || StringUtils.isBlank(appId) || resourceAuthInfo == null
            || authInfoUserMap == null) {
            return CommonConstants.EMPTY_LIST;
        }

        // user
        Set<Long> userIdSet = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(resourceAuthInfo.getUserIds())) {
            userIdSet.addAll(resourceAuthInfo.getUserIds());
        }

        // dept
        List<Long> deptIds = resourceAuthInfo.getDepartmentIds();
        if (CollectionUtils.isNotEmpty(deptIds)) {
            Map<Long, Set<Long>> deptUserIdMap = authInfoUserMap.getDeptUserIdMap();
            if (deptUserIdMap != null) {
                List<Long> deptUserIds = deptIds.stream().filter(deptId -> deptUserIdMap.containsKey(deptId))
                    .flatMap(deptId -> deptUserIdMap.get(deptId).stream()).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(deptUserIds)) {
                    userIdSet.addAll(deptUserIds);
                }
            }
        }

        // group
        List<Long> groupIds = resourceAuthInfo.getUserGroupIds();
        if (CollectionUtils.isNotEmpty(groupIds)) {
            Map<Long, Set<Long>> groupUserIdMap = authInfoUserMap.getGroupUserIdMap();
            if (groupUserIdMap != null) {
                List<Long> groupUserIds = groupIds.stream().filter(groupId -> groupUserIdMap.containsKey(groupId))
                    .flatMap(groupId -> groupUserIdMap.get(groupId).stream()).collect(Collectors.toList());
                userIdSet.addAll(groupUserIds);
            }
        }

        // position
        List<Long> positionIds = resourceAuthInfo.getPositionIds();
        if (CollectionUtils.isNotEmpty(positionIds)) {
            Map<Long, Set<Long>> positionUserIdMap = authInfoUserMap.getPositionUserIdMap();
            if (positionUserIdMap != null) {
                List<Long> positionUserIds =
                    positionIds.stream().filter(positionId -> positionUserIdMap.containsKey(positionId))
                        .flatMap(positionId -> positionUserIdMap.get(positionId).stream()).collect(Collectors.toList());
                userIdSet.addAll(positionUserIds);
            }
        }

        return userIdSet.stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 查询已接入的resourceType
     *
     * @param appId
     * @param domainId
     * @param resourceType
     * @return
     */
    public List<String> queryRegisterResourceTypes(String appId, String domainId, String resourceType) {
        List<String> resourceTypes;
        if (StringUtils.isNotBlank(resourceType)) {
            return Lists.newArrayList(resourceType);
        }
        if (StringUtils.isBlank(appId)) {
            return CommonConstants.EMPTY_LIST;
        }
        try {
            resourceTypes = appResourceMapper.getAppResourceType(appId, domainId);
        } catch (Exception e) {
            logger.error("queryRegisterResourceTypes err, appId={}, domainId={}", appId, domainId, e);
            return null;
        }
        if (CollectionUtils.isEmpty(resourceTypes)) {
            logger.warn("empty resourceType for domainId, appId={}, domainId={}", appId, domainId);
        }
        return resourceTypes;
    }

    /**
     * 查询一批部门id和上级部门id的映射
     *
     * @param appId
     * @param eid
     * @param deptIds
     * @return
     */
    public Map<String, List<String>> queryDeptParentIdMap(String appId, String eid, List<String> deptIds) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(eid) || CollectionUtils.isEmpty(deptIds)) {
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }

        GetDepartmentRelationRequest request = new GetDepartmentRelationRequest();
        request.setAppId(appId);
        request.setEnterpriseId(Long.parseLong(eid));
        List<Long> deptIdParams = deptIds.stream().map(Long::parseLong).collect(Collectors.toList());
        request.setDepartmentIds(deptIdParams);
        GetDepartmentRelationReverseListResult reverseListResult =
            departmentService.getDepartmentRelationsReverseV2(request);
        if (!reverseListResult.getSuccess()) {
            logger.error("getDepartmentRelationsReverse err, appI={}, eid={}, deptIds={}, resultCode={}", appId, eid,
                deptIds, reverseListResult.getResultCode());
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        Map<Long, Set<Long>> deptParentIdMap =
            DepartmentRelationConverter.convertRelationsReverse2Map(reverseListResult.getDepartmentRelationReverses());
        Map<String, List<String>> resultMap = Maps.newHashMap();
        deptParentIdMap.forEach((k, v) -> {
            resultMap.put(String.valueOf(k), v.stream().map(String::valueOf).collect(Collectors.toList()));
        });
        return resultMap;
    }

    /**
     * 查询一批部门的所有上级部门
     *
     * @param appId
     * @param eid
     * @param deptIds
     * @param containSelf
     *            (是否包括自己)
     * @return
     */
    public List<String> queryParentDeptIds(String appId, String eid, List<String> deptIds, boolean containSelf) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(eid) || CollectionUtils.isEmpty(deptIds)) {
            return CommonConstants.EMPTY_LIST;
        }
        Map<String, List<String>> map = queryDeptParentIdMap(appId, eid, deptIds);
        if (map == null) {
            return CommonConstants.EMPTY_LIST;
        }
        Set<String> set = map.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        if (containSelf) {
            set.addAll(deptIds);
        }
        return Lists.newArrayList(set);
    }

    /**
     * 查询一批岗位(组)和上级岗位组的映射
     *
     * @param appId
     * @param eid
     * @param positionIds
     * @return
     */
    public Map<String, List<String>> queryPositionIdParentIdMap(String appId, String eid, List<String> positionIds) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(eid) || CollectionUtils.isEmpty(positionIds)) {
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        GetPositionRelationRequest request = new GetPositionRelationRequest();
        request.setAppId(appId);
        request.setEnterpriseId(Long.parseLong(eid));
        List<Long> positionParams = positionIds.stream().map(Long::parseLong).collect(Collectors.toList());
        request.setPositionIds(positionParams);
        GetPositionRelationReverseListResult relationsReverse = positionService.getPositionRelationsReverseV2(request);
        if (!relationsReverse.getSuccess()) {
            logger.error("getPositionRelationsReverse err, appId={}, eid={}, positionIds={}, resultCode={}", appId, eid,
                String.join(CommonConstants.COMMA, positionIds), relationsReverse.getResultCode());
            return Maps.newHashMapWithExpectedSize(CommonConstants.EMPTY_COLLECTION_SIZE);
        }
        Map<Long, Set<Long>> map =
            PositionRelationConverter.convertRelationsReverse2Map(relationsReverse.getPositionRelationReverses());
        Map<String, List<String>> resultMap = Maps.newHashMap();
        map.forEach((k, v) -> {
            resultMap.put(String.valueOf(k), v.stream().map(String::valueOf).collect(Collectors.toList()));
        });
        return resultMap;
    }

    /**
     * 查询一批岗位（组）的所有上级岗位组
     *
     * @param appId
     * @param eid
     * @param positionIds
     * @param containSelf
     * @return
     */
    public List<String> queryParentPositionIds(String appId, String eid, List<String> positionIds,
        boolean containSelf) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(eid) || CollectionUtils.isEmpty(positionIds)) {
            return CommonConstants.EMPTY_LIST;
        }
        Map<String, List<String>> map = queryPositionIdParentIdMap(appId, eid, positionIds);
        if (map == null) {
            return CommonConstants.EMPTY_LIST;
        }
        Set<String> set = map.values().stream().flatMap(l -> l.stream()).collect(Collectors.toSet());
        if (containSelf) {
            set.addAll(positionIds);
        }
        return Lists.newArrayList(set);
    }

    /**
     * 获取直接挂在某些部门下的资源id
     *
     * @param appId
     * @param eid
     * @param resourceType
     * @param deptIds
     * @return
     */
    public List<String> queryDirectResourceIdsByDeptIds(String appId, String eid, String resourceType,
        List<String> deptIds) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(eid) || CollectionUtils.isEmpty(deptIds)) {
            return CommonConstants.EMPTY_LIST;
        }
        GetDirectResourceIdsByOrgIdsRequest request = new GetDirectResourceIdsByOrgIdsRequest();
        request.setAppId(appId);
        request.setEnterpriseId(Long.valueOf(eid));
        request.setResourceType(resourceType);
        request.setOrgIds(deptIds);
        GetDirectResourceIdsByOrgIdsResult result = resourceServiceProxy.getDirectResourceIdsByDepartmentIds(request);
        if (!result.getSuccess()) {
            logger.error(
                "getDirectResourceIdsByDepartmentIds err, appId={}, eid={}, resourceType={}, deptIds={}, "
                    + "resultCode={}",
                appId, eid, resourceType, deptIds.stream().collect(Collectors.joining(CommonConstants.COMMA)),
                result.getResultCode());
            return CommonConstants.EMPTY_LIST;
        }
        return result.getResourceIds();
    }

    /**
     * 查询直接挂在某些岗位(组)下的资源id
     *
     * @param appId
     * @param eid
     * @param resourceType
     * @param positionIds
     * @return
     */
    public List<String> queryDirectResourceIdsByPositionIds(String appId, String eid, String resourceType,
        List<String> positionIds) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(eid) || CollectionUtils.isEmpty(positionIds)) {
            return CommonConstants.EMPTY_LIST;
        }
        GetDirectResourceIdsByOrgIdsRequest request = new GetDirectResourceIdsByOrgIdsRequest();
        request.setAppId(appId);
        request.setEnterpriseId(Long.valueOf(eid));
        request.setResourceType(resourceType);
        request.setOrgIds(positionIds);
        GetDirectResourceIdsByOrgIdsResult result = resourceServiceProxy.getDirectResourceIdsByPositionIds(request);
        if (!result.getSuccess()) {
            logger.error(
                "getDirectResourceIdsByPositionIds err, appId={}, eid={}, resourceType={}, deptIds={}, "
                    + "resultCode={}",
                appId, eid, resourceType, positionIds.stream().collect(Collectors.joining(CommonConstants.COMMA)),
                result.getResultCode());
            return CommonConstants.EMPTY_LIST;
        }
        return result.getResourceIds();
    }

    /**
     * 查询直接挂在某些用户组下的所有资源id
     *
     * @param appId
     * @param eid
     * @param resourceType
     * @param groupIds
     * @return
     */
    public List<String> queryDirectResourceIdsByGroupIds(String appId, String eid, String resourceType,
        List<String> groupIds) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(eid) || CollectionUtils.isEmpty(groupIds)) {
            return CommonConstants.EMPTY_LIST;
        }
        GetDirectResourceIdsByOrgIdsRequest request = new GetDirectResourceIdsByOrgIdsRequest();
        request.setAppId(appId);
        request.setEnterpriseId(Long.valueOf(eid));
        request.setResourceType(resourceType);
        request.setOrgIds(groupIds);
        GetDirectResourceIdsByOrgIdsResult result = resourceServiceProxy.getDirectResourceIdsByUserGroupIds(request);
        if (!result.getSuccess()) {
            logger.error(
                "getDirectResourceIdsByPositionIds err, appId={}, eid={}, resourceType={}, deptIds={}, "
                    + "resultCode={}",
                appId, eid, resourceType, groupIds.stream().collect(Collectors.joining(CommonConstants.COMMA)),
                result.getResultCode());
            return CommonConstants.EMPTY_LIST;
        }
        return result.getResourceIds();
    }

    /**
     * 查询某些人创建的所有资源id
     *
     * @param appId
     * @param eid
     * @param resourceType
     * @param userIds
     * @param visibleTypeEnum
     * @return
     */
    public List<String> queryResourceIdsByCreateUsers(String appId, String eid, String resourceType,
        List<String> userIds, ResourceVisibleTypeEnum visibleTypeEnum) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(eid) || StringUtils.isBlank(resourceType)
            || CollectionUtils.isEmpty(userIds)) {
            return CommonConstants.EMPTY_LIST;
        }
        GetPageResourceIdByCreateUserRequest request = new GetPageResourceIdByCreateUserRequest();
        request.setAppId(appId);
        request.setEnterpriseId(Long.valueOf(eid));
        request.setResourceType(resourceType);
        request.setPageNum(CommonConstants.ONE_VALUE_INTEGER);
        request.setPageSize(CommonConstants.BATCH_QUERY_SIZE);
        request.setUserIds(userIds);
        if (visibleTypeEnum != null) {
            request.setResourceVisibleType(visibleTypeEnum.getValue());
        }

        List<String> resourceIds = Lists.newArrayList();
        GetPageBatchResourceInfoResult pageResult;
        Set<Integer> pageNumSet = Sets.newHashSet();
        for (;;) {
            if (!pageNumSet.contains(request.getPageNum())) {
                pageNumSet.add(request.getPageNum());
            }
            pageResult = resourceServiceProxy.getPageResourceIdByCreateUser(request);
            if (!pageResult.getSuccess()) {
                logger.error(
                    "getPageResourceIdByCreateUser err, appId={}, eid={}, resourceType={},"
                        + "pageNum={}, resultCode={}",
                    appId, eid, resourceType, request.getPageNum(), pageResult.getResultCode());
                break;
            }
            if (CollectionUtils.isNotEmpty(pageResult.getResourceIds())) {
                resourceIds.addAll(pageResult.getResourceIds());
            }
            if (!pageResult.isHasNextPage()) {
                break;
            }
            request.setPageNum(pageResult.getNextPage());
        }
        return resourceIds;
    }

    /**
     * 查询某一组织及其所有父组织挂载的资源Id
     *
     * @param msgId
     *            消息id
     * @param appId
     *            addId
     * @param eid
     *            企业id
     * @param resourceType
     *            资源类型
     * @param orgType
     *            组织类型
     * @param orgIds
     *            组织id集合
     * @return
     */
    public List<String> queryResourceIdByParentOrgIds(String msgId, String appId, String eid, String resourceType,
        String orgType, List<String> orgIds) {
        List<String> orgResourceIds = Lists.newArrayList();
        switch (FastEngineOrgTypeEnum.getByValue(orgType)) {
            case DEPARTMENT:
                List<String> allDeptIds = queryParentDeptIds(appId, eid, orgIds, true);
                logger.info("org query resourceIds start query direct resource ids by dept,msgId = {}", msgId);
                List<String> deptResourceIds = queryDirectResourceIdsByDeptIds(appId, eid, resourceType, allDeptIds);
                if (CollectionUtils.isNotEmpty(deptResourceIds)) {
                    orgResourceIds.addAll(deptResourceIds);
                }
                break;
            case GROUP:
                logger.info("org query resourceIds start query direct resource ids by group,msgId = {}", msgId);
                List<String> groupResourceIds = queryDirectResourceIdsByGroupIds(appId, eid, resourceType, orgIds);
                if (CollectionUtils.isNotEmpty(groupResourceIds)) {
                    orgResourceIds.addAll(groupResourceIds);
                }
                break;
            case POSITION:
                List<String> allPositionIds = queryParentPositionIds(appId, eid, orgIds, true);
                logger.info("org query resourceIds start query direct resource ids by position,msgId = {}", msgId);
                List<String> positionResourceIds =
                    queryDirectResourceIdsByPositionIds(appId, eid, resourceType, allPositionIds);
                if (CollectionUtils.isNotEmpty(positionResourceIds)) {
                    orgResourceIds.addAll(positionResourceIds);
                }
                break;
            default:
                logger.error("org query resourceIds orgType error, msgId={}", msgId);
                break;
        }
        return orgResourceIds;
    }

    /**
     * 查询某人当前可见范围的所有用户id
     *
     * @param appId
     * @param eid
     * @param userId
     * @return
     */
    public List<String> queryAuthorityUserIds(String appId, Long eid, Long userId) {
        if (StringUtils.isBlank(appId) || eid == null || userId == null) {
            return CommonConstants.EMPTY_LIST;
        }
        net.coolcollege.authority.facade.model.result.GetAuthorityUserIdsResult currentUidResult =
            authorityRangeService.getAllAuthorityUserIds(eid, userId, appId);
        if (!currentUidResult.getSuccess()) {
            logger.error("getAllAuthorityUserIds err, appId={}, eid={}, userId={}, resultCode={}", appId, eid, userId,
                currentUidResult.getResultCode());
            return CommonConstants.EMPTY_LIST;
        }
        if (CollectionUtils.isEmpty(currentUidResult.getUserIds())) {
            return CommonConstants.EMPTY_LIST;
        }
        return currentUidResult.getUserIds().stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 查询某人上次可见范围用户id
     *
     * @param appId
     * @param eid
     * @param userId
     * @return
     */
    public List<String> queryLastAuthorityUserIds(String appId, Long eid, Long userId) {
        if (StringUtils.isBlank(appId) || eid == null || userId == null) {
            return CommonConstants.EMPTY_LIST;
        }

        net.coolcollege.authority.facade.model.result.GetAuthorityUserIdsResult lastUidResult =
            authorityRangeService.getAllLastAuthorityUserIds(eid, userId, appId);
        if (!lastUidResult.getSuccess()) {
            logger.error("getAllLastAuthorityUserIds err, appId={}, eid={}, userId={}, resultCode={}", appId, eid,
                userId, lastUidResult.getResultCode());
            return CommonConstants.EMPTY_LIST;
        }
        if (CollectionUtils.isEmpty(lastUidResult.getUserIds())) {
            return CommonConstants.EMPTY_LIST;
        }

        return lastUidResult.getUserIds().stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 比较两个集合的差集
     *
     * @param list1
     * @param list2
     * @return
     */
    private List<String> difList(List<String> list1, List<String> list2) {
        if (CollectionUtils.isEmpty(list1) && CollectionUtils.isEmpty(list2)) {
            return CommonConstants.EMPTY_LIST;
        }
        if (CollectionUtils.isEmpty(list1)) {
            return list2;
        }
        if (CollectionUtils.isEmpty(list2)) {
            return list1;
        }
        HashSet<String> set1 = Sets.newHashSet(list1);
        HashSet<String> set2 = Sets.newHashSet(list2);
        List<String> addList = list1.stream().filter(i -> !set2.contains(i)).collect(Collectors.toList());
        List<String> removeList = list2.stream().filter(i -> !set1.contains(i)).collect(Collectors.toList());

        Set<String> difList = Sets.newHashSet();
        difList.addAll(addList);
        difList.addAll(removeList);

        return Lists.newArrayList(difList);
    }

    /**
     * 计算某人当前授权范围和上次授权范围用户id的差集
     *
     * @param appId
     * @param eid
     * @param userId
     * @return
     */
    public List<String> calUserDifAuthorityUserIds(String appId, String eid, String userId) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(eid) || StringUtils.isBlank(userId)) {
            return CommonConstants.EMPTY_LIST;
        }
        Long enterpriseId;
        try {
            enterpriseId = Long.valueOf(eid);
        } catch (NumberFormatException e) {
            logger.error("invalid eid={}", eid);
            return CommonConstants.EMPTY_LIST;
        }
        Long uid;
        try {
            uid = Long.valueOf(userId);
        } catch (NumberFormatException e) {
            logger.error("invalid userId={}", userId);
            return CommonConstants.EMPTY_LIST;
        }
        List<String> currentUserIds = queryAuthorityUserIds(appId, enterpriseId, uid);
        List<String> lastUserIds = queryLastAuthorityUserIds(appId, enterpriseId, uid);
        List<String> difList = difList(currentUserIds, lastUserIds);

        return difList;
    }

    public List<String> queryAllCurrentSupervisors(String appId, String eid, String userId) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(eid) || StringUtils.isBlank(userId)) {
            return CommonConstants.EMPTY_LIST;
        }
        GetSupervisorRequest request = new GetSupervisorRequest();
        request.setAppId(appId);
        request.setEnterpriseId(Long.valueOf(eid));
        request.setUserId(Long.valueOf(userId));
        request.setDirection(QuerySupervisorDirectionEnum.UP.getValue());
        request.setRecursive(true);
        GetSupervisorResult userSupervisorResult = userService.getUserSupervisor(request);
        if (!userSupervisorResult.getSuccess()) {
            logger.error("getUserSupervisor err, appId={}, eid={}, userId={}, resultCode={}", appId, eid, userId,
                userSupervisorResult.getResultCode());
            return CommonConstants.EMPTY_LIST;
        }
        List<Long> userIds = userSupervisorResult.getUserIds();
        if (CollectionUtils.isEmpty(userIds)) {
            return CommonConstants.EMPTY_LIST;
        }

        List<String> supervisorIds = userIds.stream().map(String::valueOf).collect(Collectors.toList());
        return supervisorIds;
    }

    /**
     * 查询某人现在和上次的所有上级领导
     * 
     * @param appId
     * @param eid
     * @param userOriginSupervisorInfo
     * @return
     */
    public List<String> queryAllSupervisorIdsWithOrigin(String appId, String eid,
        UserOriginSupervisorInfo userOriginSupervisorInfo) {
        String userId = userOriginSupervisorInfo.getUserId();

        Set<String> allSupervisorIds = Sets.newHashSet();
        // 当前的所有上级领导
        List<String> currentSupervisorIds = queryAllCurrentSupervisors(appId, eid, userId);
        if (CollectionUtils.isNotEmpty(currentSupervisorIds)) {
            allSupervisorIds.addAll(currentSupervisorIds);
        }
        String originDirectSupervisorId = userOriginSupervisorInfo.getOriginDirectSupervisorId();
        if (StringUtils.isNotBlank(originDirectSupervisorId)) {
            allSupervisorIds.add(originDirectSupervisorId);
            List<String> originSupervisorIds = queryAllCurrentSupervisors(appId, eid, originDirectSupervisorId);
            if (CollectionUtils.isNotEmpty(originSupervisorIds)) {
                allSupervisorIds.addAll(originSupervisorIds);
            }
        }
        return Lists.newArrayList(allSupervisorIds);
    }

    /**
     * 获取用户管辖范围类型
     *
     * @param eid
     *            企业id
     * @param userId
     *            用户id
     * @return
     */
    public String getAuthorityRangeTypeByUserId(String appId, Long eid, Long userId) {
        BatchGetAuthorityRangeTypeRequest request = new BatchGetAuthorityRangeTypeRequest();
        request.setAppId(appId);
        request.setEnterpriseId(eid);
        request.setUserIds(Lists.newArrayList(userId));
        BatchGetAuthorityRangeTypeResult batchGetAuthorityRangeTypeResult =
            authorityRangeService.batchGetAuthorityRangeType(request);
        String resultStr = CommonConstants.EMPTY_STR;
        if (!Objects.isNull(batchGetAuthorityRangeTypeResult)) {
            resultStr = JSON.toJSONString(batchGetAuthorityRangeTypeResult);
        }
        if (Objects.isNull(batchGetAuthorityRangeTypeResult) || !batchGetAuthorityRangeTypeResult.getSuccess()) {
            logger.warn("getAuthorityRangeTypeByUserId err, eid:{}, userId:{}, resultStr={}", eid, userId, resultStr);
            return RangeTypeEnum.ONLY_ME.getValue();
        }
        String rangeType = null;
        if (CollectionUtils.isNotEmpty(batchGetAuthorityRangeTypeResult.getRangeTypeRelations())) {
            rangeType = batchGetAuthorityRangeTypeResult.getRangeTypeRelations().get(CommonConstants.ZERO_VALUE_INT)
                .getRangeType();
        }
        return rangeType;
    }
}