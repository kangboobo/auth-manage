package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.dto.EsResourceListDto;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.resource.ResourceDeleteEvent;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pk
 */
@Service
public class ResourceDeleteHandler extends AbstractEventHandler {

    @Autowired
    private EsOperateService esService;

    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(ResourceDeleteEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle resourceDeleteEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        ResourceDeleteEvent event = (ResourceDeleteEvent)baseEvent;
        if (CollectionUtils.isEmpty(event.getResourceIds())) {
            return false;
        }
        if (StringUtils.isBlank(event.getResourceType())) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        ResourceDeleteEvent event = (ResourceDeleteEvent)baseEvent;
        if (!jthBaseHandler.whetherOpenCourseJth(event.getEid())
                && Constants.classDomainResourceTypeLists.contains(event.getResourceType())) {
            logger.info(
                    "The course group feature is not turned on or the business is not on the course group whitelist，eid = {}, resourceType ={}",
                    event.getEid(), event.getResourceType());
            return;
        }

        EsResourceListDto esResourceListDto = new EsResourceListDto();
        esResourceListDto.setAppId(event.getAppId());
        esResourceListDto.setEnterpriseId(event.getEid());
        esResourceListDto.setDomainId(event.getDomainId());
        esResourceListDto.setResourceType(event.getResourceType());
        esResourceListDto.setResourceIds(event.getResourceIds());

        try {
            logger.info("start to deleteEsResourceInfo, msgId={}, do={}", event.getMsgId(), esResourceListDto);
            esService.deleteEsResourceInfo(esResourceListDto);
        } catch (Exception e) {
            logger.error("deleteEsResource err, esResource={}", esResourceListDto, e);
        }
    }
}
