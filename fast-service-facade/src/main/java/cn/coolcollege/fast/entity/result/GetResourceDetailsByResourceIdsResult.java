package cn.coolcollege.fast.entity.result;

import cn.coolcollege.fast.entity.Resource;
import lombok.ToString;
import net.coolcollege.platform.util.model.BaseResult;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/7 17:18
 */
@ToString(callSuper = true)
public class GetResourceDetailsByResourceIdsResult extends BaseResult {

    private List<Resource> resourceList;

    public GetResourceDetailsByResourceIdsResult() {
    }

    public GetResourceDetailsByResourceIdsResult(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

}