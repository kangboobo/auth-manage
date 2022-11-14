package cn.coolcollege.fast.task.msg;

import cn.coolcollege.fast.model.UserOriginSupervisorInfo;
import lombok.Data;
import lombok.ToString;

/**
 * 用户设置上级领导计算资源id的消息
 *
 * @author pk
 */
@Data
@ToString(callSuper = true)
public class ResourceIdForSupervisorModifyMsg extends BaseTaskMsg {
    private UserOriginSupervisorInfo userOriginSupervisorInfo;

    public static class Builder {
        private ResourceIdForSupervisorModifyMsg msg;

        public Builder() {
            this.msg = new ResourceIdForSupervisorModifyMsg();
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

        public Builder userOriginSupervisorInfo(UserOriginSupervisorInfo userOriginSupervisorInfo) {
            this.msg.setUserOriginSupervisorInfo(userOriginSupervisorInfo);
            return this;
        }

        public ResourceIdForSupervisorModifyMsg build() {
            return this.msg;
        }
    }
}
