package cn.coolcollege.fast.entity.request;

import lombok.ToString;
import net.coolcollege.platform.util.model.BaseRequest;

/**
 * 查询继承了某个分类的所有分类id 请求参数
 *
 * @author pk
 * @date 2021-06-21 19:50
 */
@ToString(callSuper = true)
public class GetExtendClassifyIdsRequest extends BaseRequest {
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
