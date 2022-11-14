package cn.coolcollege.fast.task;

import cn.coolcollege.fast.SpringContextUtil;
import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
import cn.coolcollege.fast.task.msg.ResourceIdForUserModifyOrgMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.List;
import java.util.Set;

/**
 * 计算用户修改组织部门涉及资源id
 *
 * @author baibin
 * @date 2021-07-06 15:20
 */
public class ResourceIdForUserModifyOrgTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ResourceIdForUserModifyOrgTask.class);

    private ResourceIdForUserModifyOrgMsg msg;

    public ResourceIdForUserModifyOrgTask(ResourceIdForUserModifyOrgMsg msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        MDC.put(Constants.MSG_ID, msg.getMsgId());
        // 1.计算新增和删除的orgIds,统一计算所有挂载到这些orgId上的资源
        Set<String> orgIds = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(msg.getAddOrgIds())) {
            orgIds.addAll(msg.getAddOrgIds());
        }
        if (CollectionUtils.isNotEmpty(msg.getRemoveOrgIds())) {
            orgIds.addAll(msg.getRemoveOrgIds());
        }
        if (CollectionUtils.isEmpty(orgIds)) {
            logger.error("user modify org query resourceIds orgIds is empty, msg={}", msg);
            return;
        }
        CalculateServiceHelper calculateServiceHelper =
            (CalculateServiceHelper)SpringContextUtil.getBean("calculateServiceHelper", CalculateServiceHelper.class);
        // 根据orgType查询不同的服务
        logger.info("user modify org query resourceIds start query org parent ids,msg ={}", msg);
        List<String> userModifyOrgResourceIds = Lists.newArrayList();

        List<String> orgResourceIds = calculateServiceHelper.queryResourceIdByParentOrgIds(msg.getMsgId(),
            msg.getAppId(), msg.getEid(), msg.getResourceType(), msg.getOrgType(), Lists.newArrayList(orgIds));
        if (CollectionUtils.isNotEmpty(orgResourceIds)) {
            userModifyOrgResourceIds.addAll(orgResourceIds);
        }
        // 2.查询该用户创建的资源id
        logger.info("user modify org query resourceIds start query user create resource ids,msg ={}", msg);
        List<String> userCreateResourceIds = calculateServiceHelper.queryResourceIdsByCreateUsers(msg.getAppId(),
            msg.getEid(), msg.getResourceType(), Lists.newArrayList(msg.getUserId()), null);
        if (CollectionUtils.isNotEmpty(userCreateResourceIds)) {
            userModifyOrgResourceIds.addAll(userCreateResourceIds);
        }
        if (CollectionUtils.isEmpty(userModifyOrgResourceIds)) {
            logger.info("user modify org query resourceIds result is empty,msg = {}", msg);
            return;
        }
        // 3.发送查询资源详情事件
        JmsMessagingTemplate jmsTemplate =
            (JmsMessagingTemplate)SpringContextUtil.getBean("jmsMessagingTemplate", JmsMessagingTemplate.class);
        MsgConfig msgConfig = (MsgConfig)SpringContextUtil.getBean("msgConfig", MsgConfig.class);
        ResourceDetailMsg userModifyOrgDetailTaskMsg =
                new ResourceDetailMsg.Builder().msgId(msg.getMsgId()).appId(msg.getAppId()).eid(msg.getEid())
                        .resourceType(msg.getResourceType()).build();

        Lists.partition(userModifyOrgResourceIds, Constants.ES_BULK_LOAD_ADD_SIZE).forEach(partResourceIds -> {
            userModifyOrgDetailTaskMsg.setResourceIds(partResourceIds);
            String payload = null;
            try {
                payload = JSON.toJSONString(userModifyOrgDetailTaskMsg);
                logger.info("start to send detailMsg, msg={}", payload);
                jmsTemplate.convertAndSend(msgConfig.getDetailMsgQueue(), payload);
            } catch (Exception e) {
                logger.error("send detailMsg err, msg={}", payload, e);
            }
        });
    }
}
