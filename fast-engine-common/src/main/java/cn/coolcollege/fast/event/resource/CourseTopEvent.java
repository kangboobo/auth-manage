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
public class CourseTopEvent extends ResourceCommonEvent {
    private List<String> resourceIds;
    private final String resourceType;
    private final Long sortNo;
    private final Integer isRecommend;

    public CourseTopEvent(EventBuilder builder) {
        super(builder);
        JSONObject jsonObject = builder.getJsonObject();
        JSONArray resourceIds = jsonObject.getJSONArray(Constants.resourceIds);
        if (resourceIds != null) {
            this.resourceIds = resourceIds.toJavaList(String.class);
        }
        this.resourceType = jsonObject.getString(Constants.resourceType);
        this.sortNo = jsonObject.getLong(Constants.sortNo);
        this.isRecommend = jsonObject.getInteger(Constants.isRecommend);
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public String getResourceType() {
        return resourceType;
    }

    public Long getSortNo() {
        return sortNo;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }
}
