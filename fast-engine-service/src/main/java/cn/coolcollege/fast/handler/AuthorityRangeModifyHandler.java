package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.org.AuthorityRangeModifyEvent;
import cn.coolcollege.fast.task.msg.ResourceIdForAuthModifyMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户修改管辖范围处理
 *
 * @author pk
 */
@Service
public class AuthorityRangeModifyHandler extends AbstractEventHandler {

    @Autowired
    private CalculateServiceHelper calculateServiceHelper;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private MsgConfig msgConfig;

    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(AuthorityRangeModifyEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle authorityRangeModifyEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        AuthorityRangeModifyEvent event = (AuthorityRangeModifyEvent)baseEvent;
        if (CollectionUtils.isEmpty(event.getUserIds())) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        // 查询已接入的所有resourceType
        // 组装成 ResourceIdForAuthModifyMsg 消息
        // 发送消息
        AuthorityRangeModifyEvent event = (AuthorityRangeModifyEvent)baseEvent;

        List<String> resourceTypes = calculateServiceHelper.queryRegisterResourceTypes(event.getAppId(), null, null);
        if (CollectionUtils.isEmpty(resourceTypes)) {
            return;
        }

        jthBaseHandler.filterResourceType(event.getEid(), resourceTypes);

        resourceTypes.forEach(resourceType -> {
            event.getUserIds().forEach(userId -> {
                ResourceIdForAuthModifyMsg msg = new ResourceIdForAuthModifyMsg.Builder().msgId(event.getMsgId())
                    .appId(event.getAppId()).eid(event.getEid()).resourceType(resourceType).userId(userId).build();
                String payload = JSON.toJSONString(msg);
                logger.info("start to send idMsgQueueForAuthModify, msg={}", payload);
                jmsMessagingTemplate.convertAndSend(msgConfig.getIdMsgQueueForAuthModify(), payload);
            });
        });
    }
}
