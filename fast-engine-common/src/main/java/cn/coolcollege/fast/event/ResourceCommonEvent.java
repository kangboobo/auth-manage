package cn.coolcollege.fast.event;

import cn.coolcollege.fast.constants.Constants;

public class ResourceCommonEvent extends BaseEvent{
    /**
     * 领域
     */
    private final String domainId;

    public ResourceCommonEvent(EventBuilder builder) {
        super(builder);
        this.domainId = builder.getJsonObject().getString(Constants.domainId);
    }

    public String getDomainId() {
        return domainId;
    }

    @Override
    public String toString() {
        return "ResourceCommonEvent{" +
                "domainId='" + domainId + '\'' +
                '}';
    }
}
