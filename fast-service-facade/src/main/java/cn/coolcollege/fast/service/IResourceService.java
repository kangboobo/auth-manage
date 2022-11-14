package cn.coolcollege.fast.service;

import cn.coolcollege.fast.entity.request.*;
import cn.coolcollege.fast.entity.result.*;

/**
 * @author bai bin
 * 获取resource facade interface
 */
public interface IResourceService {
    /**
     * 根据企业id批量查询资源信息(分页)
     *
     * @param getPageBatchResourceInfoRequest
     * @return
     */
    GetPageBatchResourceInfoResult getPageBatchResourceByEnterpriseId(GetPageBatchResourceInfoRequest getPageBatchResourceInfoRequest);

    /**
     * 根据资源id列表、资源类型查询对应资源详情列表
     *
     * @param getResourceDetailsByResourceIdsRequest
     * @return
     */
    GetResourceDetailsByResourceIdsResult getResourceDetailsByResourceIds(GetResourceDetailsByResourceIdsRequest getResourceDetailsByResourceIdsRequest);

    /**
     * 根据一批用户id查询直接挂在该用户的资源ids
     *
     * @param getDirectResourceIdsByUserIdsRequest
     * @return
     */
    GetDirectResourceIdsByUserIdsResult getDirectResourceIdsByUserIds(GetDirectResourceIdsByUserIdsRequest getDirectResourceIdsByUserIdsRequest);

    /**
     * 根据一批部门id查询直接挂在该部门的资源ids
     *
     * @param getDirectResourceIdsByOrgIdsRequest
     * @return
     */
    GetDirectResourceIdsByOrgIdsResult getDirectResourceIdsByDepartmentIds(GetDirectResourceIdsByOrgIdsRequest getDirectResourceIdsByOrgIdsRequest);

    /**
     * 根据一批用户组id查询直接挂在该用户组下的资源ids
     *
     * @param getDirectResourceIdsByOrgIdsRequest
     * @return
     */
    GetDirectResourceIdsByOrgIdsResult getDirectResourceIdsByUserGroupIds(GetDirectResourceIdsByOrgIdsRequest getDirectResourceIdsByOrgIdsRequest);

    /**
     * 根据一批岗位(组)id查询直接挂在该岗位（组）下的资源ids
     *
     * @param getDirectResourceIdsByOrgIdsRequest
     * @return
     */
    GetDirectResourceIdsByOrgIdsResult getDirectResourceIdsByPositionIds(GetDirectResourceIdsByOrgIdsRequest getDirectResourceIdsByOrgIdsRequest);


    /**
     * 查询一批资源分类id和所有上级分类id的映射
     *
     * @param request
     * @return
     */
    GetResourceClassifyRelationReverseListResult getResourceClassifyRelationReverse(GetResourceClassifyRelationRequest request);


    /**
     * 查询直接挂在分类下的且跟随分类可见性的资源id
     *
     * @param request
     * @return
     */
    GetPageDirectExtendResourceIdsByClassifyIdResult getPageDirectExtendResourceIdsByClassifyId(GetPageDirectExtendResourceIdsByClassifyIdRequest request);

    /**
     * 查询一批人创建的所有资源id
     *
     * @param request
     * @return
     */
    GetPageBatchResourceInfoResult getPageResourceIdByCreateUser(GetPageResourceIdByCreateUserRequest request);
}
