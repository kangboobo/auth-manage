package cn.coolcollege.fast.entity.request;

import lombok.ToString;
import net.coolcollege.platform.util.model.BaseRequest;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/7 17:08
 */
@ToString(callSuper = true)
public class GetResourceDetailsByResourceIdsRequest extends BaseRequest {

    private List<String> resourceIds;
    /**
     * 见manage_center库的fast_engine_app_resource表
     */
    private String domainId;

    private String resourceType;

    public GetResourceDetailsByResourceIdsRequest() {
    }

    public GetResourceDetailsByResourceIdsRequest(List<String> resourceIds, String resourceType, String domainId) {
        this.resourceIds = resourceIds;
        this.resourceType = resourceType;
        this.domainId = domainId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

}
