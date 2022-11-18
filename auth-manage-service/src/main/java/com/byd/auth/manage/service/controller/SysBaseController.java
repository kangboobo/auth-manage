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
import com.byd.auth.manage.dao.entity.vo.SysBaseVo;
import com.byd.auth.manage.service.service.ISysBaseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:38
 * @description 基地管理controller类
 */
@RestController
@RequestMapping("/v1/sys/base")
@Api(tags = "Base", description = "系统基地api")
public class SysBaseController {

    @Autowired
    private ISysBaseService sysBaseService;

    /**
     * 查询base列表(分页)
     */
    @GetMapping(value = "/page/list")
    @ApiOperation(value = "分页查询基地列表", notes = "根据基地名称查询基地列表，基地名称为空时查询全部基地列表(分页)")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "base_name", value = "基地名称", dataType = DataType.STRING, paramType = ParamType.QUERY,
            defaultValue = "xxx"),
        @ApiImplicitParam(name = "page_number", value = "分页页码", dataType = DataType.INT, required = true,
            paramType = ParamType.QUERY, defaultValue = "1"),
        @ApiImplicitParam(name = "page_size", value = "分页行数", dataType = DataType.INT, required = true,
            paramType = ParamType.QUERY, defaultValue = "10")})
    public Object getPageSysBaseList(@RequestParam(value = "base_name", required = false) String baseName,
                                     @RequestParam(value = "page_number", defaultValue = "1") Integer pageNumber,
                                     @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        return sysBaseService.getSysBasePageList(baseName, pageNumber, pageSize);
    }

    /**
     * 根据单独baseId查询
     */
    @GetMapping(value = "/{base_id:\\d+}/info")
    @ApiOperation(value = "查询单条基地信息", notes = "根据基地id查询单条基地信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "base_id", value = "基地id", dataType = DataType.LONG,
        paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object getSysBaseById(@PathVariable(value = "base_id") Long baseId) {
        return sysBaseService.getSysBaseById(baseId);
    }

    /**
     * 新建｜修改基地
     * 
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "新增｜修改基地信息", notes = "新增修改基地信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "基地id", dataType = DataType.LONG, paramType = ParamType.BODY,
            defaultValue = "123L"),
        @ApiImplicitParam(name = "base_code", value = "基地编码", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "base_name", value = "基地名称", dataType = DataType.STRING, required = true,
            paramType = ParamType.BODY, defaultValue = ""),
        @ApiImplicitParam(name = "factory_code", value = "工厂代码", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "area", value = "基地面积", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "description", value = "基地描述", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "remark", value = "备注信息", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "delete_flag", value = "逻辑删除", dataType = DataType.INT, paramType = ParamType.BODY,
            defaultValue = "0")})
    public Object insertOrUpdateSysBase(@RequestBody SysBaseVo sysBaseVo) {
        return sysBaseService.insertOrUpdateSysBase(sysBaseVo);
    }

    /**
     * 删除单条基地信息
     */
    @DeleteMapping(value = "/{base_id:\\d+}/delete")
    @ApiOperation(value = "删除单条基地信息", notes = "根据基地id删除单条基地信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "base_id", value = "基地id", dataType = DataType.LONG,
        paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object deleteSysBaseById(@PathVariable(value = "base_id") Long baseId) {
        return sysBaseService.deleteSysBaseById(baseId);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "查询基地列表", notes = "根据应用id查询基地列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "app_id", value = "应用id", dataType = DataType.LONG,
        paramType = ParamType.QUERY, defaultValue = "123L")})
    public Object getSysBaseList(@RequestParam(value = "app_id") Long appId) {
        return sysBaseService.getSysBaseList(appId);
    }
}
