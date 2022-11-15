package cn.coolcollege.fast.authService.impl;

import cn.coolcollege.fast.authService.ISysUserService;
import cn.coolcollege.fast.config.SysUserConfig;
import cn.coolcollege.fast.constants.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/15 11:24
 * @description
 */
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserConfig sysUserConfig;

    /**
     * 查询用户列表
     * @param departmentId 部门id
     * @param keyword 关键词
     * @param pageNumber 分页参数
     * @param pageSize 分页参数
     * @return
     */
    @Override
    public BaseResponse getUserList(String departmentId, String keyword, Integer pageNumber, Integer pageSize) {
        return null;
    }
}
