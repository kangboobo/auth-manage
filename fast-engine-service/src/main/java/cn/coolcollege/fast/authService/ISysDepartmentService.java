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
     * @param departmentId 部门id（查询某一部门下的树，不传时默认查询所有部门结构）
     * @param pageNumber 分页参数
     * @param pageSize 分页参数
     * @return
     */
    BaseResponse getDepartmentList(String departmentId, Integer pageNumber, Integer pageSize);
}
