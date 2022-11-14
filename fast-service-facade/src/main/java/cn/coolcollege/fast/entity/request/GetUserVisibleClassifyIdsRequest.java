package cn.coolcollege.fast.entity.request;

import cn.coolcollege.fast.constants.ResourceAuthTypeEnum;
import lombok.ToString;
import net.coolcollege.platform.util.model.BaseRequest;

/**
 * @author pk
 */
@ToString(callSuper = true)
public class GetUserVisibleClassifyIdsRequest extends BaseRequest {

    private String userId;

    private String domainId;

    private String resourceType;

    /**
     * 资源权限类型 {@link cn.coolcollege.fast.constants.ResourceAuthTypeEnum}
     */
    private String resourceAuthType = ResourceAuthTypeEnum.QUERY.getValue();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getResourceAuthType() {
        return resourceAuthType;
    }

    public void setResourceAuthType(String resourceAuthType) {
        this.resourceAuthType = resourceAuthType;
    }
}
