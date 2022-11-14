package cn.coolcollege.fast.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * @Author bai bin
 * @Date 2021/5/28 10:55
 */
@Data
@ToString(callSuper = true)
public class EsFieldsDto extends EsResourceListDto {

    private Map<String, Object> paramsMap;

}
