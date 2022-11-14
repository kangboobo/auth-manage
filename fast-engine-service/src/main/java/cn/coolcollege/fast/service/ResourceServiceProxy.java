package cn.coolcollege.fast.service;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.entity.request.*;
import cn.coolcollege.fast.entity.result.*;
import cn.coolcollege.fast.model.LoadResourceTypeReq;
import cn.coolcollege.fast.model.LoadResourceTypeResp;
import cn.coolcollege.fast.storage.entity.AppResourceDo;
import cn.coolcollege.fast.storage.mapper.AppResourceMapper;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.runtime.api.client.ReferenceClient;
import com.alipay.sofa.runtime.api.client.param.BindingParam;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.google.common.collect.Maps;
import net.coolcollege.platform.util.constants.CommonConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * resourceService的一个代理（由于服务实现方可能会分布在各个服务, 通过维护fast_engine_app_resource来动态加载client）
 *
 * @author pk
 * @date 2021-06-22 16:27
 */
@Service
public class ResourceServiceProxy implements IResourceService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceProxy.class);

    /**
     * resourceType和服务uniqueId的映射 (之所以不直接搞一个resourceType和IResourceService的映射 是因为允许多个resourceType对应一个IResourceService,
     * 而uniqueId和IResourceService是一对一)
     */
    private static final Map<String, String> RESOURCE_TYPE_UNIQUE_ID_MAP = Maps.newHashMap();

    /**
     * uniqueId和具体服务的映射
     */
    private static final Map<String, IResourceService> UNIQUE_ID_RESOURCE_SERVICE_MAP = Maps.newHashMap();

    @Autowired
    private ClientFactoryBean clientFactoryBean;

    @Autowired
    private AppResourceMapper appResourceMapper;

    @Override
    public GetPageBatchResourceInfoResult getPageBatchResourceByEnterpriseId(GetPageBatchResourceInfoRequest request) {
        IResourceService iResourceService = getResourceService(request.getResourceType());
        if (iResourceService == null) {
            return null;
        }
        return iResourceService.getPageBatchResourceByEnterpriseId(request);
    }

    @Override
    public GetResourceDetailsByResourceIdsResult
        getResourceDetailsByResourceIds(GetResourceDetailsByResourceIdsRequest request) {
        IResourceService iResourceService = getResourceService(request.getResourceType());
        if (iResourceService == null) {
            logger.error("getResourceService failed, request={}", request);
            return null;
        }
        return iResourceService.getResourceDetailsByResourceIds(request);
    }

    @Override
    public GetDirectResourceIdsByUserIdsResult
        getDirectResourceIdsByUserIds(GetDirectResourceIdsByUserIdsRequest request) {
        IResourceService iResourceService = getResourceService(request.getResourceType());
        if (iResourceService == null) {
            logger.error("getResourceService failed, request={}", request);
            return null;
        }
        return iResourceService.getDirectResourceIdsByUserIds(request);
    }

    @Override
    public GetDirectResourceIdsByOrgIdsResult
        getDirectResourceIdsByDepartmentIds(GetDirectResourceIdsByOrgIdsRequest request) {
        IResourceService iResourceService = getResourceService(request.getResourceType());
        if (iResourceService == null) {
            logger.error("getResourceService failed, request={}", request);
            return null;
        }
        return iResourceService.getDirectResourceIdsByDepartmentIds(request);
    }

    @Override
    public GetDirectResourceIdsByOrgIdsResult
        getDirectResourceIdsByUserGroupIds(GetDirectResourceIdsByOrgIdsRequest request) {
        IResourceService iResourceService = getResourceService(request.getResourceType());
        if (iResourceService == null) {
            logger.error("getResourceService failed, request={}", request);
            return null;
        }
        return iResourceService.getDirectResourceIdsByUserGroupIds(request);
    }

    @Override
    public GetDirectResourceIdsByOrgIdsResult
        getDirectResourceIdsByPositionIds(GetDirectResourceIdsByOrgIdsRequest request) {
        IResourceService iResourceService = getResourceService(request.getResourceType());
        if (iResourceService == null) {
            logger.error("getResourceService failed, request={}", request);
            return null;
        }
        return iResourceService.getDirectResourceIdsByPositionIds(request);
    }

    @Override
    public GetResourceClassifyRelationReverseListResult
        getResourceClassifyRelationReverse(GetResourceClassifyRelationRequest request) {
        IResourceService iResourceService = getResourceService(request.getResourceType());
        if (iResourceService == null) {
            logger.error("getResourceService failed, request={}", request);
            return null;
        }
        return iResourceService.getResourceClassifyRelationReverse(request);
    }

    @Override
    public GetPageDirectExtendResourceIdsByClassifyIdResult
        getPageDirectExtendResourceIdsByClassifyId(GetPageDirectExtendResourceIdsByClassifyIdRequest request) {
        IResourceService iResourceService = getResourceService(request.getResourceType());
        if (iResourceService == null) {
            logger.error("getResourceService failed, request={}", request);
            return null;
        }
        return iResourceService.getPageDirectExtendResourceIdsByClassifyId(request);
    }

    @Override
    public GetPageBatchResourceInfoResult getPageResourceIdByCreateUser(GetPageResourceIdByCreateUserRequest request) {
        IResourceService iResourceService = getResourceService(request.getResourceType());
        if (iResourceService == null) {
            logger.error("getResourceService failed, request={}", request);
            return null;
        }
        return iResourceService.getPageResourceIdByCreateUser(request);
    }

    public LoadResourceTypeResp loadResourceType(LoadResourceTypeReq req) {

        AppResourceDo appResourceDo = new AppResourceDo();
        appResourceDo.setAppId(req.getAppId());
        appResourceDo.setDomainId(req.getDomainId());
        appResourceDo.setResourceType(req.getResourceType());
        appResourceDo.setUniqueId(req.getUniqueId());
        appResourceDo.setEsIndex(req.getEsIndex());
        appResourceDo.setIncludeClassify(req.getIncludeClassify());
        appResourceDo.setCreateTime(new Date());

        LoadResourceTypeResp resp = new LoadResourceTypeResp();
        try {
            appResourceMapper.insertSelective(appResourceDo);
            loadSingleResourceType(appResourceDo);
        } catch (Exception e) {
            logger.error("loadResourceType err, req={}", req, e);
            resp.setLoaded(CommonConstants.FALSE);
            resp.setSuccess(false);
        }
        return resp;
    }

    /**
     * 获取真实的服务
     *
     * @param resourceType
     * @return
     */
    private IResourceService getResourceService(String resourceType) {
        if (StringUtils.isBlank(resourceType)) {
            return null;
        }
        String uniqueId = RESOURCE_TYPE_UNIQUE_ID_MAP.get(resourceType);
        if (StringUtils.isBlank(uniqueId)) {
            logger.error("uniqueId not conf, resourceType={}", resourceType);
            return null;
        }
        if (!UNIQUE_ID_RESOURCE_SERVICE_MAP.containsKey(uniqueId)) {
            logger.error("uniqueId not support resourceType={}, uniqueId={}", resourceType, uniqueId);
            return null;
        }
        return UNIQUE_ID_RESOURCE_SERVICE_MAP.get(uniqueId);
    }

    /**
     * 根据uniqueId构造服务
     *
     * @param uniqueId
     * @return
     */
    private IResourceService buildResourceServiceByUniqueId(String uniqueId) {
        if (StringUtils.isBlank(uniqueId)) {
            return null;
        }
        ReferenceClient referenceClient = clientFactoryBean.getClientFactory().getClient(ReferenceClient.class);
        ReferenceParam<IResourceService> referenceParam = new ReferenceParam();
        referenceParam.setInterfaceType(IResourceService.class);
        referenceParam.setUniqueId(uniqueId);

        BindingParam bindingParam = new BoltBindingParam();
        referenceParam.setBindingParam(bindingParam);

        IResourceService resourceService = referenceClient.reference(referenceParam);
        return resourceService;
    }

    private void reloadResourceTypeFromDb() {
        // resourceType是有限的,可以查询全部
        List<AppResourceDo> appResourceDos = appResourceMapper.selectAll();
        if (CollectionUtils.isEmpty(appResourceDos)) {
            logger.warn("empty appResources!");
            return;
        }
        appResourceDos.forEach(this::loadSingleResourceType);
    }

    /**
     * 加载单个resourceType
     *
     * @param appResourceDo
     */
    private void loadSingleResourceType(AppResourceDo appResourceDo) {
        String domainId = appResourceDo.getDomainId();
        String resourceType = appResourceDo.getResourceType();
        String uniqueId = appResourceDo.getUniqueId();
        String esIndex = appResourceDo.getEsIndex();
        if (StringUtils.isAnyBlank(domainId, resourceType, uniqueId, esIndex)) {
            logger.warn("domainId or resourceType or uniqueId  or esIndex can't be null, id={}", appResourceDo.getId());
            return;
        }

        RESOURCE_TYPE_UNIQUE_ID_MAP.put(resourceType, uniqueId);
        if (!UNIQUE_ID_RESOURCE_SERVICE_MAP.containsKey(uniqueId)) {
            IResourceService iResourceService = buildResourceServiceByUniqueId(uniqueId);
            UNIQUE_ID_RESOURCE_SERVICE_MAP.put(uniqueId, iResourceService);
        }
        if (!Constants.domainEsIndexMaps.containsKey(domainId)) {
            Constants.domainEsIndexMaps.put(domainId, esIndex);
        }
        if (!Constants.resourceTypeDomainMaps.containsKey(resourceType)) {
            Constants.resourceTypeDomainMaps.put(resourceType, domainId);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        reloadResourceTypeFromDb();
    }
}
