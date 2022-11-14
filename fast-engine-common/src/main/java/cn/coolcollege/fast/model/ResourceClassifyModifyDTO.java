package cn.coolcollege.fast.model;

import lombok.Data;

import java.util.List;

@Data
public class ResourceClassifyModifyDTO extends ResourceCommonDTO{

    private String newClassifyIds;
    private List<String> resourceIds;
    private String resourceType;
}
