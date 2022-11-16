package cn.coolcollege.fast.task;

import cn.coolcollege.fast.SpringContextUtil;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.List;

/**
 * 根据资源id计算资源可见性的任务
 *
 * @author pk
 * @date 2021-06-23 16:31
 */
public class ResourceDetailTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ResourceDetailTask.class);

    private ResourceDetailMsg msg;

    public ResourceDetailTask(ResourceDetailMsg msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        MDC.put(Constants.MSG_ID, msg.getMsgId());
        String appId = msg.getAppId();
        String resourceType = msg.getResourceType();
        Long eid = Long.valueOf(msg.getEid());
        String domainId = msg.getDomainId();
        if (StringUtils.isBlank(domainId)) {
            domainId = Constants.resourceTypeDomainMaps.get(resourceType);
        }
        logger.info("start query details,msg = {}", msg);
        if (CollectionUtils.isEmpty(msg.getResourceIds())) {
            logger.error("query details resource ids is empty, msg = {}", msg);
            return;
        }

        // 1.获取详情
        CalculateServiceHelper calculateServiceHelper =
            (CalculateServiceHelper)SpringContextUtil.getBean("calculateServiceHelper", CalculateServiceHelper.class);
        List<Resource> resourceList =
            calculateServiceHelper.queryResourceDetails(appId, eid, domainId, resourceType, msg.getResourceIds());
        if (CollectionUtils.isEmpty(resourceList)) {
            logger.warn("resourceList is empty, msg={}", msg);
            return;
        }
        logger.info("start build es obj,msg = {}", msg);
        // 2.组装es对象
        List<UserResourceDo> userResourceDos =
            calculateServiceHelper.buildUserResourceDoList(appId, eid, resourceType, resourceList);
        if (CollectionUtils.isEmpty(userResourceDos)) {
            return;
        }

        logger.info("start write to es,msg = {}", msg);
        // 3.写入es
        EsOperateService esOperateService =
            (EsOperateService)SpringContextUtil.getBean("esOperateServiceImpl", EsOperateService.class);
        try {
            esOperateService.addOrUpdateDocument(userResourceDos, domainId);
        } catch (Exception e) {
            logger.error("addOrUpdateDocument err, detailTaskMsg={}", msg, e);
        }
    }

}
