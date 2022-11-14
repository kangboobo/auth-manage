package cn.coolcollege.fast.task.listener;

import cn.coolcollege.fast.config.ThreadPoolExecutorConfig;
import cn.coolcollege.fast.task.ResourceIdForUserAddOrgTask;
import cn.coolcollege.fast.task.msg.ResourceIdForUserAddOrgMsg;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 用户新增事件查询资源id监听
 *
 * @author pk
 * @date 2021-07-06 14:00
 */
@Component
public class ResourceIdForUserAddOrgMsgListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ResourceIdForUserAddOrgMsgListener.class);

    @Autowired
    private ThreadPoolExecutorConfig threadPoolExecutorConfig;
    private ExecutorService executorService;

    @JmsListener(destination = "${fast.engine.msg.id-msg-queue-for-user-add-org}")
    public void onTaskMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return;
        }
        logger.info("receive userAddOrgIdMsg = {}", msg);
        ResourceIdForUserAddOrgMsg taskMsg = null;
        try {
            taskMsg = JSON.parseObject(msg, ResourceIdForUserAddOrgMsg.class);
        } catch (Exception e) {
            logger.error("invalid userAddOrgIdMsg={}", msg);
            return;
        }

        try {
            executorService.submit(new ResourceIdForUserAddOrgTask(taskMsg));
        } catch (RejectedExecutionException rex) {
            logger.error("resourceIdForUserAddOrgTask be rejected, msg={}", msg, rex);
        } catch (Exception e) {
            logger.error("submit resourceIdForUserAddOrgTask err, msg={}", msg, e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadPoolExecutorConfig.DefaultResourceIdConfig resourceIdConfig = threadPoolExecutorConfig.getDefaultResourceIdConfig();
        executorService = new ThreadPoolExecutor(resourceIdConfig.getCorePoolSize(), resourceIdConfig.getMaximumPoolSize()
                , resourceIdConfig.getKeepAliveTime(), TimeUnit.MINUTES, new LinkedBlockingQueue<>(resourceIdConfig.getQueueSize())
                , new ThreadPoolExecutor.AbortPolicy());
    }
}
