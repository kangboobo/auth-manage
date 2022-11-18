package com.byd.auth.manage.service.controller;

import com.battcn.boot.swagger.model.DataType;
import com.battcn.boot.swagger.model.ParamType;
import com.byd.auth.manage.common.model.BaseResponse;
import com.byd.auth.manage.service.service.ISysDepartmentService;
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
 * @date 2022/11/15 11:11
 * @description 部门controller类
 */
@RestController
@Api(tags = "User", description = "组织架构api")
@RequestMapping("/v1/sys/department")
public class SysDepartmentController {

    @Autowired
    private ISysDepartmentService sysDepartmentService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "查询部门列表", notes = "递归返回树形结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "department_id", value = "部门id", dataType = DataType.STRING, paramType = ParamType.QUERY, defaultValue = "xxx"),
            @ApiImplicitParam(name = "page_number", value = "分页参数", dataType = DataType.INT, paramType = ParamType.QUERY, defaultValue = "1"),
            @ApiImplicitParam(name = "page_size", value = "分页参数", dataType = DataType.INT, paramType = ParamType.QUERY, defaultValue = "10")})
    public BaseResponse getDepartmentList(@RequestParam(value = "department_id", required = false) String departmentId,
                                          @RequestParam(value = "page_number", defaultValue = "1") Integer pageNumber,
                                          @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        return sysDepartmentService.getDepartmentList(departmentId, pageNumber, pageSize);
    }
}
