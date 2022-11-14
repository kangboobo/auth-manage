package cn.coolcollege.fast.event;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.constants.FastMsgTypeEnum;
import com.alibaba.fastjson.JSONObject;

/**
 * 消息基类
 *
 * @author pk
 */
public class BaseEvent {

    /**
     * 消息Id(uuid保证唯一)
     */
    private final String msgId;

    /**
     * {@link net.coolcollege.platform.util.constants.AppIdEnum}
     */
    private final String appId;

    /**
     * 企业id
     */
    private final String eid;

    /**
     * 消息类型{@link FastMsgTypeEnum}
     */
    private final String msgType;

    /**
     * 事件发生的13位时间戳
     */
    private final Long eventTs;

    /**
     * 操作用户id
     */
    private final String operateUserId;

    /**
     * 操作用户姓名
     */
    private final String operateUserName;

    public BaseEvent(EventBuilder builder) {
        JSONObject jsonObject = builder.getJsonObject();
        this.eventTs = jsonObject.getLong(Constants.eventTs);
        this.msgType = jsonObject.getString(Constants.msgType);
        this.appId = jsonObject.getString(Constants.appId);
        this.eid = jsonObject.getString(Constants.eId);
        this.msgId = jsonObject.getString(Constants.msgId);
        this.operateUserId = jsonObject.getString(Constants.operateUserId);
        this.operateUserName = jsonObject.getString(Constants.operateUserName);
    }


    public String getMsgId() {
        return msgId;
    }

    public String getAppId() {
        return appId;
    }

    public String getEid() {
        return eid;
    }

    public String getMsgType() {
        return msgType;
    }

    public Long getEventTs() {
        return eventTs;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BaseEvent{");
        sb.append("msgId='").append(msgId).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", eid='").append(eid).append('\'');
        sb.append(", msgType='").append(msgType).append('\'');
        sb.append(", eventTs=").append(eventTs);
        sb.append(", operateUserId=").append(operateUserId);
        sb.append(", operateUserName=").append(operateUserName);
        sb.append('}');
        return sb.toString();
    }
}
