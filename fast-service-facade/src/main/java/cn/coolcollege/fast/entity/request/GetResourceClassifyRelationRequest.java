package cn.coolcollege.fast.entity.request;

import lombok.ToString;
import net.coolcollege.platform.util.model.BaseRequest;

import java.util.List;

/**
 * 获取资源分类id映射请求
 *
 * @author pk
 */
@ToString(callSuper = true)
public class GetResourceClassifyRelationRequest extends BaseRequest {

    /**
     * 见manage_center库的fast_engine_app_resource表
     */
    private String domainId;

    private String resourceType;
    /**
     * 分类id
     */
    private List<String> classifyIds;

    public List<String> getClassifyIds() {
        return classifyIds;
    }

    public void setClassifyIds(List<String> classifyIds) {
        this.classifyIds = classifyIds;
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
