package cn.coolcollege.fast.task;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsMessagingTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cn.coolcollege.fast.SpringContextUtil;
import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
import cn.coolcollege.fast.task.msg.ResourceIdForUserAddOrgMsg;

/**
 * 用户新增事件查询资源id任务
 *
 * @author pk
 * @date 2021-07-06 17:07
 */
public class ResourceIdForUserAddOrgTask implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(ResourceIdForUserAddOrgTask.class);

    private ResourceIdForUserAddOrgMsg msg;

    public ResourceIdForUserAddOrgTask(ResourceIdForUserAddOrgMsg msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        MDC.put(Constants.MSG_ID, msg.getMsgId());
        // 1.请求departmentId, positionId, groupId所有上级id
        // 2.分别查询直接挂在departmentId, positionId, groupId及所有上级的资源id

        CalculateServiceHelper calculateServiceHelper =
            (CalculateServiceHelper)SpringContextUtil.getBean("calculateServiceHelper", CalculateServiceHelper.class);

        Set<String> resIdSet = Sets.newHashSet();

        List<String> allDeptIds =
            calculateServiceHelper.queryParentDeptIds(msg.getAppId(), msg.getEid(), msg.getDepartmentIds(), true);
        if (CollectionUtils.isNotEmpty(allDeptIds)) {
            List<String> deptResIds = calculateServiceHelper.queryDirectResourceIdsByDeptIds(msg.getAppId(),
                msg.getEid(), msg.getResourceType(), allDeptIds);
            if (CollectionUtils.isNotEmpty(deptResIds)) {
                resIdSet.addAll(deptResIds);
            }
        }

        List<String> allPositionIds =
            calculateServiceHelper.queryParentPositionIds(msg.getAppId(), msg.getEid(), msg.getPositionIds(), true);
        if (CollectionUtils.isNotEmpty(allPositionIds)) {
            List<String> positionResIds = calculateServiceHelper.queryDirectResourceIdsByPositionIds(msg.getAppId(),
                msg.getEid(), msg.getResourceType(), allPositionIds);
            if (CollectionUtils.isNotEmpty(positionResIds)) {
                resIdSet.addAll(positionResIds);
            }
        }

        if (CollectionUtils.isNotEmpty(msg.getGroupIds())) {
            List<String> groupResIds = calculateServiceHelper.queryDirectResourceIdsByGroupIds(msg.getAppId(),
                msg.getEid(), msg.getResourceType(), msg.getGroupIds());
            if (CollectionUtils.isNotEmpty(groupResIds)) {
                resIdSet.addAll(groupResIds);
            }
        }
        List<String> resIds = Lists.newArrayList(resIdSet);
        if (CollectionUtils.isEmpty(resIds)) {
            logger.info("user add org query resourceIds result is empty, msg = {}", msg);
            return;
        }
        MsgConfig msgConfig = (MsgConfig)SpringContextUtil.getBean("msgConfig", MsgConfig.class);
        JmsMessagingTemplate jmsTemplate =
            (JmsMessagingTemplate)SpringContextUtil.getBean("jmsMessagingTemplate", JmsMessagingTemplate.class);
        ResourceDetailMsg detailMsg = new ResourceDetailMsg.Builder().appId(msg.getAppId()).eid(msg.getEid())
            .resourceType(msg.getResourceType()).build();
        Lists.partition(resIds, Constants.ES_BULK_LOAD_ADD_SIZE).forEach(partResourceIds -> {
            detailMsg.setResourceIds(partResourceIds);
            String payload = JSON.toJSONString(detailMsg);
            logger.info("start to send detailMsg, msg={}", payload);
            jmsTemplate.convertAndSend(msgConfig.getDetailMsgQueue(), payload);
        });

    }
}
