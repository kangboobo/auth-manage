package cn.coolcollege.fast.task.msg;

import lombok.Data;
import lombok.ToString;

/**
 * bulk load 资源id消息
 *
 * @author pk
 * @date 2021-06-27 10:11
 */
@Data
@ToString(callSuper = true)
public class ResourceIdForBulkLoadMsg extends BaseTaskMsg {

    public static class Builder {
        private ResourceIdForBulkLoadMsg msg;

        public Builder() {
            this.msg = new ResourceIdForBulkLoadMsg();
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

        public ResourceIdForBulkLoadMsg build() {
            return this.msg;
        }
    }
}
