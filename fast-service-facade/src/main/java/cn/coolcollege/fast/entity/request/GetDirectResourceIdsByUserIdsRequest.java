package cn.coolcollege.fast.entity.request;

import lombok.ToString;
import net.coolcollege.platform.util.model.BaseRequest;

import java.util.List;

/**
 * @author pk
 */
@ToString(callSuper = true)
public class GetDirectResourceIdsByUserIdsRequest extends BaseRequest {

    /**
     * 用户id
     */
    private List<String> userIds;

    /**
     * 见manage_center库的fast_engine_app_resource表
     */

    private String domainId;
    private String resourceType;

    public GetDirectResourceIdsByUserIdsRequest() {
    }

    public GetDirectResourceIdsByUserIdsRequest(List<String> userIds, String resourceType,String domainId) {
        this.userIds = userIds;
        this.resourceType = resourceType;
        this.domainId = domainId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
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
