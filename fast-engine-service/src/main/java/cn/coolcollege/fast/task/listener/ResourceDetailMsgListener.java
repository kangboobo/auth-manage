package cn.coolcollege.fast.task.listener;

import cn.coolcollege.fast.config.ThreadPoolExecutorConfig;
import cn.coolcollege.fast.task.ResourceDetailTask;
import cn.coolcollege.fast.task.msg.ResourceDetailMsg;
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
 * 资源详情任务消息监听
 *
 * @author pk
 * @date 2021-06-24 11:23
 */
@Component
public class ResourceDetailMsgListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ResourceDetailMsgListener.class);

    @Autowired
    private ThreadPoolExecutorConfig threadPoolExecutorConfig;

    private ExecutorService executorService;

    @JmsListener(destination = "${fast.engine.msg.detail-msg-queue}")
    public void onQueryResourceDetailMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return;
        }
        logger.info("receive detailMsg = {}", msg);
        ResourceDetailMsg resourceDetailMsg;
        try {
            resourceDetailMsg = JSON.parseObject(msg, ResourceDetailMsg.class);
        } catch (Exception e) {
            logger.error("invalid detailMsg = {}", msg);
            return;
        }
        ResourceDetailTask resourceDetailTask = new ResourceDetailTask(resourceDetailMsg);
        try {
            executorService.submit(resourceDetailTask);
        } catch (RejectedExecutionException rejectedExecutionException) {
            logger.error("resourceDetailTask be rejected, msg={}", msg, rejectedExecutionException);
        } catch (Exception e) {
            logger.error("submit resourceDetailTask err, msg={}", msg, e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadPoolExecutorConfig.ResourceDetailConfig resourceDetailConfig = threadPoolExecutorConfig.getResourceDetailConfig();
        executorService = new ThreadPoolExecutor(resourceDetailConfig.getCorePoolSize(), resourceDetailConfig.getMaximumPoolSize()
                , resourceDetailConfig.getKeepAliveTime(), TimeUnit.MINUTES, new LinkedBlockingQueue<>(resourceDetailConfig.getQueueSize())
                , new ThreadPoolExecutor.AbortPolicy());
    }
}
