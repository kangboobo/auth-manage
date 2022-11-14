package cn.coolcollege.fast.entity.result;

import cn.coolcollege.fast.entity.ResourceClassifyRelationReverse;
import lombok.ToString;
import net.coolcollege.platform.util.model.BaseResult;

import java.util.List;

/**
 * 获取某些资源分类id和上级分类id的映射
 *
 * @author pk
 */
@ToString(callSuper = true)
public class GetResourceClassifyRelationReverseListResult extends BaseResult {

    private List<ResourceClassifyRelationReverse> resourceClassifyRelationReverses;

    public List<ResourceClassifyRelationReverse> getResourceClassifyRelationReverses() {
        return resourceClassifyRelationReverses;
    }

    public void setResourceClassifyRelationReverses(List<ResourceClassifyRelationReverse> resourceClassifyRelationReverses) {
        this.resourceClassifyRelationReverses = resourceClassifyRelationReverses;
    }
}
