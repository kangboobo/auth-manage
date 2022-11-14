package cn.coolcollege.fast.task.msg;

import lombok.Data;
import lombok.ToString;

/**
 * 用户修改管辖范围计算资源id的消息
 *
 * @author pk
 */
@Data
@ToString(callSuper = true)
public class ResourceIdForAuthModifyMsg extends BaseTaskMsg {
    private String userId;

    public static class Builder {
        private ResourceIdForAuthModifyMsg msg;

        public Builder() {
            this.msg = new ResourceIdForAuthModifyMsg();
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

        public Builder userId(String userId) {
            this.msg.setUserId(userId);
            return this;
        }

        public ResourceIdForAuthModifyMsg build() {
            return this.msg;
        }
    }
}
