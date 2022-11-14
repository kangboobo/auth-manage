package cn.coolcollege.fast.task;

import cn.coolcollege.fast.SpringContextUtil;
import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.constants.FastEngineOrgTypeEnum;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
import cn.coolcollege.fast.task.msg.ResourceIdForOrgModifyMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 计算组织部门修改涉及资源id
 *
 * @author baibin
 * @date 2021/7/7 11:16
 */
public class ResourceIdForOrgModifyTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ResourceIdForOrgModifyTask.class);

    private ResourceIdForOrgModifyMsg msg;

    public ResourceIdForOrgModifyTask(ResourceIdForOrgModifyMsg msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        MDC.put(Constants.MSG_ID, msg.getMsgId());
        // 1.计算所有orgId以及其对应的上级orgId关联的资源id
        List<String> orgIds = Lists.newArrayList();
        if (StringUtils.isNotBlank(msg.getFromParentOrgId())) {
            orgIds.add(msg.getFromParentOrgId());
        }
        if (StringUtils.isNotBlank(msg.getToParentOrgId())) {
            orgIds.add(msg.getToParentOrgId());
        }
        if (CollectionUtils.isEmpty(orgIds)) {
            logger.error("org modify query resourceIds orgIds is empty,msg = {}", msg);
            return;
        }
        CalculateServiceHelper calculateServiceHelper =
            (CalculateServiceHelper)SpringContextUtil.getBean("calculateServiceHelper", CalculateServiceHelper.class);
        logger.info("modify org query resourceIds start query org parent ids,msg = {}", msg);
        List<String> orgModifyResourceIds = Lists.newArrayList();
        // 查询该组织类型下所有组织id及其父id对应的资源id集合
        List<String> orgResourceIds = calculateServiceHelper.queryResourceIdByParentOrgIds(msg.getMsgId(),
            msg.getAppId(), msg.getEid(), msg.getResourceType(), msg.getOrgType(), orgIds);
        if (CollectionUtils.isNotEmpty(orgResourceIds)) {
            orgModifyResourceIds.addAll(orgResourceIds);
        }
        // 2.计算本部门以及下级部门的人创建的资源id
        List<Long> orgUserIds = Lists.newArrayList();
        // 计算该组织类型下组织id及其子组织对应的用户id集合
        if (FastEngineOrgTypeEnum.DEPARTMENT.getOrgType().equals(msg.getOrgType())) {
            Map<Long, Set<Long>> deptUserIdMaps = calculateServiceHelper.getUserIdsGroupByDeptIdsV2(msg.getAppId(),
                Long.parseLong(msg.getEid()), Sets.newHashSet(Long.parseLong(msg.getOrgId())));
            if (MapUtils.isNotEmpty(deptUserIdMaps)) {
                deptUserIdMaps.forEach((k, v) -> orgUserIds.addAll(v));
            }
        } else if (FastEngineOrgTypeEnum.POSITION.getOrgType().equals(msg.getOrgType())) {
            Map<Long, Set<Long>> positionUserIdMaps = calculateServiceHelper.getUserIdsGroupByPositionIdsV2(
                msg.getAppId(), Long.parseLong(msg.getEid()), Sets.newHashSet(Long.parseLong(msg.getOrgId())));
            if (MapUtils.isNotEmpty(positionUserIdMaps)) {
                positionUserIdMaps.forEach((k, v) -> orgUserIds.addAll(v));
            }
        } else {
            Map<Long, Set<Long>> groupUserIdMaps = calculateServiceHelper.getUserIdsGroupByGroupIds(msg.getAppId(),
                Long.parseLong(msg.getEid()), Sets.newHashSet(Long.parseLong(msg.getOrgId())));
            if (MapUtils.isNotEmpty(groupUserIdMaps)) {
                groupUserIdMaps.forEach((k, v) -> orgUserIds.addAll(v));
            }
        }
        if (CollectionUtils.isNotEmpty(orgUserIds)) {
            // 查询所有用户id对应的资源id集合
            List<String> orgUserCreateResourceIds =
                calculateServiceHelper.queryResourceIdsByCreateUsers(msg.getAppId(), msg.getEid(),
                    msg.getResourceType(), orgUserIds.stream().map(String::valueOf).collect(Collectors.toList()), null);
            if (CollectionUtils.isNotEmpty(orgUserCreateResourceIds)) {
                orgModifyResourceIds.addAll(orgUserCreateResourceIds);
            }
        }
        if (CollectionUtils.isEmpty(orgModifyResourceIds)) {
            logger.info("modify org query resourceIds is empty,msg = {}", msg);
            return;
        }
        // 3.发送查询资源详情事件
        JmsMessagingTemplate jmsTemplate =
            (JmsMessagingTemplate)SpringContextUtil.getBean("jmsMessagingTemplate", JmsMessagingTemplate.class);
        MsgConfig msgConfig = (MsgConfig)SpringContextUtil.getBean("msgConfig", MsgConfig.class);

        ResourceDetailMsg orgModifyDetailTaskMsg =
                new ResourceDetailMsg.Builder().msgId(msg.getMsgId()).appId(msg.getAppId()).eid(msg.getEid())
                        .resourceType(msg.getResourceType()).build();

        Lists.partition(orgModifyResourceIds, Constants.ES_BULK_LOAD_ADD_SIZE).forEach(partResourceIds -> {
            orgModifyDetailTaskMsg.setResourceIds(partResourceIds);
            String payload = null;
            try {
                payload = JSON.toJSONString(orgModifyDetailTaskMsg);
                logger.info("start to send detailMsg, msg={}", payload);
                jmsTemplate.convertAndSend(msgConfig.getDetailMsgQueue(), payload);
            } catch (Exception e) {
                logger.error("send detailMsg err, msg={}", payload, e);
                return;
            }
            logger.info("org modify query resourceIds success,msg = {}", msg);
        });


    }
}
