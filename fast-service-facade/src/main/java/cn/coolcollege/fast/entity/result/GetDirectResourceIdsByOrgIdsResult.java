package cn.coolcollege.fast.entity.result;

import lombok.ToString;
import net.coolcollege.platform.util.model.BaseResult;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/7 17:42
 */
@ToString(callSuper = true)
public class GetDirectResourceIdsByOrgIdsResult extends BaseResult {

    private List<String> resourceIds;

    public GetDirectResourceIdsByOrgIdsResult() {
    }

    public GetDirectResourceIdsByOrgIdsResult(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }
}
