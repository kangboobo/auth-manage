package cn.coolcollege.fast.model;

import lombok.Data;

import java.util.List;

@Data
public class ResourceTopDTO extends ResourceCommonDTO {
    private List<String> resourceIds;
    private String resourceType;
    private Long sortNO;
    private int isRecommend;
}
