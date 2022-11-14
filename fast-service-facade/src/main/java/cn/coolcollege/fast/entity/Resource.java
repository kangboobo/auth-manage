package cn.coolcollege.fast.entity;

import lombok.ToString;

import java.util.List;

/**
 * @author bai bin
 */
@ToString
public class Resource {

    private String resourceId;

    private String resourceType;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源描述
     */
    private String desc;

    /**
     * 创建人用户id
     */
    private String createUserId;

    /**
     * 创建人名字
     */
    private String createUserName;

    /**
     * 资源创建时间戳
     */
    private Long createTs;

    /**
     * 资源所直属分类id
     */
    private List<Long> classifyIds;

    /**
     * 查询权限的设置信息
     */
    private ResourceAuthInfo queryAuthInfo;
    /**
     * 引用权限设置信息
     */
    private ResourceAuthInfo referAuthInfo;
    /**
     * 编辑权限设置信息
     */
    private ResourceAuthInfo editAuthInfo;

    /**
     * 业务独有的属性（比如课程的讲师, 是否是必须课程）
     */
    private String dataDict;

    public Resource() {
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Long createTs) {
        this.createTs = createTs;
    }

    public List<Long> getClassifyIds() {
        return classifyIds;
    }

    public void setClassifyIds(List<Long> classifyIds) {
        this.classifyIds = classifyIds;
    }


    public ResourceAuthInfo getQueryAuthInfo() {
        return queryAuthInfo;
    }

    public void setQueryAuthInfo(ResourceAuthInfo queryAuthInfo) {
        this.queryAuthInfo = queryAuthInfo;
    }

    public ResourceAuthInfo getReferAuthInfo() {
        return referAuthInfo;
    }

    public void setReferAuthInfo(ResourceAuthInfo referAuthInfo) {
        this.referAuthInfo = referAuthInfo;
    }

    public ResourceAuthInfo getEditAuthInfo() {
        return editAuthInfo;
    }

    public void setEditAuthInfo(ResourceAuthInfo editAuthInfo) {
        this.editAuthInfo = editAuthInfo;
    }

    public String getDataDict() {
        return dataDict;
    }

    public void setDataDict(String dataDict) {
        this.dataDict = dataDict;
    }
}
