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
import com.byd.auth.manage.dao.entity.vo.SysRoleVo;
import com.byd.auth.manage.service.service.ISysRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/18 11:52
 * @description 角色管理controller
 */
@RestController
@RequestMapping("/v1/sys/role")
@Api(tags = "Role", description = "系统角色api")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 根据单条roleId查询
     */
    @GetMapping(value = "/{role_id:\\d+}/info")
    @ApiOperation(value = "查询单条角色信息", notes = "根据角色id查询单条角色信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "role_id", value = "角色id", dataType = DataType.LONG,
        paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object getSysAppById(@PathVariable(value = "role_id") Long roleId) {
        return sysRoleService.getSysRoleById(roleId);
    }

    /**
     * 查询角色列表
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "查询角色列表", notes = "根据角色名称查询角色列表，角色名称非必传")
    @ApiImplicitParams({@ApiImplicitParam(name = "role_name", value = "角色名称", dataType = DataType.STRING,
        paramType = ParamType.QUERY, defaultValue = "")})
    public Object getSysRoleList(@RequestParam(value = "role_name", required = false) String roleName) {
        return sysRoleService.getSysRoleList(roleName);
    }

    /**
     * 新建｜修改角色
     *
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "新增｜修改角色信息", notes = "新增修改角色信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "角色id", dataType = DataType.LONG, paramType = ParamType.BODY,
            defaultValue = "123L"),
        @ApiImplicitParam(name = "role_name", value = "角色名称", dataType = DataType.STRING, required = true,
            paramType = ParamType.BODY, defaultValue = ""),
        @ApiImplicitParam(name = "role_sort", value = "角色排序", dataType = DataType.INT,
            paramType = ParamType.BODY, defaultValue = "1L"),
        @ApiImplicitParam(name = "remark", value = "备注信息", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "delete_flag", value = "逻辑删除", dataType = DataType.INT, paramType = ParamType.BODY,
            defaultValue = "0")})
    public Object insertOrUpdateRole(@RequestBody SysRoleVo sysRoleVo) {
        return sysRoleService.insertOrUpdateRole(sysRoleVo);
    }

    /**
     * 删除角色
     */
    @DeleteMapping(value = "/{role_id:\\d+}/delete")
    @ApiOperation(value = "删除单条角色信息", notes = "根据角色id删除单条角色信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "role_id", value = "角色id", dataType = DataType.LONG,
        paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object deleteSysMenuById(@PathVariable(value = "role_id") Long roleId) {
        return sysRoleService.deleteSysRoleById(roleId);
    }
}
