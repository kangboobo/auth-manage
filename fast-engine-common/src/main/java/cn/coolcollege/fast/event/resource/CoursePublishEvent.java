package cn.coolcollege.fast.event.resource;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.EventBuilder;
import cn.coolcollege.fast.event.ResourceCommonEvent;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.ToString;

import java.util.List;

/**
 * @author pk
 */
@ToString(callSuper = true)
public class CoursePublishEvent extends ResourceCommonEvent {

    private final String publishStatus;
    private List<String> resourceIds;
    private final String resourceType;

    public CoursePublishEvent(EventBuilder builder) {
        super(builder);
        JSONObject jsonObject = builder.getJsonObject();
        this.publishStatus = jsonObject.getString(Constants.publishStatus);
        JSONArray resourceIds = jsonObject.getJSONArray(Constants.resourceIds);
        if (resourceIds != null) {
            this.resourceIds = resourceIds.toJavaList(String.class);
        }
        this.resourceType = jsonObject.getString(Constants.resourceType);
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public String getResourceType() {
        return resourceType;
    }
}
