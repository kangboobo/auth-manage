package cn.coolcollege.fast.task;

import cn.coolcollege.fast.SpringContextUtil;
import cn.coolcollege.fast.config.MsgConfig;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.entity.request.GetPageDirectExtendResourceIdsByClassifyIdRequest;
import cn.coolcollege.fast.entity.result.GetPageDirectExtendResourceIdsByClassifyIdResult;
import cn.coolcollege.fast.task.msg.ResourceIdForDirectExtendClassifyMsg;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import net.coolcollege.platform.util.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.Set;

/**
 * 查询直接跟随某个分类可见性的资源id的任务
 *
 * @author pk
 * @date 2021-06-28 00:04
 */
public class ResourceIdForDirectExtendClassifyTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ResourceIdForDirectExtendClassifyTask.class);

    private ResourceIdForDirectExtendClassifyMsg msg;

    public ResourceIdForDirectExtendClassifyTask(ResourceIdForDirectExtendClassifyMsg msg) {
        this.msg = msg;
    }

    @Override
    public void run() {

        //1.分页请求resourceService获取特定resourceType下的所有资源id
        //2.把分页请求到的resourceId 发送到 resourceDetailTask 接收队列
        GetPageDirectExtendResourceIdsByClassifyIdRequest request = buildQueryResourceIdReq();

        JmsMessagingTemplate jmsTemplate = (JmsMessagingTemplate) SpringContextUtil.getBean("jmsMessagingTemplate", JmsMessagingTemplate.class);
        ResourceServiceProxy resourceServiceProxy = (ResourceServiceProxy) SpringContextUtil.getBean("resourceServiceProxy", ResourceServiceProxy.class);

        MDC.put(Constants.MSG_ID, msg.getMsgId());
        Set<Integer> pageNumSet = Sets.newHashSet();
        for (; ; ) {
            if (pageNumSet.contains(request.getPageNum())) {
                //防止服务方代码bug导致无限循环
                logger.error("getPageBatchResourceByEnterpriseId pageNum cycle!, msg={}", msg);
                break;
            }
            pageNumSet.add(request.getPageNum());

            logger.info("start to query resourceId, request={}", request);

            GetPageDirectExtendResourceIdsByClassifyIdResult pageResult = resourceServiceProxy.getPageDirectExtendResourceIdsByClassifyId(request);

            if (!pageResult.getSuccess()) {
                logger.error("getPageDirectExtendResourceIdsByClassifyIdResult p err, request={}, resultCode={}", request, pageResult.getResultCode());
                break;
            }
            ResourceDetailMsg detailTaskMsg = new ResourceDetailMsg.Builder().msgId(msg.getMsgId())
                    .appId(msg.getAppId()).eid(msg.getEid()).domainId(msg.getDomainId())
                    .resourceType(msg.getResourceType()).resourceIds(pageResult.getResourceIds()).build();

            MsgConfig msgConfig = (MsgConfig) SpringContextUtil.getBean("msgConfig", MsgConfig.class);
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
    private GetPageDirectExtendResourceIdsByClassifyIdRequest buildQueryResourceIdReq() {
        GetPageDirectExtendResourceIdsByClassifyIdRequest request = new GetPageDirectExtendResourceIdsByClassifyIdRequest();
        request.setAppId(msg.getAppId());
        request.setEnterpriseId(Long.valueOf(msg.getEid()));
        request.setResourceType(msg.getResourceType());
        request.setClassifyId(msg.getClassifyId());
        request.setPageNum(CommonConstants.ONE_VALUE_INTEGER);
        request.setPageSize(CommonConstants.BATCH_QUERY_SIZE);
        return request;
    }
}
