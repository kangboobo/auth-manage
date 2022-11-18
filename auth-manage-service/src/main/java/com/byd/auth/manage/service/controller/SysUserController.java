package com.byd.auth.manage.service.controller;

import com.battcn.boot.swagger.model.DataType;
import com.battcn.boot.swagger.model.ParamType;
import com.byd.auth.manage.common.model.BaseResponse;
import com.byd.auth.manage.service.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "User", description = "组织架构api")
@RequestMapping("/v1/sys/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "查询用户列表", notes = "查询某一部门下的用户，不递归下级部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department_id", value = "部门id", dataType = DataType.STRING, required = true, paramType = ParamType.QUERY, defaultValue = "xxx"),
            @ApiImplicitParam(name = "keyword", value = "用户姓名关键词", dataType = DataType.STRING, paramType = ParamType.QUERY, defaultValue = ""),
            @ApiImplicitParam(name = "page_number", value = "分页参数", dataType = DataType.INT, paramType = ParamType.QUERY, defaultValue = "1"),
            @ApiImplicitParam(name = "page_size", value = "分页参数", dataType = DataType.INT, paramType = ParamType.QUERY, defaultValue = "10")})
    public BaseResponse getUserList(@RequestParam(value = "department_id", required = true) String departmentId,
                                    @RequestParam(value = "keyword", required = false) String keyword,
                                    @RequestParam(value = "page_number", defaultValue = "1") Integer pageNumber,
                                    @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        return sysUserService.getUserList(departmentId, keyword, pageNumber, pageSize);
    }

}
