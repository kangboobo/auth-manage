package cn.coolcollege.fast.entity.request;

import lombok.ToString;
import net.coolcollege.platform.util.model.BaseRequest;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/7 17:46
 */
@ToString(callSuper = true)
public class GetDirectResourceIdsByOrgIdsRequest extends BaseRequest {

    /**
     * orgIds可以为departmentIds、userGroupIds、positionIds
     */
    private List<String> orgIds;

    /**
     * 见manage_center库的fast_engine_app_resource表
     */
    private String domainId;

    private String resourceType;

    public GetDirectResourceIdsByOrgIdsRequest() {
    }

    public GetDirectResourceIdsByOrgIdsRequest(List<String> orgIds, String resourceType, String domainId) {
        this.orgIds = orgIds;
        this.resourceType = resourceType;
        this.domainId = domainId;
    }

    public List<String> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(List<String> orgIds) {
        this.orgIds = orgIds;
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

}
