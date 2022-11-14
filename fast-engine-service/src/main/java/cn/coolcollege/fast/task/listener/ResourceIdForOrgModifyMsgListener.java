package cn.coolcollege.fast.task.listener;

import cn.coolcollege.fast.config.ThreadPoolExecutorConfig;
import cn.coolcollege.fast.task.ResourceIdForOrgModifyTask;
import cn.coolcollege.fast.task.ResourceIdForUserModifyOrgTask;
import cn.coolcollege.fast.task.msg.ResourceIdForOrgModifyMsg;
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
 * 部门修改事件查询资源id监听
 *
 * @author baibin
 * @date 2021/7/7 11:04
 */
@Component
public class ResourceIdForOrgModifyMsgListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ResourceIdForOrgModifyMsgListener.class);

    @Autowired
    private ThreadPoolExecutorConfig threadPoolExecutorConfig;

    private ExecutorService executorService;

    @JmsListener(destination = "${fast.engine.msg.id-msg-queue-for-org-modify}")
    public void onTaskMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return;
        }
        logger.info("receive orgModifyIdMsg = {}", msg);

        ResourceIdForOrgModifyMsg resourceIdForOrgModifyMsg;
        try {
            resourceIdForOrgModifyMsg = JSON.parseObject(msg, ResourceIdForOrgModifyMsg.class);
        } catch (Exception e) {
            logger.error("invalid orgModify resourceIdMsg = {}", msg);
            return;
        }

        try {
            executorService.submit(new ResourceIdForOrgModifyTask(resourceIdForOrgModifyMsg));
        } catch (RejectedExecutionException rex) {
            logger.error("query resourceId orgModifyTask be rejected, msg={}", msg, rex);
        } catch (Exception e) {
            logger.error("submit query ResourceId orgModifyTask err, msg={}", msg, e);
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
