package cn.coolcollege.fast.event;

import cn.coolcollege.fast.constants.Constants;
import lombok.ToString;

/**
 * @author pk
 */
@ToString(callSuper = true)
public class BulkLoadEvent extends ResourceCommonEvent {

    private final String resourceType;

    public BulkLoadEvent(EventBuilder builder) {
        super(builder);
        this.resourceType = builder.getJsonObject().getString(Constants.resourceType);
    }

    public String getResourceType() {
        return resourceType;
    }
}
