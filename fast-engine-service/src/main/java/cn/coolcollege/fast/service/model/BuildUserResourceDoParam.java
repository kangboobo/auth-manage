package cn.coolcollege.fast.service.model;

import java.util.List;
import java.util.Map;

/**
 * @author pk
 * @date 2021-06-26 19:14
 */
public class BuildUserResourceDoParam {
    private String appId;
    private String resourceType;
    private Long enterpriseId;
    private AuthInfoUserMap authInfoUserMap;
    private Resource resDetail;
    private Map<String, List<String>> authoritySupervisorMap;
    private Map<Long, List<Long>> classifyIdParentIdMap;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public AuthInfoUserMap getAuthInfoUserMap() {
        return authInfoUserMap;
    }

    public void setAuthInfoUserMap(AuthInfoUserMap authInfoUserMap) {
        this.authInfoUserMap = authInfoUserMap;
    }

    public Resource getResDetail() {
        return resDetail;
    }

    public void setResDetail(Resource resDetail) {
        this.resDetail = resDetail;
    }

    public Map<String, List<String>> getAuthoritySupervisorMap() {
        return authoritySupervisorMap;
    }

    public void setAuthoritySupervisorMap(Map<String, List<String>> authoritySupervisorMap) {
        this.authoritySupervisorMap = authoritySupervisorMap;
    }

    public Map<Long, List<Long>> getClassifyIdParentIdMap() {
        return classifyIdParentIdMap;
    }

    public void setClassifyIdParentIdMap(Map<Long, List<Long>> classifyIdParentIdMap) {
        this.classifyIdParentIdMap = classifyIdParentIdMap;
    }
}
