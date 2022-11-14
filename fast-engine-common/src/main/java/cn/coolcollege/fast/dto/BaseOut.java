package cn.coolcollege.fast.dto;

import lombok.Data;
import net.coolcollege.platform.util.model.BaseResult;

@Data
public class BaseOut extends BaseResult {
    private Object data;
}
