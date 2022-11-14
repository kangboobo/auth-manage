package cn.coolcollege.fast.event.org;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.EventBuilder;
import cn.coolcollege.fast.model.UserOriginSupervisorInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.ToString;

import java.util.List;

/**
 * 用户设置上级领导事件
 *
 * @author pk
 */
@ToString(callSuper = true)
public class SupervisorModifyEvent extends BaseEvent {

    private List<UserOriginSupervisorInfo> users;

    public SupervisorModifyEvent(EventBuilder builder) {
        super(builder);
        JSONObject jsonObject = builder.getJsonObject();
        JSONArray jsonArray = jsonObject.getJSONArray(Constants.users);
        List<UserOriginSupervisorInfo> users;
        if (jsonArray != null) {
            users = jsonArray.toJavaList(UserOriginSupervisorInfo.class);
            this.users = users;
        }
    }

    public List<UserOriginSupervisorInfo> getUsers() {
        return users;
    }
}
