package cn.coolcollege.fast.entity;

import java.util.List;

/**
 * 单个分类id和所有父id的映射
 *
 * @author pk
 */
public class ResourceClassifyRelationReverse {
    /**
     * 分类id
     */
    private String classifyId;
    /**
     * 分类所有上级id
     */
    private List<String> parentIds;

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }
}
