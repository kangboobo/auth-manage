package cn.coolcollege.fast.event.resource;


import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.EventBuilder;
import cn.coolcollege.fast.event.ResourceCommonEvent;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.ToString;

import java.util.List;

@ToString(callSuper = true)
public class ClassifyModifyEvent extends ResourceCommonEvent {

    private final String newClassifyIds;
    private List<String> resourceIds;
    private final String resourceType;

    public ClassifyModifyEvent(EventBuilder builder) {
        super(builder);
        JSONObject jsonObject = builder.getJsonObject();
        JSONArray resourceIds = jsonObject.getJSONArray(Constants.resourceIds);
        if (resourceIds != null) {
            this.resourceIds = resourceIds.toJavaList(String.class);
        }
        this.resourceType = jsonObject.getString(Constants.resourceType);
        this.newClassifyIds = jsonObject.getString(Constants.newClassifyIds);
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public String getNewClassifyIds() {
        return newClassifyIds;
    }

    public String getResourceType() {
        return resourceType;
    }
}
