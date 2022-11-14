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
public class ResourceAddEvent extends ResourceCommonEvent {

    private final String resourceType;
    private List<String> resourceIds;


    public ResourceAddEvent(EventBuilder builder) {
        super(builder);

        JSONObject jsonObject = builder.getJsonObject();
        this.resourceType = jsonObject.getString(Constants.resourceType);
        JSONArray resourceIds = jsonObject.getJSONArray(Constants.resourceIds);
        if (resourceIds != null) {
            this.resourceIds = resourceIds.toJavaList(String.class);
        }
    }

    public String getResourceType() {
        return resourceType;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

}
