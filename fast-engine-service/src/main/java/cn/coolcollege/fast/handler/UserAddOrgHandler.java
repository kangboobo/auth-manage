package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.org.UserAddOrgEvent;
import cn.coolcollege.fast.task.msg.ResourceIdForUserAddOrgMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author pk
 */
@Service
public class UserAddOrgHandler extends AbstractEventHandler {

    @Autowired
    private CalculateServiceHelper calculateServiceHelper;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private MsgConfig msgConfig;

    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(UserAddOrgEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle userAddOrgEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        UserAddOrgEvent event = (UserAddOrgEvent)baseEvent;
        if (CollectionUtils.isEmpty(event.getUsers())) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        UserAddOrgEvent event = (UserAddOrgEvent)baseEvent;

        Set<String> departmentIdSet = Sets.newHashSet();
        Set<String> groupIdSet = Sets.newHashSet();
        Set<String> positionIdSet = Sets.newHashSet();

        event.getUsers().forEach(user -> {
            if (CollectionUtils.isNotEmpty(user.getDepartmentIds())) {
                departmentIdSet.addAll(user.getDepartmentIds());
            }
            if (CollectionUtils.isNotEmpty(user.getGroupIds())) {
                groupIdSet.addAll(user.getGroupIds());
            }
            if (CollectionUtils.isNotEmpty(user.getPositionIds())) {
                positionIdSet.addAll(user.getPositionIds());
            }
        });
        List<String> departmentIds = Lists.newArrayList(departmentIdSet);
        List<String> groupIds = Lists.newArrayList(groupIdSet);
        List<String> positionIds = Lists.newArrayList(positionIdSet);

        List<String> resourceTypes = calculateServiceHelper.queryRegisterResourceTypes(event.getAppId(), null, null);

        jthBaseHandler.filterResourceType(event.getEid(), resourceTypes);

        resourceTypes.forEach(resourceType -> {
            ResourceIdForUserAddOrgMsg msg = new ResourceIdForUserAddOrgMsg.Builder().msgId(event.getMsgId())
                .appId(event.getAppId()).eid(event.getEid()).resourceType(resourceType).departmentIds(departmentIds)
                .groupIds(groupIds).positionIds(positionIds).build();
            try {
                String payload = JSON.toJSONString(msg);
                logger.info("start to send idMsgQueueForUserAddOrg, msg={}", payload);
                jmsMessagingTemplate.convertAndSend(msgConfig.getIdMsgQueueForUserAddOrg(), payload);
            } catch (Exception e) {
                logger.error("send idTaskMsgForOrg err, msg={}", msg, e);
            }
        });
    }
}
