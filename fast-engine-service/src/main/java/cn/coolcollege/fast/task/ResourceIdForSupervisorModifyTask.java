package cn.coolcollege.fast.task;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsMessagingTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;

import cn.coolcollege.fast.SpringContextUtil;
import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.constants.ResourceVisibleTypeEnum;
import cn.coolcollege.fast.model.UserOriginSupervisorInfo;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
import cn.coolcollege.fast.task.msg.ResourceIdForSupervisorModifyMsg;

/**
 * 计算某人修改上级领导涉及的资源id的任务
 *
 * @author pk
 */
public class ResourceIdForSupervisorModifyTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ResourceIdForSupervisorModifyTask.class);

    private ResourceIdForSupervisorModifyMsg msg;

    public ResourceIdForSupervisorModifyTask(ResourceIdForSupervisorModifyMsg msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        MDC.put(Constants.MSG_ID, msg.getMsgId());
        // 1.获取某人创建的所有资源id
        // 2.获取某人原来和现在所有上级领导创建的所有选择了"管辖范围及下属"的资源id
        // 3.发送到detailMsgQueue
        CalculateServiceHelper calculateServiceHelper =
            (CalculateServiceHelper)SpringContextUtil.getBean("calculateServiceHelper", CalculateServiceHelper.class);

        // 被修改人创建的所有资源id
        UserOriginSupervisorInfo userOriginSupervisorInfo = msg.getUserOriginSupervisorInfo();
        String userId = userOriginSupervisorInfo.getUserId();
        List<String> createResourceIds = calculateServiceHelper.queryResourceIdsByCreateUsers(msg.getAppId(),
            msg.getEid(), msg.getResourceType(), Lists.newArrayList(userId), null);

        // 被修改人的所有现在和过去的上级领导创建的所有选择了"管辖范围及下属"的资源id
        List<String> allSupervisorIds = calculateServiceHelper.queryAllSupervisorIdsWithOrigin(msg.getAppId(),
            msg.getEid(), userOriginSupervisorInfo);
        List<String> authWithSubResourceIds = calculateServiceHelper.queryResourceIdsByCreateUsers(msg.getAppId(),
            msg.getEid(), msg.getResourceType(), allSupervisorIds, ResourceVisibleTypeEnum.AUTH_WITH_SUB);

        Set<String> allResourceIds = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(createResourceIds)) {
            allResourceIds.addAll(createResourceIds);
        }
        if (CollectionUtils.isNotEmpty(authWithSubResourceIds)) {
            allResourceIds.addAll(authWithSubResourceIds);
        }
        if (CollectionUtils.isEmpty(allResourceIds)) {
            logger.warn("emptyResourceIds, msg={}", msg);
            return;
        }
        JmsMessagingTemplate jmsTemplate =
                (JmsMessagingTemplate)SpringContextUtil.getBean("jmsMessagingTemplate", JmsMessagingTemplate.class);
        MsgConfig msgConfig = (MsgConfig)SpringContextUtil.getBean("msgConfig", MsgConfig.class);

        ResourceDetailMsg detailTaskMsg = new ResourceDetailMsg.Builder().msgId(msg.getMsgId()).appId(msg.getAppId())
            .eid(msg.getEid()).domainId(msg.getDomainId()).resourceType(msg.getResourceType()).build();

        Lists.partition(Lists.newArrayList(allResourceIds), Constants.ES_BULK_LOAD_ADD_SIZE).forEach(partResourceIds -> {
            detailTaskMsg.setResourceIds(partResourceIds);
            String payload;
            try {
                payload = JSON.toJSONString(detailTaskMsg);
                logger.info("start to send detailMsg, msg={}", payload);
                jmsTemplate.convertAndSend(msgConfig.getDetailMsgQueue(), payload);
            } catch (Exception e) {
                logger.error("sendDetailMsg err, msg={}", msg, e);
            }
        });

    }
}
