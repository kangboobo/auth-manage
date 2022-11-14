package cn.coolcollege.fast.task.msg;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 用户修改组织部门资源id消息
 * 
 * @author baibin
 * @date 2021-07-06 14:38
 */
@ToString(callSuper = true)
@Data
public class ResourceIdForUserModifyOrgMsg extends BaseTaskMsg {

    String orgType;

    String userId;

    List<String> addOrgIds;

    List<String> removeOrgIds;

    public static class Builder {
        private ResourceIdForUserModifyOrgMsg msg;

        public Builder() {
            this.msg = new ResourceIdForUserModifyOrgMsg();
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

        public Builder resourceType(String resourceType) {
            this.msg.setResourceType(resourceType);
            return this;
        }

        public Builder orgType(String orgType) {
            this.msg.setOrgType(orgType);
            return this;
        }

        public Builder userId(String userId) {
            this.msg.setUserId(userId);
            return this;
        }

        public Builder addOrgIds(List<String> addOrgIds) {
            this.msg.setAddOrgIds(addOrgIds);
            return this;
        }

        public Builder removeOrgIds(List<String> removeOrgIds) {
            this.msg.setRemoveOrgIds(removeOrgIds);
            return this;
        }

        public ResourceIdForUserModifyOrgMsg build() {
            return this.msg;
        }
    }
}
