package cn.coolcollege.fast.entity.result;

import lombok.ToString;
import net.coolcollege.platform.util.model.BasePageResult;

import java.util.List;

/**
 * @author pk
 * @date 2021-06-26 15:11
 */
@ToString(callSuper = true)
public class GetPageDirectExtendResourceIdsByClassifyIdResult extends BasePageResult {

    private List<String> resourceIds;

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }
}
