package cn.coolcollege.fast.task;

import cn.coolcollege.fast.SpringContextUtil;
import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.entity.request.GetPageBatchResourceInfoRequest;
import cn.coolcollege.fast.entity.result.GetPageBatchResourceInfoResult;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
import cn.coolcollege.fast.task.msg.ResourceIdForBulkLoadMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import net.coolcollege.platform.util.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.Set;

/**
 * bulk load 查询单个resource_type资源id
 *
 * @author pk
 * @date 2021-06-22 16:12
 */
public class ResourceIdForBulkLoadTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ResourceIdForBulkLoadTask.class);

    private ResourceIdForBulkLoadMsg msg;

    public ResourceIdForBulkLoadTask(ResourceIdForBulkLoadMsg msg) {
        this.msg = msg;
    }

    @Override
    public void run() {

        MDC.put(Constants.MSG_ID, msg.getMsgId());
        // 1.分页请求resourceService获取特定resourceType下的所有资源id
        // 2.把分页请求到的resourceId 发送到 resourceDetailTask 接收队列
        GetPageBatchResourceInfoRequest request = buildQueryResourceIdReq();

        JmsMessagingTemplate jmsTemplate =
            (JmsMessagingTemplate)SpringContextUtil.getBean("jmsMessagingTemplate", JmsMessagingTemplate.class);
        ResourceServiceProxy resourceServiceProxy =
            (ResourceServiceProxy)SpringContextUtil.getBean("resourceServiceProxy", ResourceServiceProxy.class);
        MsgConfig msgConfig = (MsgConfig)SpringContextUtil.getBean("msgConfig", MsgConfig.class);
        Set<Integer> pageNumSet = Sets.newHashSet();
        for (;;) {
            if (pageNumSet.contains(request.getPageNum())) {
                // 防止服务方代码bug导致无限循环
                logger.error("getPageBatchResourceByEnterpriseId pageNum cycle!, msg={}", msg);
                break;
            }
            pageNumSet.add(request.getPageNum());

            logger.info("start to query resourceId, request={}", request);

            GetPageBatchResourceInfoResult pageResult;
            try {
                pageResult =
                        resourceServiceProxy.getPageBatchResourceByEnterpriseId(request);

            } catch (Exception e) {
                logger.error("query resourceId err, request={}", request, e);
                break;
            }
            if (pageResult == null) {
                logger.error("getPageBatchResourceByEnterpriseId err, request={}", request);
                break;
            }
            if (!pageResult.getSuccess()) {
                logger.error("getPageBatchResourceByEnterpriseId err, request={}, resultCode={}", request,
                        pageResult.getResultCode());
                break;
            }
            ResourceDetailMsg detailTaskMsg = new ResourceDetailMsg.Builder().msgId(msg.getMsgId())
                    .appId(msg.getAppId()).eid(msg.getEid()).domainId(msg.getDomainId()).resourceType(msg.getResourceType())
                    .resourceIds(pageResult.getResourceIds()).build();

            String payload = null;
            try {
                payload = JSON.toJSONString(detailTaskMsg);
                logger.info("start to send detailMsg, msg={}", payload);
                jmsTemplate.convertAndSend(msgConfig.getDetailMsgQueue(), payload);
            } catch (Exception e) {
                logger.error("send detailMsg err, msg={}, pageNum={}", payload, request.getPageNum(), e);
                break;
            }

            if (!pageResult.isHasNextPage()) {
                break;
            }
            request.setPageNum(pageResult.getNextPage());
        }
    }

    /**
     * 构造请求资源id的请求
     *
     * @return
     */
    private GetPageBatchResourceInfoRequest buildQueryResourceIdReq() {
        GetPageBatchResourceInfoRequest request = new GetPageBatchResourceInfoRequest();
        request.setAppId(msg.getAppId());
        request.setEnterpriseId(Long.valueOf(msg.getEid()));
        request.setResourceType(msg.getResourceType());
        request.setDomainId(msg.getDomainId());
        request.setPageNum(CommonConstants.ONE_VALUE_INTEGER);
        request.setPageSize(CommonConstants.BATCH_QUERY_SIZE);
        return request;
    }

}
