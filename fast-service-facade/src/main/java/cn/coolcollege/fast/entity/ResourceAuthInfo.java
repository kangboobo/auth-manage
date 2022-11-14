package cn.coolcollege.fast.entity;

import java.util.List;

/**
 * 可见范围部分的逻辑
 *
 * @author pk
 * @date 2021-06-21 17:52
 */
public class ResourceAuthInfo {
    /**
     * 资源可见性类型
     * {@link cn.coolcollege.fast.constants.ResourceVisibleTypeEnum}
     */
    private String visibleType;

    /**
     * 可见范围直接可见的用户id
     */
    private List<Long> userIds;

    /**
     * 可见范围内的直属部门id
     */
    private List<Long> departmentIds;

    /**
     * 可见范围的直属岗位(组)id
     */
    private List<Long> positionIds;

    /**
     * 可见范围的直属用户组id
     */
    private List<Long> userGroupIds;


    public String getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(String visibleType) {
        this.visibleType = visibleType;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<Long> getPositionIds() {
        return positionIds;
    }

    public void setPositionIds(List<Long> positionIds) {
        this.positionIds = positionIds;
    }

    public List<Long> getUserGroupIds() {
        return userGroupIds;
    }

    public void setUserGroupIds(List<Long> userGroupIds) {
        this.userGroupIds = userGroupIds;
    }

}