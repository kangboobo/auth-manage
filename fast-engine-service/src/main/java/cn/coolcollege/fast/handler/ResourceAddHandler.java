package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.resource.ResourceAddEvent;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResourceAddHandler extends AbstractEventHandler {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private MsgConfig msgConfig;
    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(ResourceAddEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle resourceAddEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        ResourceAddEvent event = (ResourceAddEvent)baseEvent;
        if (CollectionUtils.isEmpty(event.getResourceIds())) {
            return false;
        }
        if (StringUtils.isBlank(event.getResourceType())) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        ResourceAddEvent event = (ResourceAddEvent)baseEvent;
        if (!jthBaseHandler.whetherOpenCourseJth(event.getEid())
            && Constants.classDomainResourceTypeLists.contains(event.getResourceType())) {
            logger.info(
                "The course group feature is not turned on or the business is not on the course group whitelist，eid = {}, resourceType ={}",
                event.getEid(), event.getResourceType());
            return;
        }
        ResourceDetailMsg detailTaskMsg = new ResourceDetailMsg.Builder().msgId(event.getMsgId())
            .appId(event.getAppId()).eid(event.getEid()).domainId(event.getDomainId())
            .resourceType(event.getResourceType()).resourceIds(event.getResourceIds()).build();

        String payload = null;
        try {
            payload = JSON.toJSONString(detailTaskMsg);
            logger.info("start send to send detailMsg, msg={}", payload);
            jmsMessagingTemplate.convertAndSend(msgConfig.getDetailMsgQueue(), payload);
        } catch (Exception e) {
            logger.error("sendDetailMsg err, msg={}", payload, e);
        }
    }
}
