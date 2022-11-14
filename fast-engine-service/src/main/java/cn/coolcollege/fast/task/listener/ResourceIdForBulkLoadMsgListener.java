package cn.coolcollege.fast.task.listener;

import cn.coolcollege.fast.config.ThreadPoolExecutorConfig;
import cn.coolcollege.fast.task.ResourceIdForBulkLoadTask;
import cn.coolcollege.fast.task.msg.ResourceIdForBulkLoadMsg;
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
 * bulk load 资源id任务监听
 *
 * @author pk
 * @date 2021-06-27 10:19
 */
@Component
public class ResourceIdForBulkLoadMsgListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ResourceIdForBulkLoadMsgListener.class);

    @Autowired
    private ThreadPoolExecutorConfig threadPoolExecutorConfig;
    private ExecutorService executorService;

    @JmsListener(destination = "${fast.engine.msg.id-msg-queue-for-bulk-load}")
    public void onTaskMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return;
        }
        logger.info("receive bulk load id msg = {}", msg);
        ResourceIdForBulkLoadMsg resourceIdForBulkLoadMsg;
        try {
            resourceIdForBulkLoadMsg = JSON.parseObject(msg, ResourceIdForBulkLoadMsg.class);
        } catch (Exception e) {
            logger.error("invalid bulk load resourceIdMsg = {}", msg);
            return;
        }

        try {
            executorService.submit(new ResourceIdForBulkLoadTask(resourceIdForBulkLoadMsg));
        } catch (RejectedExecutionException rex) {
            logger.error("queryResourceIdTask be rejected, msg={}", msg, rex);
        } catch (Exception e) {
            logger.error("submit queryResourceIdTask err, msg={}", msg, e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadPoolExecutorConfig.BulkLoadResourceIdConfig resourceIdConfig = threadPoolExecutorConfig.getBulkLoadResourceIdConfig();
        executorService = new ThreadPoolExecutor(resourceIdConfig.getCorePoolSize(), resourceIdConfig.getMaximumPoolSize()
                , resourceIdConfig.getKeepAliveTime(), TimeUnit.MINUTES, new LinkedBlockingQueue<>(resourceIdConfig.getQueueSize())
                , new ThreadPoolExecutor.AbortPolicy());
    }
}
