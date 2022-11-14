package cn.coolcollege.fast.task.listener;

import cn.coolcollege.fast.config.ThreadPoolExecutorConfig;
import cn.coolcollege.fast.task.ResourceIdForDirectExtendClassifyTask;
import cn.coolcollege.fast.task.msg.ResourceIdForDirectExtendClassifyMsg;
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
 * 直接继承分类的资源id任务监听
 *
 * @author pk
 * @date 2021-06-27 10:19
 */
@Component
public class ResourceIdForDirectExtendClassifyMsgListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ResourceIdForDirectExtendClassifyMsgListener.class);

    @Autowired
    private ThreadPoolExecutorConfig threadPoolExecutorConfig;
    private ExecutorService executorService;

    @JmsListener(destination = "${fast.engine.msg.id-msg-queue-for-direct-extend-classify}")
    public void onTaskMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return;
        }
        logger.info("receive directExtendId msg = {}", msg);
        ResourceIdForDirectExtendClassifyMsg resourceIdForDirectExtendClassifyMsg;
        try {
            resourceIdForDirectExtendClassifyMsg = JSON.parseObject(msg, ResourceIdForDirectExtendClassifyMsg.class);
        } catch (Exception e) {
            logger.error("invalid directExtendResourceIdMsg = {}", msg);
            return;
        }

        try {
            executorService.submit(new ResourceIdForDirectExtendClassifyTask(resourceIdForDirectExtendClassifyMsg));
        } catch (RejectedExecutionException rex) {
            logger.error("queryDirectExtendResourceIdTask be rejected, msg={}", msg, rex);
        } catch (Exception e) {
            logger.error("submit queryDirectExtendResourceIdTask err, msg={}", msg, e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadPoolExecutorConfig.DefaultResourceIdConfig resourceIdConfig =
            threadPoolExecutorConfig.getDefaultResourceIdConfig();
        executorService = new ThreadPoolExecutor(resourceIdConfig.getCorePoolSize(), resourceIdConfig.getMaximumPoolSize()
                , resourceIdConfig.getKeepAliveTime(), TimeUnit.MINUTES, new LinkedBlockingQueue<>(resourceIdConfig.getQueueSize())
                , new ThreadPoolExecutor.AbortPolicy());
    }
}
