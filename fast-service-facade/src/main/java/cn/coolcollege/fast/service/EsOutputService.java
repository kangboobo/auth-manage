package cn.coolcollege.fast.service;

import cn.coolcollege.fast.entity.request.CheckResourceVisibleRequest;
import cn.coolcollege.fast.entity.request.EsOutputRequest;
import cn.coolcollege.fast.entity.request.GetUserVisibleClassifyIdsRequest;
import cn.coolcollege.fast.entity.result.CheckResourceVisibleResult;
import cn.coolcollege.fast.entity.result.EsOutGroupCountResult;
import cn.coolcollege.fast.entity.result.EsOutputResult;
import cn.coolcollege.fast.entity.result.GetUserVisibleClassifyIdsResult;

/**
 * @Author bai bin
 * @Date 2021/5/7 19:55
 */
public interface EsOutputService {
    /**
     * es获取对应资源信息
     *
     * @param esOutputRequest
     * @return
     */
    EsOutputResult getPageEsOutputResourceInfo(EsOutputRequest esOutputRequest);

    /**
     * es查询资源分组数量统计
     * @param esOutputRequest
     * @return
     */
    EsOutGroupCountResult getResourceGroupCount(EsOutputRequest esOutputRequest);
    /**
     * es查询用户的所有分类id(包含子分类以及所有父分类id)
     * 
     * @param userVisibleClassifyIdsRequest
     * 
     * @return
     */
    GetUserVisibleClassifyIdsResult
        getUserVisibleClassifyIds(GetUserVisibleClassifyIdsRequest userVisibleClassifyIdsRequest);

    /**
     * 检查某人资源可见性
     * 
     * @param request
     * @return
     */
    CheckResourceVisibleResult checkResourceVisible(CheckResourceVisibleRequest request);
}
