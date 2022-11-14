package cn.coolcollege.fast.task.msg;

import lombok.Data;
import lombok.ToString;

/**
 * 直接继承分类的资源id消息
 *
 * @author pk
 * @date 2021-06-27 10:11
 */
@Data
@ToString(callSuper = true)
public class ResourceIdForDirectExtendClassifyMsg extends BaseTaskMsg {

    private String classifyId;

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public static class Builder {
        private ResourceIdForDirectExtendClassifyMsg msg;

        public Builder() {
            this.msg = new ResourceIdForDirectExtendClassifyMsg();
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

        public Builder classifyId(String classifyId) {
            this.msg.setClassifyId(classifyId);
            return this;
        }

        public ResourceIdForDirectExtendClassifyMsg build() {
            return this.msg;
        }
    }
}
