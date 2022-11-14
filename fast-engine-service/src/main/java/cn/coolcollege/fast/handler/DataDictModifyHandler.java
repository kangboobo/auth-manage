package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.dto.EsFieldsDto;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.resource.DataDictModifyEvent;
import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author pk
 * @date 2021-06-27 19:26
 */
@Service
public class DataDictModifyHandler extends AbstractEventHandler {

    @Autowired
    private EsOperateService esService;
    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(DataDictModifyEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle dataDictModifyEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        DataDictModifyEvent event = (DataDictModifyEvent)baseEvent;
        String dataDict = event.getDataDict();
        if (StringUtils.isBlank(dataDict)) {
            return false;
        }
        List<String> resourceIds = event.getResourceIds();
        if (CollectionUtils.isEmpty(resourceIds)) {
            return false;
        }
        String resourceType = event.getResourceType();
        if (StringUtils.isBlank(resourceType)) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        DataDictModifyEvent event = (DataDictModifyEvent)baseEvent;
        if (!jthBaseHandler.whetherOpenCourseJth(event.getEid())
                && Constants.classDomainResourceTypeLists.contains(event.getResourceType())) {
            logger.info(
                    "The course group feature is not turned on or the business is not on the course group whitelist，eid = {}, resourceType ={}",
                    event.getEid(), event.getResourceType());
            return;
        }
        String dataDict = event.getDataDict();
        Map map;
        try {
            map = JSON.parseObject(dataDict, Map.class);
        } catch (Exception e) {
            logger.error("invalid dataDict, event={}", event);
            return;
        }
        EsFieldsDto fieldsDto = new EsFieldsDto();
        fieldsDto.setAppId(event.getAppId());
        fieldsDto.setEnterpriseId(event.getEid());
        fieldsDto.setDomainId(event.getDomainId());
        fieldsDto.setResourceType(event.getResourceType());
        fieldsDto.setResourceIds(event.getResourceIds());
        fieldsDto.setParamsMap(map);
        try {
            logger.info("start to updateEsBusinessField, msgId={}, do={}", event.getMsgId(), fieldsDto);
            esService.updateEsBusinessField(fieldsDto);
        } catch (Exception e) {
            logger.error("update esBusinessField err, event={}", event, e);
        }
    }
}
