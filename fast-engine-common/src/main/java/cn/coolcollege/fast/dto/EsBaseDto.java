package cn.coolcollege.fast.dto;

import lombok.Data;

/**
 * @Author bai bin
 * @Date 2021/5/14 16:30
 */
@Data
public class EsBaseDto {

    private String appId;

    private String enterpriseId;

    private String resourceType;

    private String domainId;

    public EsBaseDto() {
    }

    public EsBaseDto(String appId, String enterpriseId, String domainId, String resourceType) {
        this.appId = appId;
        this.enterpriseId = enterpriseId;
        this.domainId = domainId;
        this.resourceType = resourceType;
    }
}
