package cn.coolcollege.fast.task;

import cn.coolcollege.fast.SpringContextUtil;
import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
import cn.coolcollege.fast.task.msg.ResourceIdForAuthModifyMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.List;

/**
 * 计算某人修改管辖范围涉及的资源id的任务
 *
 * @author pk
 */
public class ResourceIdForAuthModifyTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ResourceIdForAuthModifyTask.class);

    private ResourceIdForAuthModifyMsg msg;

    public ResourceIdForAuthModifyTask(ResourceIdForAuthModifyMsg msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        // 1.获取某人当前可以管辖的所有用户id
        // 2.获取某人之前可以管辖的所有用户id
        // 3.比较出addUserIds, removeUserIds
        // 4.查询addUserIds, removeUserIds 他们所建的所有资源id
        // 5.发送到detailMsgQueue
        MDC.put(Constants.MSG_ID, msg.getMsgId());
        CalculateServiceHelper calculateServiceHelper =
            (CalculateServiceHelper)SpringContextUtil.getBean("calculateServiceHelper", CalculateServiceHelper.class);
        List<String> difUserIds =
            calculateServiceHelper.calUserDifAuthorityUserIds(msg.getAppId(), msg.getEid(), msg.getUserId());
        if (CollectionUtils.isEmpty(difUserIds)) {
            logger.warn("emptyDifUserIds, msg={}", msg);
            return;
        }
        List<String> resourceIds = calculateServiceHelper.queryResourceIdsByCreateUsers(msg.getAppId(), msg.getEid(),
            msg.getResourceType(), difUserIds, null);
        if (CollectionUtils.isEmpty(resourceIds)) {
            logger.warn("emptyResourceIds, msg={}", msg);
            return;
        }

        JmsMessagingTemplate jmsTemplate =
                (JmsMessagingTemplate)SpringContextUtil.getBean("jmsMessagingTemplate", JmsMessagingTemplate.class);
        MsgConfig msgConfig = (MsgConfig)SpringContextUtil.getBean("msgConfig", MsgConfig.class);

        ResourceDetailMsg detailTaskMsg =
            new ResourceDetailMsg.Builder().msgId(msg.getMsgId()).appId(msg.getAppId()).eid(msg.getEid())
                .domainId(msg.getDomainId()).resourceType(msg.getResourceType()).build();

        Lists.partition(resourceIds, Constants.ES_BULK_LOAD_ADD_SIZE).forEach(partResourceIds -> {
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
