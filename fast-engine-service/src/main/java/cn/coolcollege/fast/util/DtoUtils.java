package cn.coolcollege.fast.util;

import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.ResourceCommonEvent;
import cn.coolcollege.fast.event.org.OrgModifyEvent;
import cn.coolcollege.fast.event.org.UserAddOrgEvent;
import cn.coolcollege.fast.event.org.UserLeaveOrgEvent;
import cn.coolcollege.fast.event.org.UserModifyOrgEvent;
import cn.coolcollege.fast.event.resource.*;
import cn.coolcollege.fast.model.*;

import java.util.List;

/**
 * @author pk
 */
public class DtoUtils {

    /**
     * 构建BaseDTO
     *
     * @param baseDTO   基础部分
     * @param baseEvent 消息JSon
     */
    public static void structureBaseDTO(BaseDTO baseDTO, BaseEvent baseEvent) {
        baseDTO.setMsgId(baseEvent.getMsgId());
        baseDTO.setAppId(baseEvent.getAppId());
        baseDTO.setEId(baseEvent.getEid());
        baseDTO.setMessageType(baseEvent.getMsgType());
        baseDTO.setEventTs(baseEvent.getEventTs());
        baseDTO.setOperateUserId(baseEvent.getOperateUserId());
        baseDTO.setOperateUserName(baseEvent.getOperateUserName());
    }

    /**
     * 构建资源类型事件
     * @param resourceCommonDTO
     * @param resourceCommonEvent
     */
    public static void structureResourceCommonDTO(ResourceCommonDTO resourceCommonDTO,ResourceCommonEvent resourceCommonEvent){
        resourceCommonDTO.setDomainId(resourceCommonEvent.getDomainId());
    }

    public static UserDTO buildUserAddDTO(UserAddOrgEvent event) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsers(event.getUsers());
        structureBaseDTO(userDTO, event);
        return userDTO;
    }

    public static UserDTO buildUserRemoveDTO(UserLeaveOrgEvent event) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsers(event.getUsers());
        structureBaseDTO(userDTO, event);
        return userDTO;
    }

    public static UserModifyOrgDTO buildUserModifyOrgDTO(UserModifyOrgEvent event) {
        UserModifyOrgDTO userModifyOrgDTO = new UserModifyOrgDTO();
        userModifyOrgDTO.setOrgType(event.getOrgType());
        userModifyOrgDTO.setUserId(event.getUserId());
        userModifyOrgDTO.setAddOrgIds(event.getAddOrgIds());
        userModifyOrgDTO.setRemoveOrgIds(event.getRemoveOrgIds());
        structureBaseDTO(userModifyOrgDTO, event);
        return userModifyOrgDTO;
    }

    public static OrgModifyDTO buildOrgModifyDTO(OrgModifyEvent event) {
        OrgModifyDTO orgModifyDTO = new OrgModifyDTO();
        orgModifyDTO.setOrgType(event.getOrgType());
        orgModifyDTO.setOrgId(event.getOrgId());
        orgModifyDTO.setFromOrgId(event.getFromParentOrgId());
        orgModifyDTO.setToOrgId(event.getToParentOrgId());
        structureBaseDTO(orgModifyDTO, event);
        return orgModifyDTO;
    }

    public static ResourceDeletedDTO buildResourceDeleteDTO(ResourceDeleteEvent event) {
        ResourceDeletedDTO resourceDeletedDTO = new ResourceDeletedDTO();
        resourceDeletedDTO.setResourceType(event.getResourceType());
        resourceDeletedDTO.setResourceIds(event.getResourceIds());
        structureBaseDTO(resourceDeletedDTO, event);
        structureResourceCommonDTO(resourceDeletedDTO,event);
        return resourceDeletedDTO;
    }

    public static ResourceStatusDTO buildResourceStatusDTO(ResourceProgressEvent event) {
        ResourceStatusDTO resourceStatusDTO = new ResourceStatusDTO();
        resourceStatusDTO.setResourceId(event.getResourceId());
        resourceStatusDTO.setResourceType(event.getResourceType());
        resourceStatusDTO.setStatus(event.getStatus());
        resourceStatusDTO.setUserId(event.getUserId());
        structureBaseDTO(resourceStatusDTO, event);
        structureResourceCommonDTO(resourceStatusDTO,event);
        return resourceStatusDTO;
    }

    public static ResourcePublishStatusDTO buildResourcePublishStatusDTO(CoursePublishEvent event) {
        ResourcePublishStatusDTO resourcePublishStatusDTO = new ResourcePublishStatusDTO();
        resourcePublishStatusDTO.setResourceIds(event.getResourceIds());
        resourcePublishStatusDTO.setResourceType(event.getResourceType());
        resourcePublishStatusDTO.setPublishStatus(event.getPublishStatus());
        structureBaseDTO(resourcePublishStatusDTO, event);
        structureResourceCommonDTO(resourcePublishStatusDTO,event);
        return resourcePublishStatusDTO;
    }

    public static ResourceClassifyModifyDTO buildResourceClassifyModifyDTO(ClassifyModifyEvent event) {
        ResourceClassifyModifyDTO resourceClassifyModifyDTO = new ResourceClassifyModifyDTO();
        resourceClassifyModifyDTO.setResourceIds(event.getResourceIds());
        resourceClassifyModifyDTO.setResourceType(event.getResourceType());
        resourceClassifyModifyDTO.setNewClassifyIds(event.getNewClassifyIds());
        structureBaseDTO(resourceClassifyModifyDTO, event);
        structureResourceCommonDTO(resourceClassifyModifyDTO,event);
        return resourceClassifyModifyDTO;
    }

    public static ResourceTopDTO buildResourceTopDTO(CourseTopEvent event) {

        ResourceTopDTO resourceTopDTO = new ResourceTopDTO();
        resourceTopDTO.setResourceIds(event.getResourceIds());
        resourceTopDTO.setResourceType(event.getResourceType());
        resourceTopDTO.setSortNO(event.getSortNo());
        resourceTopDTO.setIsRecommend(event.getIsRecommend());
        structureBaseDTO(resourceTopDTO, event);
        structureResourceCommonDTO(resourceTopDTO,event);
        return resourceTopDTO;
    }

    public static AddOrChangeResourceDTO buildAddResourceDTO(ResourceAddEvent event) {
        AddOrChangeResourceDTO addOrChangeResourceDTO = new AddOrChangeResourceDTO();
        List<String> resourceIds = event.getResourceIds();
        addOrChangeResourceDTO.setResourceIds(resourceIds);
        addOrChangeResourceDTO.setResourceType(event.getResourceType());
        structureBaseDTO(addOrChangeResourceDTO, event);
        structureResourceCommonDTO(addOrChangeResourceDTO,event);
        return addOrChangeResourceDTO;
    }

    public static AddOrChangeResourceDTO buildChangeResourceDTO(ResourceChangeEvent event) {
        AddOrChangeResourceDTO changeResourceDTO = new AddOrChangeResourceDTO();
        List<String> resourceIds = event.getResourceIds();
        changeResourceDTO.setResourceIds(resourceIds);
        changeResourceDTO.setResourceType(event.getResourceType());
        structureBaseDTO(changeResourceDTO, event);
        structureResourceCommonDTO(changeResourceDTO,event);
        return changeResourceDTO;
    }
}
