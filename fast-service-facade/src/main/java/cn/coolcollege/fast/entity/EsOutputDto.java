package cn.coolcollege.fast.entity;

import lombok.ToString;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/18 13:57
 */
@ToString
public class EsOutputDto {

    private String resourceId;
    private String resourceTitle;
    private List<String> classifyIds;
    private String desc;
    private String createUserId;
    private String createUserName;
    private Long createTs;
    private String dataDict;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public List<String> getClassifyIds() {
        return classifyIds;
    }

    public void setClassifyIds(List<String> classifyIds) {
        this.classifyIds = classifyIds;
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

    public String getDataDict() {
        return dataDict;
    }

    public void setDataDict(String dataDict) {
        this.dataDict = dataDict;
    }

}
