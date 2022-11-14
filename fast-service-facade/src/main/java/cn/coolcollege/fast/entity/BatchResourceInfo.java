package cn.coolcollege.fast.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/7 11:54
 */
public class BatchResourceInfo {

    private String resourceId;

    private List<String> userIds = new ArrayList<>();

    private List<String> deptIds = new ArrayList<>();

    private List<String> positionIds = new ArrayList<>();

    private List<String> userGroupIds = new ArrayList<>();

    /**
     * {@link cn.coolcollege.fast.constants.ResourceVisibleTypeEnum}
     */
    private String visibleType;

    public BatchResourceInfo() {
    }

    public BatchResourceInfo(String resourceId, List<String> userIds, List<String> deptIds, List<String> positionIds, List<String> userGroupIds) {
        this.resourceId = resourceId;
        this.userIds = userIds;
        this.deptIds = deptIds;
        this.positionIds = positionIds;
        this.userGroupIds = userGroupIds;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<String> deptIds) {
        this.deptIds = deptIds;
    }

    public List<String> getPositionIds() {
        return positionIds;
    }

    public void setPositionIds(List<String> positionIds) {
        this.positionIds = positionIds;
    }

    public List<String> getUserGroupIds() {
        return userGroupIds;
    }

    public void setUserGroupIds(List<String> userGroupIds) {
        this.userGroupIds = userGroupIds;
    }

    public String getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(String visibleType) {
        this.visibleType = visibleType;
    }

    @Override
    public String toString() {
        return "BatchResourceInfo{" +
                "resourceId='" + resourceId + '\'' +
                ", userIds=" + userIds +
                ", deptIds=" + deptIds +
                ", positionIds=" + positionIds +
                ", userGroupIds=" + userGroupIds +
                ", visibleType=" + visibleType +
                '}';
    }
}
