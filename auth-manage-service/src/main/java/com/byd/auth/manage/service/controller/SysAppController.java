package com.byd.auth.manage.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.battcn.boot.swagger.model.DataType;
import com.battcn.boot.swagger.model.ParamType;
import com.byd.auth.manage.dao.entity.dao.SysApp;
import com.byd.auth.manage.service.service.ISysAppService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:38
 * @description 应用管理controller
 */
@RestController
@RequestMapping("/v1/sys/app")
@Api(tags = "App", description = "系统应用api")
public class SysAppController {

    @Autowired
    private ISysAppService sysAppService;

    /**
     * 查询app列表不分页
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "查询应用列表", notes = "根据应用名称查询应用列表，应用名称为空时查询全部应用列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "app_name", value = "应用名称", dataType = DataType.STRING,
        paramType = ParamType.QUERY, defaultValue = "")})
    public Object getSysAppList(@RequestParam(value = "app_name", required = false) String appName) {
        return sysAppService.getSysAppList(appName);
    }

    /**
     * 查询app列表(分页)
     */
    @GetMapping(value = "/page/list")
    @ApiOperation(value = "查询应用列表", notes = "根据应用名称查询应用列表，应用名称为空时查询全部应用列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "app_name", value = "应用名称", dataType = DataType.STRING, paramType = ParamType.QUERY,
            defaultValue = "xxx"),
        @ApiImplicitParam(name = "page_number", value = "分页页码", dataType = DataType.INT, required = true,
            paramType = ParamType.QUERY, defaultValue = "1"),
        @ApiImplicitParam(name = "page_size", value = "分页行数", dataType = DataType.INT, required = true,
            paramType = ParamType.QUERY, defaultValue = "10")})
    public Object getPageSysAppList(@RequestParam(value = "app_name", required = false) String appName,
                                    @RequestParam(value = "page_number", defaultValue = "1") Integer pageNumber,
                                    @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        return sysAppService.getSysAppPageList(appName, pageNumber, pageSize);
    }

    /**
     * 根据单独appId查询
     */
    @GetMapping(value = "/{app_id:\\d+}/info")
    @ApiOperation(value = "查询应用信息", notes = "根据应用id查询单条应用信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "app_id", value = "应用id", dataType = DataType.LONG,
        paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object getSysAppById(@PathVariable(value = "app_id") Long appId) {
        return sysAppService.getSysAppById(appId);
    }

    /**
     * 新建｜修改应用
     * 
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "新增｜修改应用信息", notes = "新增修改应用信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "应用id", dataType = DataType.LONG, paramType = ParamType.BODY,
            defaultValue = "123L"),
        @ApiImplicitParam(name = "app_code", value = "应用编码", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "app_name", value = "应用名称", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "factory_code", value = "工厂代码", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "remark", value = "备注信息", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "remark", value = "备注信息", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
    })
    public Object insertOrUpdateSysApp(@RequestBody SysApp sysApp) {
        return sysAppService.insertOrUpdateSysApp(sysApp);
    }

    /**
     * 删除app
     */
    @DeleteMapping(value = "/{app_id:\\d+}/delete")
    @ApiOperation(value = "删除应用信息", notes = "根据应用id删除应用信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "app_id", value = "应用id", dataType = DataType.LONG,
        paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object deleteSysAppById(@PathVariable(value = "app_id") Long appId) {
        return sysAppService.deleteSysAppById(appId);
    }
}
