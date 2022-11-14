package cn.coolcollege.fast.service.model;

import java.util.Map;
import java.util.Set;

/**
 * @author pk
 * @date 2021-06-26 19:14
 */
public class AuthInfoUserMap {
    private Map<Long, Set<Long>> deptUserIdMap;
    private Map<Long, Set<Long>> groupUserIdMap;
    private Map<Long, Set<Long>> positionUserIdMap;

    public Map<Long, Set<Long>> getDeptUserIdMap() {
        return deptUserIdMap;
    }

    public void setDeptUserIdMap(Map<Long, Set<Long>> deptUserIdMap) {
        this.deptUserIdMap = deptUserIdMap;
    }

    public Map<Long, Set<Long>> getGroupUserIdMap() {
        return groupUserIdMap;
    }

    public void setGroupUserIdMap(Map<Long, Set<Long>> groupUserIdMap) {
        this.groupUserIdMap = groupUserIdMap;
    }

    public Map<Long, Set<Long>> getPositionUserIdMap() {
        return positionUserIdMap;
    }

    public void setPositionUserIdMap(Map<Long, Set<Long>> positionUserIdMap) {
        this.positionUserIdMap = positionUserIdMap;
    }
}
