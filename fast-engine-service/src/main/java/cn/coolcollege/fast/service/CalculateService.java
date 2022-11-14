package cn.coolcollege.fast.service;

import cn.coolcollege.fast.model.*;

/**
 * The calculate service for all the DTO.
 *
 * @author majian
 */
public interface CalculateService {

    /**
     * user add to one organization
     *
     * @param user is the DTO contains a list of user with all information
     */
    void userAddToOrg(UserDTO user);

    /**
     * user leave one organization
     *
     * @param user
     */
    void userLeaveOrg(UserDTO user);

    /**
     * organization change
     *
     * @param orgModify is the DTO of user change
     */
    void orgChange(OrgModifyDTO orgModify);

    /**
     * user changes organization
     *
     * @param userModifyOrg
     */
    void userModifyOrg(UserModifyOrgDTO userModifyOrg);

    /**
     * add or change resource
     *
     * @param resourceDTO
     */
    void addOrChangeResource(AddOrChangeResourceDTO resourceDTO);


    /**
     * remove resource
     *
     * @param resourceDeletedDTO contains information of deleted resource.
     */
    void removeResource(ResourceDeletedDTO resourceDeletedDTO);

    /**
     * @param resourceStatusDTO contains the info for status-changed resource
     */
    void resourceStatusChange(ResourceStatusDTO resourceStatusDTO);

    /***
     *
     * @param resourcePublishStatusDTO contains the changed publish status of resource
     */
    void resourcePublishChange(ResourcePublishStatusDTO resourcePublishStatusDTO);

    /**
     * @param resourceTopDTO is the objects contains resource top related info
     */
    void resourceTopChange(ResourceTopDTO resourceTopDTO);

    /**
     * @param resourceClassifyModifyDTO contains resource new_classify_ids
     */
    void resourceClassifyModify(ResourceClassifyModifyDTO resourceClassifyModifyDTO);
}
