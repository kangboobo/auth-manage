package cn.coolcollege.fast.authService;

import cn.coolcollege.fast.constants.BaseResponse;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/15 11:16
 * @description 部门接口
 */
public interface ISysDepartmentService {

    /**
     * 查询部门树
     * @param keyword 关键词
     * @param pageNumber 分页参数
     * @param pageSize 分页参数
     * @return
     */
    BaseResponse getDepartmentList(String keyword, Integer pageNumber, Integer pageSize);
}
