package cn.coolcollege.fast.authService;

import cn.coolcollege.fast.constants.BaseResponse;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/15 11:16
 * @description 用户接口
 */
public interface ISysUserService {

    /**
     * 查询用户列表
     * @param departmentId 部门id
     * @param keyword 关键词
     * @param pageNumber 分页参数
     * @param pageSize 分页参数
     * @return
     */
    BaseResponse getUserList(String departmentId, String keyword, Integer pageNumber, Integer pageSize);
}
