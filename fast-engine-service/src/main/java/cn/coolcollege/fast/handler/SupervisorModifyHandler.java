package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.org.SupervisorModifyEvent;
import cn.coolcollege.fast.model.UserOriginSupervisorInfo;
import cn.coolcollege.fast.task.msg.ResourceIdForSupervisorModifyMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author pk
 */
@Component
public class SupervisorModifyHandler extends AbstractEventHandler {

    @Autowired
    private MsgConfig msgConfig;
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private CalculateServiceHelper calculateServiceHelper;
    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(SupervisorModifyEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle supervisorModifyEvent , event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        SupervisorModifyEvent event = (SupervisorModifyEvent)baseEvent;
        if (CollectionUtils.isEmpty(event.getUsers())) {
            return false;
        }
        for (UserOriginSupervisorInfo user : event.getUsers()) {
            String userId = user.getUserId();
            if (StringUtils.isBlank(userId)) {
                return false;
            }
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        SupervisorModifyEvent event = (SupervisorModifyEvent)baseEvent;
        // 查询已接入的所有resourceType
        // 组装消息
        // 发送消息
        List<String> resourceTypes = calculateServiceHelper.queryRegisterResourceTypes(event.getAppId(), null, null);
        if (CollectionUtils.isEmpty(resourceTypes)) {
            return;
        }

        jthBaseHandler.filterResourceType(event.getEid(), resourceTypes);

        resourceTypes.forEach(resourceType -> {
            event.getUsers().forEach(userOriginSupervisorInfo -> {
                ResourceIdForSupervisorModifyMsg msg = new ResourceIdForSupervisorModifyMsg.Builder()
                    .msgId(event.getMsgId()).appId(event.getAppId()).eid(event.getEid()).resourceType(resourceType)
                    .userOriginSupervisorInfo(userOriginSupervisorInfo).build();
                String payload = JSON.toJSONString(msg);
                logger.info("start to send idMsgQueueForSupervisorModify, msg={}", payload);
                jmsMessagingTemplate.convertAndSend(msgConfig.getIdMsgQueueForSupervisorModify(), payload);
            });
        });
    }
}
