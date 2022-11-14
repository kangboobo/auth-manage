package cn.coolcollege.fast.event.org;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.EventBuilder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.ToString;

import java.util.List;

/**
 * @author pk
 */
@ToString(callSuper = true)
public class UserModifyOrgEvent extends BaseEvent {

    private final String orgType;
    private final String userId;
    private List<String> addOrgIds;
    private List<String> removeOrgIds;

    public UserModifyOrgEvent(EventBuilder builder) {
        super(builder);
        JSONObject jsonObject = builder.getJsonObject();
        this.orgType = jsonObject.getString(Constants.orgType);
        this.userId = jsonObject.getString(Constants.userId);

        JSONArray addOrgIds = jsonObject.getJSONArray(Constants.addOrgIds);
        if(addOrgIds != null){
            this.addOrgIds = addOrgIds.toJavaList(String.class);
        }
        JSONArray removeOrgIds = jsonObject.getJSONArray(Constants.removeOrgIds);
        if(removeOrgIds != null){
            this.removeOrgIds = removeOrgIds.toJavaList(String.class);
        }
    }

    public String getOrgType() {
        return orgType;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getAddOrgIds() {
        return addOrgIds;
    }

    public List<String> getRemoveOrgIds() {
        return removeOrgIds;
    }
}
