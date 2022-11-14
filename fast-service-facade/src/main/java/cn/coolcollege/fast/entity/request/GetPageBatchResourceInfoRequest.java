package cn.coolcollege.fast.entity.request;

import lombok.ToString;
import net.coolcollege.platform.util.model.BasePageRequest;

/**
 * @Author bai bin
 * @Date 2021/5/7 10:35
 */
@ToString(callSuper = true)
public class GetPageBatchResourceInfoRequest extends BasePageRequest {
    /**
     * 见manage_center库的fast_engine_app_resource表
     */
    private String domainId;

    private String resourceType;

    public GetPageBatchResourceInfoRequest() {
    }

    public GetPageBatchResourceInfoRequest(String resourceType, String domainId) {
        this.resourceType = resourceType;
        this.domainId = domainId;
    }


    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

}
