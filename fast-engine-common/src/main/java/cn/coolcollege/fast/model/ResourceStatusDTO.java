package cn.coolcollege.fast.model;

import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * @author majian
 */

@Data
public class ResourceStatusDTO extends ResourceCommonDTO{
    @NotNull
    String resourceId;

    String resourceType;

    String status;

    String userId;
}
