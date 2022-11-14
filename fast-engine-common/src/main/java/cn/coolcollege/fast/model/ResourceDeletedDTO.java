package cn.coolcollege.fast.model;

import lombok.Data;

import java.util.List;

/**
 * @author majian
 * DTO Resource which is deleted
 */

@Data
public class ResourceDeletedDTO extends ResourceCommonDTO{
    String resourceType;
    List<String> resourceIds;
}
