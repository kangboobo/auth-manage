package com.byd.auth.manage.facade.controller;

import com.byd.auth.manage.facade.authService.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/15 11:02
 * @description 用户controller类
 */
@RestController
@RequestMapping("/v1/sys/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询用户列表
     * @param departmentId 部门id
     * @param keyword 关键词
     * @param pageNumber 分页参数
     * @param pageSize 分页参数
     * @return
     */
    @GetMapping(value = "/list")
    public Object getUserList(@RequestParam(value = "department_id", required = true) String departmentId,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "page_number", defaultValue = "1") Integer pageNumber,
                              @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        return sysUserService.getUserList(departmentId, keyword, pageNumber, pageSize);
    }

}
