package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.SpringContextUtil;
import cn.coolcollege.fast.dto.WrongMsgDo;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.ResourceCommonEvent;
import cn.coolcollege.fast.service.EsOperateService;
import com.alipay.sofa.rpc.common.json.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pk
 */
public abstract class AbstractEventHandler {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 校验msg是否合法
     *
     * @param baseEvent
     * @return
     */
    abstract boolean checkEvent(BaseEvent baseEvent);

    /**
     * do handle
     *
     * @param baseEvent
     */
    abstract void doHandle(BaseEvent baseEvent);

    public void handleEvent(BaseEvent baseEvent) {
        //先检查基本消息
        if (!checkBaseEvent(baseEvent)) {
            logger.error("checkBaseMsg failed, baseEvent = {}", baseEvent);
            saveWrongMsg(baseEvent);
            return;
        }
        //检查自己独有的消息
        if (!checkEvent(baseEvent)) {
            logger.error("checkMsg failed, baseEvent = {}", baseEvent);
            saveWrongMsg(baseEvent);
            return;
        }
        try {
            doHandle(baseEvent);
        } catch (Exception e) {
            logger.error("doHandle err, baseEvent={}", baseEvent, e);
        }
    }


    /**
     * 记录消息异常日志并将错误消息存入ES
     *
     * @param baseEvent 消息Json
     */
    private void saveWrongMsg(BaseEvent baseEvent) {
        logger.error("{} msg parameter exception", baseEvent.getMsgType());
        EsOperateService esOperateService = (EsOperateService) SpringContextUtil.getBean("esOperateService", EsOperateService.class);

        esOperateService.addWrongMsgRecord(structureWrongMsgDo(JSON.toJSONString(baseEvent)));
    }

    /**
     * 构建WrongMsgDo
     *
     * @param msg 消息
     * @return WrongMsgDo
     */
    private WrongMsgDo structureWrongMsgDo(String msg) {
        return new WrongMsgDo(msg, System.currentTimeMillis());
    }

    private boolean checkBaseEvent(BaseEvent baseEvent) {
        String eid = baseEvent.getEid();
        if (StringUtils.isBlank(eid)) {
            return false;
        }
        String appId = baseEvent.getAppId();
        if (StringUtils.isBlank(appId)) {
            return false;
        }
        String msgType = baseEvent.getMsgType();
        if (StringUtils.isBlank(msgType)) {
            return false;
        }
        String msgId = baseEvent.getMsgId();
        if (StringUtils.isBlank(msgId)) {
            return false;
        }
        Long eventTs = baseEvent.getEventTs();
        if (eventTs == null) {
            return false;
        }
        if(baseEvent instanceof ResourceCommonEvent){
            ResourceCommonEvent resourceCommonEvent = (ResourceCommonEvent) baseEvent;
            if(StringUtils.isEmpty(resourceCommonEvent.getDomainId())){
                return false;
            }
        }
        return true;
    }
}
