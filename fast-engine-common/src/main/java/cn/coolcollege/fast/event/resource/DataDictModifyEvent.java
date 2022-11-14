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
 * @date 2021-06-27 19:17
 */
@ToString(callSuper = true)
public class DataDictModifyEvent extends ResourceCommonEvent {

    private final String resourceType;
    private List<String> resourceIds;
    private final String dataDict;

    public DataDictModifyEvent(EventBuilder builder) {
        super(builder);
        JSONObject jsonObject = builder.getJsonObject();
        this.resourceType = jsonObject.getString(Constants.resourceType);
        this.dataDict = jsonObject.getString(Constants.DATA_DICT);
        JSONArray jsonArray = jsonObject.getJSONArray(Constants.resourceIds);
        if (jsonArray != null) {
            this.resourceIds = jsonArray.toJavaList(String.class);
        }
    }

    public String getResourceType() {
        return resourceType;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public String getDataDict() {
        return dataDict;
    }
}
