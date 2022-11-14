package cn.coolcollege.fast.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author pk
 * @date 2021-06-22 23:06
 */
public class LoadResourceTypeReq {

    @JSONField(name = "app_id")
    private String appId;
    @JSONField(name = "domain_id")
    private String domainId;
    @JSONField(name = "resource_type")
    private String resourceType;
    private String uniqueId;
    @JSONField(name = "es_index")
    private String esIndex;
    @JSONField(name = "include_classify")
    private Boolean includeClassify;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getEsIndex() {
        return esIndex;
    }

    public void setEsIndex(String esIndex) {
        this.esIndex = esIndex;
    }

    public Boolean getIncludeClassify() {
        return includeClassify;
    }

    public void setIncludeClassify(Boolean includeClassify) {
        this.includeClassify = includeClassify;
    }
}
