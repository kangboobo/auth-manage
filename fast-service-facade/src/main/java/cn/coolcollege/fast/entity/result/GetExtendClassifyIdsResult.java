package cn.coolcollege.fast.entity.result;

import lombok.ToString;
import net.coolcollege.platform.util.model.BaseResult;

import java.util.List;

/**
 * 查询继承了某个分类的所有分类id 返回值
 *
 * @author pk
 * @date 2021-06-21 19:46
 */
@ToString(callSuper = true)
public class GetExtendClassifyIdsResult extends BaseResult {
    private List<String> classifyIds;

    public List<String> getClassifyIds() {
        return classifyIds;
    }

    public void setClassifyIds(List<String> classifyIds) {
        this.classifyIds = classifyIds;
    }
}
