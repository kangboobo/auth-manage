package cn.coolcollege.fast.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author pk
 * @date 2021-06-23 20:38
 */
@Component
@ConfigurationProperties(prefix = "fast.engine.msg")
public class MsgConfig {

    private String idMsgQueueForBulkLoad;
    private String idMsgQueueForUserAddOrg;
    private String idMsgQueueForUserModifyOrg;
    private String idMsgQueueForOrgModify;
    private String idMsgQueueForAuthModify;
    private String idMsgQueueForSupervisorModify;
    private String idMsgQueueForDirectExtendClassify;
    private String detailMsgQueue;

    public String getIdMsgQueueForBulkLoad() {
        return idMsgQueueForBulkLoad;
    }

    public void setIdMsgQueueForBulkLoad(String idMsgQueueForBulkLoad) {
        this.idMsgQueueForBulkLoad = idMsgQueueForBulkLoad;
    }


    public String getIdMsgQueueForUserAddOrg() {
        return idMsgQueueForUserAddOrg;
    }

    public void setIdMsgQueueForUserAddOrg(String idMsgQueueForUserAddOrg) {
        this.idMsgQueueForUserAddOrg = idMsgQueueForUserAddOrg;
    }

    public String getIdMsgQueueForUserModifyOrg() {
        return idMsgQueueForUserModifyOrg;
    }

    public void setIdMsgQueueForUserModifyOrg(String idMsgQueueForUserModifyOrg) {
        this.idMsgQueueForUserModifyOrg = idMsgQueueForUserModifyOrg;
    }

    public String getIdMsgQueueForOrgModify() {
        return idMsgQueueForOrgModify;
    }

    public void setIdMsgQueueForOrgModify(String idMsgQueueForOrgModify) {
        this.idMsgQueueForOrgModify = idMsgQueueForOrgModify;
    }

    public String getIdMsgQueueForAuthModify() {
        return idMsgQueueForAuthModify;
    }

    public void setIdMsgQueueForAuthModify(String idMsgQueueForAuthModify) {
        this.idMsgQueueForAuthModify = idMsgQueueForAuthModify;
    }

    public String getIdMsgQueueForSupervisorModify() {
        return idMsgQueueForSupervisorModify;
    }

    public void setIdMsgQueueForSupervisorModify(String idMsgQueueForSupervisorModify) {
        this.idMsgQueueForSupervisorModify = idMsgQueueForSupervisorModify;
    }

    public String getIdMsgQueueForDirectExtendClassify() {
        return idMsgQueueForDirectExtendClassify;
    }

    public void setIdMsgQueueForDirectExtendClassify(String idMsgQueueForDirectExtendClassify) {
        this.idMsgQueueForDirectExtendClassify = idMsgQueueForDirectExtendClassify;
    }

    public String getDetailMsgQueue() {
        return detailMsgQueue;
    }

    public void setDetailMsgQueue(String detailMsgQueue) {
        this.detailMsgQueue = detailMsgQueue;
    }
}
