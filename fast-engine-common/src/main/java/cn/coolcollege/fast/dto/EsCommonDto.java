package cn.coolcollege.fast.dto;

import lombok.Data;

/**
 * @Author bai bin
 * @Date 2021/5/12 19:13
 */
@Data
public class EsCommonDto extends EsBaseDto {

    private String resourceId;

    public EsCommonDto() {
    }

    public EsCommonDto(String appId, String enterpriseId, String domainId, String resourceType, String resourceId) {
        super(appId, enterpriseId, domainId, resourceType);
        this.resourceId = resourceId;
    }
}
