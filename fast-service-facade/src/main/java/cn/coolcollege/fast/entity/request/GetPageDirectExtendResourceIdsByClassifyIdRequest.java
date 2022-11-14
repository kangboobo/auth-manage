package cn.coolcollege.fast.entity.request;

import lombok.ToString;
import net.coolcollege.platform.util.model.BasePageRequest;
import net.coolcollege.platform.util.model.BaseRequest;

import java.util.List;

/**
 * 查询直接挂在分类下的且跟随分类可见性的资源id 请求参数
 *
 * @author pk
 * @date 2021-06-21 22:15
 */
@ToString(callSuper = true)
public class GetPageDirectExtendResourceIdsByClassifyIdRequest extends BasePageRequest {
    private String classifyId;
    private String resourceType;

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}