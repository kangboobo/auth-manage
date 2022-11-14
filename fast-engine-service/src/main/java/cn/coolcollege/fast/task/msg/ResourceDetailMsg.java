package cn.coolcollege.fast.task.msg;


import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 资源详情的消息
 *
 * @author pk
 * @date 2021-06-23 20:15
 */
@Data
@ToString(callSuper = true)
public class ResourceDetailMsg extends BaseTaskMsg {
    private List<String> resourceIds;

    public static class Builder {
        private ResourceDetailMsg msg;

        public Builder() {
            this.msg = new ResourceDetailMsg();
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

        public Builder resourceIds(List<String> resourceIds) {
            this.msg.setResourceIds(resourceIds);
            return this;
        }

        public ResourceDetailMsg build() {
            return this.msg;
        }
    }
}
