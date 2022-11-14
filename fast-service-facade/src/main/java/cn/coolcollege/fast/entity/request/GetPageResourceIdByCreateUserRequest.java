package cn.coolcollege.fast.entity.request;

import lombok.ToString;
import net.coolcollege.platform.util.model.BasePageRequest;

import java.util.List;

/**
 * @author pk
 * @date 2021-06-28 09:56
 */
@ToString(callSuper = true)
public class GetPageResourceIdByCreateUserRequest extends BasePageRequest {
    private String resourceType;
    private List<String> userIds;

    /**
     * 资源可见性类型, 如果不传则不对它过滤 {@link cn.coolcollege.fast.constants.ResourceVisibleTypeEnum}
     * 
     */
    private String resourceVisibleType;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getResourceVisibleType() {
        return resourceVisibleType;
    }

    public void setResourceVisibleType(String resourceVisibleType) {
        this.resourceVisibleType = resourceVisibleType;
    }
}
