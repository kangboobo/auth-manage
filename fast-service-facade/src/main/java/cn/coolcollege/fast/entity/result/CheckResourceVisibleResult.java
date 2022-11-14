package cn.coolcollege.fast.entity.result;

import lombok.Data;
import lombok.ToString;
import net.coolcollege.platform.util.constants.CommonConstants;
import net.coolcollege.platform.util.model.BaseResult;

/**
 * 检查某人是否可见资源的检查结果
 * 
 * @author pk
 * @date 2021-07-19 10:52 上午
 */
@Data
@ToString(callSuper = true)
public class CheckResourceVisibleResult extends BaseResult {

    /**
     * 是否检查通过("true" or "false")
     * 
     */
    private String checked;
}