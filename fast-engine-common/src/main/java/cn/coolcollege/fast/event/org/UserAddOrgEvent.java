package cn.coolcollege.fast.event.org;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.EventBuilder;
import cn.coolcollege.fast.model.User;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.ToString;

import java.util.List;

/**
 * @author pk
 */
@ToString(callSuper = true)
public class UserAddOrgEvent extends BaseEvent {

    private List<User> users;

    public UserAddOrgEvent(EventBuilder builder) {
        super(builder);
        JSONObject jsonObject = builder.getJsonObject();
        JSONArray jsonArray = jsonObject.getJSONArray(Constants.users);

        if (jsonArray != null) {
            users = Lists.newArrayList();
            for (Object obj : jsonArray) {
                User userWithOrg = new User();
                JSONObject jsb = (JSONObject) obj;
                userWithOrg.setId(jsb.getString(Constants.id));
                JSONArray deptIdArray = jsb.getJSONArray(Constants.departmentIds);
                if (deptIdArray != null) {
                    userWithOrg.setDepartmentIds(deptIdArray.toJavaList(String.class));
                }
                JSONArray groupArray = jsb.getJSONArray(Constants.groupIds);
                if (groupArray != null) {
                    userWithOrg.setGroupIds(groupArray.toJavaList(String.class));
                }
                JSONArray positionArray = jsb.getJSONArray(Constants.positionIds);
                if (positionArray != null) {
                    userWithOrg.setPositionIds(positionArray.toJavaList(String.class));
                }
                users.add(userWithOrg);
            }
        }
    }

    public List<User> getUsers() {
        return users;
    }
}
