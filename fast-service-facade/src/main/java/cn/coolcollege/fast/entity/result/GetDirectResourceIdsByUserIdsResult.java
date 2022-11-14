package cn.coolcollege.fast.entity.result;

import lombok.ToString;
import net.coolcollege.platform.util.model.BaseResult;

import java.util.List;

/**
 * @author pk
 */
@ToString(callSuper = true)
public class GetDirectResourceIdsByUserIdsResult extends BaseResult {

    private List<String> resourceIds;

    public GetDirectResourceIdsByUserIdsResult() {
    }

    public GetDirectResourceIdsByUserIdsResult(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

}
