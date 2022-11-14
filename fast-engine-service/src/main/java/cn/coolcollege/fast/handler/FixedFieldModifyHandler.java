package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.dto.EsFieldsDto;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.resource.FixedFieldModifyEvent;
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
 * @date 2021-06-27 20:27
 */
@Service
public class FixedFieldModifyHandler extends AbstractEventHandler {

    @Autowired
    private EsOperateService esOperateService;
    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(FixedFieldModifyEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle fixedFieldModifyEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        FixedFieldModifyEvent event = (FixedFieldModifyEvent)baseEvent;
        List<String> resourceIds = event.getResourceIds();
        if (CollectionUtils.isEmpty(resourceIds)) {
            return false;
        }
        String resourceType = event.getResourceType();
        if (StringUtils.isBlank(resourceType)) {
            return false;
        }
        String field = event.getField();
        if (StringUtils.isBlank(field)) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        FixedFieldModifyEvent event = (FixedFieldModifyEvent)baseEvent;
        if (!jthBaseHandler.whetherOpenCourseJth(event.getEid())
                && Constants.classDomainResourceTypeLists.contains(event.getResourceType())) {
            logger.info(
                    "The course group feature is not turned on or the business is not on the course group whitelist，eid = {}, resourceType ={}",
                    event.getEid(), event.getResourceType());
            return;
        }
        String field = event.getField();
        Map map;
        try {
            map = JSON.parseObject(field, Map.class);
        } catch (Exception e) {
            logger.error("invalid field, event={}", event);
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
            logger.info("start to updateEsResourceFixedField ,msgId={}, do={}", event.getMsgId(), fieldsDto);
            esOperateService.updateEsResourceFixedField(fieldsDto);
        } catch (Exception e) {
            logger.error("updateEsResourceFixedField err, dto={}", fieldsDto, e);
        }
    }
}
