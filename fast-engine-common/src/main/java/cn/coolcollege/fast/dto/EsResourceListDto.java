package cn.coolcollege.fast.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/14 17:32
 */
@Data
@ToString(callSuper = true)
public class EsResourceListDto extends EsBaseDto {

    List<String> resourceIds;

    public EsResourceListDto() {
    }

    public EsResourceListDto(String appId, String enterpriseId, String domainId, String resourceType, List<String> resourceIds) {
        super(appId, enterpriseId, domainId, resourceType);
        this.resourceIds = resourceIds;
    }
}
