package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.resource.ResourceProgressEvent;
import cn.coolcollege.fast.model.ResourceStatusDTO;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pk
 */
@Service
public class ResourceProgressHandler extends AbstractEventHandler {

    @Autowired
    private EsOperateService esService;

    @Autowired
    private JthBaseHandler jthBaseHandler;

    @Subscribe
    public void onEvent(ResourceProgressEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle resourceProgressEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        ResourceProgressEvent event = (ResourceProgressEvent)baseEvent;
        if (StringUtils.isBlank(event.getResourceId())) {
            return false;
        }
        if (StringUtils.isBlank(event.getResourceType())) {
            return false;
        }
        if (StringUtils.isBlank(event.getStatus())) {
            return false;
        }
        if (StringUtils.isBlank(event.getUserId())) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        ResourceProgressEvent event = (ResourceProgressEvent)baseEvent;

        if (!jthBaseHandler.whetherOpenCourseJth(event.getEid())
            && Constants.classDomainResourceTypeLists.contains(event.getResourceType())) {
            logger.info(
                "The course group feature is not turned on or the business is not on the course group whitelist，eid = {}, resourceType ={}",
                event.getEid(), event.getResourceType());
            return;
        }

        ResourceStatusDTO statusDTO = new ResourceStatusDTO();
        statusDTO.setAppId(event.getAppId());
        statusDTO.setEId(event.getEid());
        statusDTO.setDomainId(event.getDomainId());
        statusDTO.setResourceId(event.getResourceId());
        statusDTO.setResourceType(event.getResourceType());
        statusDTO.setStatus(event.getStatus());
        statusDTO.setUserId(event.getUserId());

        try {
            logger.info("start to updateResourceStudyStatus, msgId={}, do={}", event.getMsgId(), statusDTO);
            esService.updateResourceStudyStatus(statusDTO);
        } catch (Exception e) {
            logger.error("update resource progress userIds err, event={}", event, e);
        }
    }
}
