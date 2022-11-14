package cn.coolcollege.fast.model;

import lombok.Data;

import java.util.List;

@Data
public class ResourcePublishStatusDTO extends ResourceCommonDTO{
    private String publishStatus;
    private List<String> resourceIds;
    private String resourceType;
}
