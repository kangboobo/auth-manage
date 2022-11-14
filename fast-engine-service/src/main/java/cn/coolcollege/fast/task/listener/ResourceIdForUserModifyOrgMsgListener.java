package cn.coolcollege.fast.task.listener;

import cn.coolcollege.fast.config.ThreadPoolExecutorConfig;
import cn.coolcollege.fast.task.ResourceIdForUserModifyOrgTask;
import cn.coolcollege.fast.task.msg.ResourceIdForUserModifyOrgMsg;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用户修改组织事件查询资源id监听
 *
 * @author baibin
 * @date 2021/7/6 15:11
 */
@Component
public class ResourceIdForUserModifyOrgMsgListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ResourceIdForUserModifyOrgMsgListener.class);

    @Autowired
    private ThreadPoolExecutorConfig threadPoolExecutorConfig;

    private ExecutorService executorService;

    @JmsListener(destination = "${fast.engine.msg.id-msg-queue-for-user-modify-org}")
    public void onTaskMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return;
        }
        logger.info("receive userModifyOrgIdMsg = {}", msg);

        ResourceIdForUserModifyOrgMsg resourceIdForUserModifyOrgMsg;
        try {
            resourceIdForUserModifyOrgMsg = JSON.parseObject(msg, ResourceIdForUserModifyOrgMsg.class);
        } catch (Exception e) {
            logger.error("invalid userModifyOrg resourceIdMsg = {}", msg);
            return;
        }

        try {
            executorService.submit(new ResourceIdForUserModifyOrgTask(resourceIdForUserModifyOrgMsg));
        } catch (RejectedExecutionException rex) {
            logger.error("query resourceId userModifyOrgTask be rejected, msg={}", msg, rex);
        } catch (Exception e) {
            logger.error("submit query ResourceId userModifyOrgTask err, msg={}", msg, e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadPoolExecutorConfig.DefaultResourceIdConfig resourceIdConfig =
            threadPoolExecutorConfig.getDefaultResourceIdConfig();
        executorService = new ThreadPoolExecutor(resourceIdConfig.getCorePoolSize(),
            resourceIdConfig.getMaximumPoolSize(), resourceIdConfig.getKeepAliveTime(), TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(resourceIdConfig.getQueueSize()), new ThreadPoolExecutor.AbortPolicy());
    }
}
