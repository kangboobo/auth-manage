package cn.coolcollege.fast.constants;


import cn.coolcollege.fast.event.BulkLoadEvent;
import cn.coolcollege.fast.event.resource.*;
import cn.coolcollege.fast.event.org.*;
import org.apache.commons.lang3.StringUtils;

/**
 * @author bai bin
 */

public enum FastMsgTypeEnum {

    RESOURCE_CHANGE("resource_change", ResourceChangeEvent.class),
    RESOURCE_ADD("resource_add", ResourceAddEvent.class),
    RESOURCE_DELETE("resource_delete", ResourceDeleteEvent.class),
    RESOURCE_PROGRESS("resource_progress", ResourceProgressEvent.class),
    USER_ADD_ORG("user_add_org", UserAddOrgEvent.class),
    USER_LEAVE_ORG("user_leave_org", UserLeaveOrgEvent.class),
    USER_MODIFY_ORG("user_modify_org", UserModifyOrgEvent.class),
    ORG_MODIFY("org_modify", OrgModifyEvent.class),
    BULK_LOAD("bulk_load", BulkLoadEvent.class),
    COURSE_PUBLISH("course_publish", CoursePublishEvent.class),
    COURSE_TOP("course_top", CourseTopEvent.class),
    CLASSIFY_MODIFY("classify_modify", ClassifyModifyEvent.class),
    FIXED_FIELD_MODIFY("fixed_field_modify", FixedFieldModifyEvent.class),
    DATA_DICT_MODIFY("data_dict_modify", DataDictModifyEvent.class),
    AUTHORITY_RANGE_MODIFY("authority_range_modify", AuthorityRangeModifyEvent.class),
    SUPERVISOR_MODIFY("supervisor_modify", SupervisorModifyEvent.class);

    private final String msgType;
    private final Class eventClass;

    FastMsgTypeEnum(String msgType, Class eventClass) {
        this.msgType = msgType;
        this.eventClass = eventClass;
    }

    public String getMsgType() {
        return msgType;
    }

    public Class getEventClass() {
        return eventClass;
    }

    public static FastMsgTypeEnum getByValue(String msgType) {
        if (StringUtils.isBlank(msgType)) {
            return null;
        }
        for (FastMsgTypeEnum value : values()) {
            if (value.getMsgType().equals(msgType)) {
                return value;
            }
        }
        return null;
    }
}
