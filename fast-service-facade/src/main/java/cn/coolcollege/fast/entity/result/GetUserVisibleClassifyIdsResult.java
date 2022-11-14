package cn.coolcollege.fast.entity.result;

import lombok.ToString;
import net.coolcollege.platform.util.model.BaseResult;
import java.util.Set;

/**
 * @author pk
 */
@ToString(callSuper = true)
public class GetUserVisibleClassifyIdsResult extends BaseResult {

    /**
     * 所有可见的分类id
     */
    private Set<String> classifyIds;
    /**
     * 所有可见分类的上级分类id
     */
    private Set<String> parentClassifyIds;

    public Set<String> getClassifyIds() {
        return classifyIds;
    }

    public void setClassifyIds(Set<String> classifyIds) {
        this.classifyIds = classifyIds;
    }

    public Set<String> getParentClassifyIds() {
        return parentClassifyIds;
    }

    public void setParentClassifyIds(Set<String> parentClassifyIds) {
        this.parentClassifyIds = parentClassifyIds;
    }
}
