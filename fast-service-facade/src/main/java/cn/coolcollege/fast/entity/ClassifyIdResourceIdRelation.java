package cn.coolcollege.fast.entity;

import java.util.List;

/**
 * @author pk
 * @date 2021-06-21 22:22
 */
public class ClassifyIdResourceIdRelation {
    private String classifyId;
    private List<String> resourceIds;

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }
}
