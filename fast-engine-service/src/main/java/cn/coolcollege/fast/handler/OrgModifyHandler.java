package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.org.OrgModifyEvent;
import cn.coolcollege.fast.task.msg.ResourceIdForOrgModifyMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author pk
 */
@Service
public class OrgModifyHandler extends AbstractEventHandler {

    @Autowired
    private CalculateServiceHelper calculateServiceHelper;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private MsgConfig msgConfig;

    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(OrgModifyEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle orgModifyEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        OrgModifyEvent event = (OrgModifyEvent)baseEvent;
        if (StringUtils.isBlank(event.getOrgType())) {
            return false;
        }
        if (StringUtils.isBlank(event.getOrgId())) {
            return false;
        }
        if (StringUtils.isBlank(event.getFromParentOrgId())) {
            return false;
        }
        if (StringUtils.isBlank(event.getToParentOrgId())) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        OrgModifyEvent event = (OrgModifyEvent)baseEvent;
        List<String> resourceTypes = calculateServiceHelper.queryRegisterResourceTypes(event.getAppId(), null, null);
        if (CollectionUtils.isEmpty(resourceTypes)) {
            return;
        }
        jthBaseHandler.filterResourceType(event.getEid(), resourceTypes);
        resourceTypes.forEach(resourceType -> {
            ResourceIdForOrgModifyMsg msg =
                new ResourceIdForOrgModifyMsg.Builder().msgId(event.getMsgId()).appId(event.getAppId())
                    .eid(event.getEid()).resourceType(resourceType).orgId(event.getOrgId()).orgType(event.getOrgType())
                    .fromParentOrgId(event.getFromParentOrgId()).toParentOrgId(event.getToParentOrgId()).build();
            try {
                String payload = JSON.toJSONString(msg);
                logger.info("start to send idMsgQueueForOrgModify, msg={}", payload);
                jmsMessagingTemplate.convertAndSend(msgConfig.getIdMsgQueueForOrgModify(), payload);
            } catch (Exception e) {
                logger.error("send idTaskMsgForOrgModify err, msg={}", msg, e);
            }
        });
    }
}
