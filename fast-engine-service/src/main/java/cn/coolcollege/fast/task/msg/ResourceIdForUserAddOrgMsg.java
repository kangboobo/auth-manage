package cn.coolcollege.fast.task.msg;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 用户新增事件资源id消息
 *
 * @author pk
 * @date 2021-06-28 11:51
 */
@Data
@ToString(callSuper = true)
public class ResourceIdForUserAddOrgMsg extends BaseTaskMsg {

    List<String> departmentIds;

    List<String> groupIds;

    List<String> positionIds;

    public static class Builder {

        private ResourceIdForUserAddOrgMsg msg;

        public Builder() {
            this.msg = new ResourceIdForUserAddOrgMsg();
        }

        public Builder msgId(String msgId) {
            this.msg.setMsgId(msgId);
            return this;
        }

        public Builder appId(String appId) {
            this.msg.setAppId(appId);
            return this;
        }

        public Builder eid(String eid) {
            this.msg.setEid(eid);
            return this;
        }

        public Builder domainId(String domainId) {
            this.msg.setDomainId(domainId);
            return this;
        }

        public Builder resourceType(String resourceType) {
            this.msg.setResourceType(resourceType);
            return this;
        }

        public Builder departmentIds(List<String> departmentIds) {
            this.msg.setDepartmentIds(departmentIds);
            return this;
        }

        public Builder groupIds(List<String> groupIds) {
            this.msg.setGroupIds(groupIds);
            return this;
        }

        public Builder positionIds(List<String> positionIds) {
            this.msg.setPositionIds(positionIds);
            return this;
        }

        public ResourceIdForUserAddOrgMsg build() {
            return this.msg;
        }

    }
}