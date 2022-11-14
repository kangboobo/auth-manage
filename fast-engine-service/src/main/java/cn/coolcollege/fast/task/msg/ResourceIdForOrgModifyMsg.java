package cn.coolcollege.fast.task.msg;

import lombok.Data;
import lombok.ToString;

/**
 * 组织部门修改资源id消息
 *
 * @author baibin
 * @date 2021/7/7 9:53
 */
@ToString(callSuper = true)
@Data
public class ResourceIdForOrgModifyMsg extends BaseTaskMsg {

    private String orgId;
    private String orgType;
    private String fromParentOrgId;
    private String toParentOrgId;

    public static class Builder {
        private ResourceIdForOrgModifyMsg msg;

        public Builder() {
            this.msg = new ResourceIdForOrgModifyMsg();
        }

        public ResourceIdForOrgModifyMsg.Builder msgId(String msgId) {
            this.msg.setMsgId(msgId);
            return this;
        }

        public ResourceIdForOrgModifyMsg.Builder appId(String appId) {
            this.msg.setAppId(appId);
            return this;
        }

        public ResourceIdForOrgModifyMsg.Builder eid(String eid) {
            this.msg.setEid(eid);
            return this;
        }

        public ResourceIdForOrgModifyMsg.Builder resourceType(String resourceType) {
            this.msg.setResourceType(resourceType);
            return this;
        }

        public ResourceIdForOrgModifyMsg.Builder orgId(String orgId) {
            this.msg.setOrgId(orgId);
            return this;
        }

        public ResourceIdForOrgModifyMsg.Builder orgType(String orgType) {
            this.msg.setOrgType(orgType);
            return this;
        }

        public ResourceIdForOrgModifyMsg.Builder fromParentOrgId(String fromParentOrgId) {
            this.msg.setFromParentOrgId(fromParentOrgId);
            return this;
        }

        public ResourceIdForOrgModifyMsg.Builder toParentOrgId(String toParentOrgId) {
            this.msg.setToParentOrgId(toParentOrgId);
            return this;
        }

        public ResourceIdForOrgModifyMsg build() {
            return this.msg;
        }
    }

}
