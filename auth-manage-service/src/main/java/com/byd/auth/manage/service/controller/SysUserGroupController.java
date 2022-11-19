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
import com.byd.auth.manage.dao.entity.vo.SysUserGroupUserVo;
import com.byd.auth.manage.dao.entity.vo.SysUserGroupVo;
import com.byd.auth.manage.service.service.ISysUserGroupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/19 10:34
 * @description 用户组管理controller
 */
@RestController
@RequestMapping("/v1/sys/userGroup")
@Api(tags = "UserGroup", description = "系统用户组api")
public class SysUserGroupController {

    @Autowired
    private ISysUserGroupService userGroupService;

    /**
     * 根据单条userGroupId查询
     */
    @GetMapping(value = "/{user_group_id:\\d+}/info")
    @ApiOperation(value = "查询单条用户组信息", notes = "根据用户组id查询单条用户组信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "user_group_id", value = "用户组id", dataType = DataType.LONG,
        paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object getSysUserGroupById(@PathVariable(value = "user_group_id") Long userGroupId) {
        return userGroupService.getSysUserGroupById(userGroupId);
    }

    /**
     * 根据用户组id查询用户信息
     */
    @GetMapping(value = "/{user_group_id:\\d+}/users")
    @ApiOperation(value = "查询用户信息", notes = "根据用户组id查询用户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "user_group_id", value = "用户组id", dataType = DataType.LONG,
        paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object getSysUsersById(@PathVariable(value = "user_group_id") Long userGroupId) {
        return userGroupService.getSysUsersById(userGroupId);
    }

    /**
     * 查询用户组列表(分页)
     */
    @GetMapping(value = "/page/list")
    @ApiOperation(value = "分页查询用户组列表", notes = "根据用户组名称查询用户组列表，用户组名称为空时查询全部用户组列表(分页)")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "user_group_name", value = "用户组名称", dataType = DataType.STRING,
            paramType = ParamType.QUERY, defaultValue = "xxx"),
        @ApiImplicitParam(name = "page_number", value = "分页页码", dataType = DataType.INT, required = true,
            paramType = ParamType.QUERY, defaultValue = "1"),
        @ApiImplicitParam(name = "page_size", value = "分页行数", dataType = DataType.INT, required = true,
            paramType = ParamType.QUERY, defaultValue = "10")})
    public Object getPageSysUserGroupList(
        @RequestParam(value = "user_group_name", required = false) String userGroupName,
        @RequestParam(value = "page_number", defaultValue = "1") Integer pageNumber,
        @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        return userGroupService.getPageSysUserGroupList(userGroupName, pageNumber, pageSize);
    }

    /**
     * 新建｜修改用户组
     *
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "新增｜修改用户组信息", notes = "新增修改用户组信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "角色id", dataType = DataType.LONG, paramType = ParamType.BODY,
            defaultValue = "123L"),
        @ApiImplicitParam(name = "user_group_name", value = "用户组名称", dataType = DataType.STRING, required = true,
            paramType = ParamType.BODY, defaultValue = ""),
        @ApiImplicitParam(name = "remark", value = "备注信息", dataType = DataType.STRING, paramType = ParamType.BODY,
            defaultValue = ""),
        @ApiImplicitParam(name = "delete_flag", value = "逻辑删除", dataType = DataType.INT, paramType = ParamType.BODY,
            defaultValue = "0")})
    public Object insertOrUpdateRole(@RequestBody SysUserGroupVo sysUserGroupVo) {
        return userGroupService.insertOrUpdateUserGroup(sysUserGroupVo);
    }

    @PostMapping(value = "/users/save")
    @ApiOperation(value = "新增用户组用户关联关系", notes = "新增用户组用户关联关系")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "用户组id", dataType = DataType.LONG, required = true,
        paramType = ParamType.BODY, defaultValue = "123L")})
    public Object a(@RequestBody SysUserGroupUserVo userGroupUserVo) {
        return userGroupService.saveUserGroupUser(userGroupUserVo);
    }

    /**
     * 删除用户组
     */
    @DeleteMapping(value = "/{user_group_id:\\d+}/delete")
    @ApiOperation(value = "删除单条用户组信息", notes = "根据用户组id删除单条用户组信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "user_group_id", value = "用户组id", dataType = DataType.LONG,
        paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object deleteSysUserGroupById(@PathVariable(value = "user_group_id") Long userGroupId) {
        return userGroupService.deleteSysUserGroupById(userGroupId);
    }
}
