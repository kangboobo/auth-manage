package cn.coolcollege.fast.task.listener;

import cn.coolcollege.fast.config.ThreadPoolExecutorConfig;
import cn.coolcollege.fast.task.ResourceIdForAuthModifyTask;
import cn.coolcollege.fast.task.msg.ResourceIdForAuthModifyMsg;
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
 * @author pk
 */
@Component
public class ResourceIdForAuthModifyMsgListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ResourceIdForAuthModifyMsgListener.class);
    @Autowired
    private ThreadPoolExecutorConfig threadPoolExecutorConfig;
    private ExecutorService executorService;

    @JmsListener(destination = "${fast.engine.msg.id-msg-queue-for-auth-modify}")
    public void onMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return;
        }
        ResourceIdForAuthModifyMsg taskMsg;
        try {
            taskMsg = JSON.parseObject(msg, ResourceIdForAuthModifyMsg.class);
        } catch (Exception e) {
            logger.error("invalid resourceIdForAuthModifyMsg, msg={}", msg);
            return;
        }

        try {
            executorService.submit(new ResourceIdForAuthModifyTask(taskMsg));
        } catch (RejectedExecutionException rex) {
            logger.error("resourceIdForAuthModifyTask be rejected, msg={}", msg, rex);
        } catch (Exception e) {
            logger.error("submit resourceIdForAuthModifyTask err, msg={}", msg, e);
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
