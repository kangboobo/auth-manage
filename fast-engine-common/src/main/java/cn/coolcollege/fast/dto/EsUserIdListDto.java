package cn.coolcollege.fast.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/14 18:29
 */
@Data
public class EsUserIdListDto extends EsCommonDto {

    private List<String> userIdList;

    public EsUserIdListDto() {

    }

    public EsUserIdListDto(String appId, String enterpriseId, String domainId, String resourceType, String resourceId, List<String> userIdList) {
        super(appId, enterpriseId, domainId, resourceType, resourceId);
        this.userIdList = userIdList;
    }
}
