package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.resource.ResourceChangeEvent;
import cn.coolcollege.fast.storage.mapper.AppResourceMapper;
import cn.coolcollege.fast.task.msg.ResourceIdForDirectExtendClassifyMsg;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
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
public class ResourceChangeHandler extends AbstractEventHandler {

    @Autowired
    private MsgConfig msgConfig;
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private CalculateServiceHelper calculateServiceHelper;
    @Autowired
    private AppResourceMapper appResourceMapper;
    @Autowired
    private JthBaseHandler jthBaseHandler;

    private static final String RESOURCE_CLASSIFY = "resource_classify";

    @Subscribe
    public void onEvent(ResourceChangeEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle resourceChangeEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        ResourceChangeEvent event = (ResourceChangeEvent)baseEvent;
        if (CollectionUtils.isEmpty(event.getResourceIds())) {
            return false;
        }
        if (StringUtils.isBlank(event.getResourceType())) {
            return false;
        }
        if (StringUtils.isBlank(event.getOperateUserId())) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        ResourceChangeEvent event = (ResourceChangeEvent)baseEvent;
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
            logger.info("start to send detailMsg, msg={}", payload);
            jmsMessagingTemplate.convertAndSend(msgConfig.getDetailMsgQueue(), payload);
        } catch (Exception e) {
            logger.error("sendDetailMsg err, msg={}", payload, e);
            return;
        }
        if (event.getResourceType().equals(RESOURCE_CLASSIFY)) {
            // 如果是资源分类修改事件, 除了计算分类本身的可见性, 还要计算跟随了分类可见性的所有类型资源
            // 1.查询注册的所有类型资源
            // 2.sendMsg
            List<String> resourceTypes = appResourceMapper.getIncludeClassifyResourceType(event.getAppId(), true);
            if (CollectionUtils.isEmpty(resourceTypes)) {
                return;
            }
            jthBaseHandler.filterResourceType(event.getEid(), resourceTypes);

            resourceTypes.forEach(resourceType -> {
                if (resourceType.equals(RESOURCE_CLASSIFY)) {
                    return;
                }
                event.getResourceIds().forEach(classifyId -> {
                    String domainId = Constants.resourceTypeDomainMaps.get(resourceType);
                    if (StringUtils.isBlank(domainId)) {
                        return;
                    }
                    ResourceIdForDirectExtendClassifyMsg msg = new ResourceIdForDirectExtendClassifyMsg.Builder()
                        .msgId(event.getMsgId()).appId(event.getAppId()).eid(event.getEid()).domainId(domainId)
                        .resourceType(resourceType).classifyId(classifyId).build();
                    try {
                        String idMsgPayload = JSON.toJSONString(msg);
                        logger.info("start to send idMsgQueueForDirectExtendClassify, msg={}", idMsgPayload);
                        jmsMessagingTemplate.convertAndSend(msgConfig.getIdMsgQueueForDirectExtendClassify(),
                            idMsgPayload);
                    } catch (Exception e) {
                        logger.error("send idTaskMsg err, msg={}", msg, e);
                    }
                });
            });
        }
    }
}
