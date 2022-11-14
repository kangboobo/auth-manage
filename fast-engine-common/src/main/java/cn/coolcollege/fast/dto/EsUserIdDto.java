package cn.coolcollege.fast.dto;

import cn.coolcollege.fast.constants.ResourceAuthTypeEnum;
import lombok.Data;

/**
 * @Author bai bin
 * @Date 2021/5/19 14:52
 */
@Data
public class EsUserIdDto extends EsBaseDto {
    private String userId;

    private String resourceAuthType = ResourceAuthTypeEnum.QUERY.getValue();
}
