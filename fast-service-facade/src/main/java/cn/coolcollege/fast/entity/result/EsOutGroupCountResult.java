package cn.coolcollege.fast.entity.result;

import lombok.ToString;
import net.coolcollege.platform.util.model.BaseResult;
import java.util.Map;

/**
 * @Author bai bin
 * @Date 2021/5/7 20:13
 */
@ToString(callSuper = true)
public class EsOutGroupCountResult extends BaseResult {

    /**
     * 总数
     */
    private Long total;

    /**
     * 分组类型数量统计
     */
    private Map<String, Long> groupCountMap;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Map<String, Long> getGroupCountMap() {
        return groupCountMap;
    }

    public void setGroupCountMap(Map<String, Long> groupCountMap) {
        this.groupCountMap = groupCountMap;
    }
}
