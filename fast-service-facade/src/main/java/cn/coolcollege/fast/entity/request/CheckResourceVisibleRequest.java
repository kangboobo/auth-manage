package cn.coolcollege.fast.entity.request;

import lombok.Data;
import lombok.ToString;
import net.coolcollege.platform.util.model.BaseRequest;

/**
 * @author pk
 * @date 2021-07-19 10:55 上午
 */
@Data
@ToString(callSuper = true)
public class CheckResourceVisibleRequest extends BaseRequest {

    private String userId;

    /**
     * 资源所属的domainId
     */
    private String domainId;

    private String resourceType;

    private String resourceId;
    /**
     * 资源权限类型,{@link cn.coolcollege.fast.constants.ResourceAuthTypeEnum}
     */
    private String resourceAuthType;
}
