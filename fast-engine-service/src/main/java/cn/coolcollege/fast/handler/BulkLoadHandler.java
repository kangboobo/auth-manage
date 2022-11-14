package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.BulkLoadEvent;
import cn.coolcollege.fast.task.msg.ResourceIdForBulkLoadMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pk
 */
@Service
public class BulkLoadHandler extends AbstractEventHandler {

    @Autowired
    private MsgConfig msgConfig;

    @Autowired
    private CalculateServiceHelper calculateServiceHelper;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(BulkLoadEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle bulkLoadEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        BulkLoadEvent event = (BulkLoadEvent)baseEvent;
        List<String> resourceTypes = calculateServiceHelper.queryRegisterResourceTypes(event.getAppId(),
            event.getDomainId(), event.getResourceType());
        if (CollectionUtils.isEmpty(resourceTypes)) {
            return;
        }
        jthBaseHandler.filterResourceType(event.getEid(), resourceTypes);
        resourceTypes.forEach(resourceType -> {
            ResourceIdForBulkLoadMsg msg =
                new ResourceIdForBulkLoadMsg.Builder().msgId(event.getMsgId()).appId(event.getAppId())
                    .eid(event.getEid()).domainId(event.getDomainId()).resourceType(resourceType).build();
            try {
                String payload = JSON.toJSONString(msg);
                logger.info("start to send idMsgQueueForBulkLoad, msg={}", payload);
                jmsMessagingTemplate.convertAndSend(msgConfig.getIdMsgQueueForBulkLoad(), payload);
            } catch (Exception e) {
                logger.error("send idTaskMsgForBulkLoad err, msg={}", msg, e);
            }
        });
    }
}
