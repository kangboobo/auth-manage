package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.org.UserModifyOrgEvent;
import cn.coolcollege.fast.task.msg.ResourceIdForUserModifyOrgMsg;
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
public class UserModifyOrgHandler extends AbstractEventHandler {

    @Autowired
    private CalculateServiceHelper calculateServiceHelper;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private MsgConfig msgConfig;

    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(UserModifyOrgEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle userModifyOrgEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        UserModifyOrgEvent event = (UserModifyOrgEvent)baseEvent;
        String orgType = event.getOrgType();
        if (StringUtils.isBlank(orgType)) {
            return false;
        }
        if (StringUtils.isBlank(event.getUserId())) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        UserModifyOrgEvent event = (UserModifyOrgEvent)baseEvent;
        List<String> resourceTypes = calculateServiceHelper.queryRegisterResourceTypes(event.getAppId(), null, null);
        if (CollectionUtils.isEmpty(resourceTypes)) {
            return;
        }
        jthBaseHandler.filterResourceType(event.getEid(), resourceTypes);
        resourceTypes.forEach(resourceType -> {
            ResourceIdForUserModifyOrgMsg msg = new ResourceIdForUserModifyOrgMsg.Builder().msgId(event.getMsgId())
                .appId(event.getAppId()).eid(event.getEid()).resourceType(resourceType).orgType(event.getOrgType())
                .userId(event.getUserId()).addOrgIds(event.getAddOrgIds()).removeOrgIds(event.getRemoveOrgIds())
                .build();

            try {
                String payload = JSON.toJSONString(msg);
                logger.info("start to send idMsgQueueForUserModifyOrg, msg={}", payload);
                jmsMessagingTemplate.convertAndSend(msgConfig.getIdMsgQueueForUserModifyOrg(), payload);
            } catch (Exception e) {
                logger.error("send idTaskMsgForUserModifyOrg err, msg={}", msg, e);
            }
        });
    }
}
