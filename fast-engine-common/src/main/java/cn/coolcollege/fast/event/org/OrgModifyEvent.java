package cn.coolcollege.fast.event.org;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.EventBuilder;
import com.alibaba.fastjson.JSONObject;
import lombok.ToString;

/**
 * @author pk
 */
@ToString(callSuper = true)
public class OrgModifyEvent extends BaseEvent {
    private final String orgType;
    private final String orgId;
    private final String fromParentOrgId;
    private final String toParentOrgId;

    public OrgModifyEvent(EventBuilder builder) {
        super(builder);
        JSONObject jsonObject = builder.getJsonObject();
        this.orgType = jsonObject.getString(Constants.orgType);
        this.orgId = jsonObject.getString(Constants.orgId);
        this.fromParentOrgId = jsonObject.getString(Constants.fromParentOrgId);
        this.toParentOrgId = jsonObject.getString(Constants.toParentOrgId);
    }

    public String getOrgType() {
        return orgType;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getFromParentOrgId() {
        return fromParentOrgId;
    }

    public String getToParentOrgId() {
        return toParentOrgId;
    }
}
