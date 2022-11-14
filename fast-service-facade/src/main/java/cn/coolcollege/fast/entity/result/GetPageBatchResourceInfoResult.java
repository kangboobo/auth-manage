package cn.coolcollege.fast.entity.result;

import lombok.ToString;
import net.coolcollege.platform.util.model.BasePageResult;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/7 10:24
 */
@ToString(callSuper = true)
public class GetPageBatchResourceInfoResult extends BasePageResult {

    private List<String> resourceIds;

    public GetPageBatchResourceInfoResult() {
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }
}
