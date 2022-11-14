package cn.coolcollege.fast.event.resource;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.EventBuilder;
import cn.coolcollege.fast.event.ResourceCommonEvent;
import com.alibaba.fastjson.JSONObject;
import lombok.ToString;

/**
 * @author pk
 */
@ToString(callSuper = true)
public class ResourceProgressEvent extends ResourceCommonEvent {

    private final String resourceType;
    private final String resourceId;
    private final String status;
    private final String userId;

    public ResourceProgressEvent(EventBuilder builder) {
        super(builder);
        JSONObject jsonObject = builder.getJsonObject();
        this.resourceType = jsonObject.getString(Constants.resourceType);
        this.resourceId = jsonObject.getString(Constants.resourceId);
        this.status = jsonObject.getString(Constants.status);
        this.userId = jsonObject.getString(Constants.userId);
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }
}
