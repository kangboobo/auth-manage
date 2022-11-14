package cn.coolcollege.fast.listener;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.constants.FastMsgTypeEnum;
import cn.coolcollege.fast.event.EventBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.EventBus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 监听业务发送的消息类型
 *
 * @author bai bin
 */
@Component
public class QueueMessageListener {
    private static final String MSG_PREFIX = "fast_msg_%s";
    private static final int EXPIRE_KEY_MINUTES = 1;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EventBus eventBus;

    @Resource
    private EsOperateService esOperateService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @JmsListener(destination = "${fast.engine.resource.topic}", containerFactory = "jmsListenerContainerTopic")
    public void receiveQueue(String msg) {
        if (StringUtils.isBlank(msg)) {
            logger.error("fast-engine receive topic is null");
            return;
        }
        JSONObject msgJson = JSON.parseObject(msg);
        String msgId = msgJson.getString("msg_id");
        if (StringUtils.isBlank(msgId)) {
            return;
        }
        String msgKey = buildMsgKey(msgId);
        if (StringUtils.isBlank(msgKey)) {
            return;
        }
        Boolean absent =
            stringRedisTemplate.opsForValue().setIfAbsent(msgKey, msgId, EXPIRE_KEY_MINUTES, TimeUnit.MINUTES);
        if (!absent) {
            return;
        }
        logger.info("fast-engine receive topic,{}", msg);
        FastMsgTypeEnum fastMsgTypeEnum = FastMsgTypeEnum.getByValue(msgJson.getString(Constants.msgType));
        if (fastMsgTypeEnum == null) {
            logger.error("un support msgType, msg is {}", msg);
            return;
        }
        Object event;
        try {
            event = new EventBuilder(msgJson).type(fastMsgTypeEnum.getEventClass()).build();
        } catch (Exception e) {
            logger.error("build event err, msg={}", msg, e);
            return;
        }
        if (event != null) {
            eventBus.post(event);
        }
    }

    /**
     * 构造消息在workflow唯一的key
     *
     * @param msgId
     * @return
     */
    private String buildMsgKey(String msgId) {
        if (org.springframework.util.StringUtils.isEmpty(msgId)) {
            return null;
        }
        return String.format(MSG_PREFIX, msgId);
    }
}
