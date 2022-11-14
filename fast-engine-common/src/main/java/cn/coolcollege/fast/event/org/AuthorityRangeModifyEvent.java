package cn.coolcollege.fast.event.org;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.EventBuilder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.ToString;

import java.util.List;

/**
 * 用户修改管辖范围事件
 *
 * @author pk
 */
@ToString(callSuper = true)
public class AuthorityRangeModifyEvent extends BaseEvent {

    private List<String> userIds;

    public AuthorityRangeModifyEvent(EventBuilder builder) {
        super(builder);
        JSONObject jsonObject = builder.getJsonObject();
        JSONArray jsonArray = jsonObject.getJSONArray(Constants.userIds);
        List<String> userIds;
        if (jsonArray != null) {
            userIds = jsonArray.toJavaList(String.class);
            this.userIds = userIds;
        }
    }

    public List<String> getUserIds() {
        return userIds;
    }
}
