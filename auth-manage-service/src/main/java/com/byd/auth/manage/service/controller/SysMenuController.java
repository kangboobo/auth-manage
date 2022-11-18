package com.byd.auth.manage.service.controller;

import com.byd.auth.manage.dao.entity.dao.SysMenu;
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
import com.byd.auth.manage.service.service.ISysMenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/17 16:58
 * @description 菜单管理controller
 */
@RestController
@RequestMapping("/v1/sys/menu")
@Api(tags = "Menu", description = "系统菜单api")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 查询菜单树列表
     */
    @GetMapping(value = "/tree")
    @ApiOperation(value = "查询菜单树列表", notes = "根据应用id、角色id查询菜单树列表，角色id非必传")
    @ApiImplicitParams({@ApiImplicitParam(name = "app_id", value = "应用id", dataType = DataType.LONG,
        required = true, paramType = ParamType.QUERY, defaultValue = "123L"),
                        @ApiImplicitParam(name = "role_id", value = "角色id", dataType = DataType.LONG,
                     paramType = ParamType.QUERY, defaultValue = "123L")})
    public Object getMenuTreeById(@RequestParam(value = "app_id") Long appId,
                                  @RequestParam(value = "role_id", required = false) Long roleId) {
        return sysMenuService.selectMenuTreeById(appId, roleId);
    }

    /**
     * 查询菜单id集合
     * @param appId 应用id
     * @param roleId 角色id
     * @return
     */
    @GetMapping(value = "/ids")
    @ApiOperation(value = "查询菜单id集合", notes = "根据应用id、角色id查询菜单id集合")
    @ApiImplicitParams({@ApiImplicitParam(name = "app_id", value = "应用id", dataType = DataType.LONG,
            required = true, paramType = ParamType.QUERY, defaultValue = "123L"),
            @ApiImplicitParam(name = "role_id", value = "角色id", dataType = DataType.LONG,
                    required = true,paramType = ParamType.QUERY, defaultValue = "123L")})
    public Object getMenuIdsById(@RequestParam(value = "app_id") Long appId,
                                 @RequestParam(value = "role_id") Long roleId){
        return sysMenuService.selectMenuIdsById(appId, roleId);
    }

    /**
     * 根据单条menuId查询
     */
    @GetMapping(value = "/{menu_id:\\d+}/info")
    @ApiOperation(value = "查询单条菜单信息", notes = "根据菜单id查询单条菜单信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "menu_id", value = "菜单id", dataType = DataType.LONG,
            paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object getSysAppById(@PathVariable(value = "menu_id") Long menuId) {
        return sysMenuService.selectSysMenuById(menuId);
    }

    /**
     * 新建｜修改菜单
     *
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "新增｜修改菜单信息", notes = "新增修改菜单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单id", dataType = DataType.LONG, paramType = ParamType.BODY,
                    defaultValue = "123L"),
            @ApiImplicitParam(name = "app_id", value = "应用id", dataType = DataType.LONG, required = true,
                    paramType = ParamType.BODY, defaultValue = "123L"),
            @ApiImplicitParam(name = "menu_name", value = "菜单名称", dataType = DataType.STRING, required = true,
                    paramType = ParamType.BODY, defaultValue = ""),
            @ApiImplicitParam(name = "parent_id", value = "父菜单id", dataType = DataType.LONG, required = true,
                    paramType = ParamType.BODY, defaultValue = ""),
            @ApiImplicitParam(name = "order_num", value = "排序id", dataType = DataType.INT, paramType = ParamType.BODY,
                    defaultValue = ""),
            @ApiImplicitParam(name = "path", value = "路由地址", dataType = DataType.STRING, required = true,
                    paramType = ParamType.BODY, defaultValue = ""),
            @ApiImplicitParam(name = "menu_type", value = "菜单类型(M:目录 C:菜单 F:按钮)", dataType = DataType.STRING, required = true,
                    paramType = ParamType.BODY, defaultValue = ""),
            @ApiImplicitParam(name = "remark", value = "备注信息", dataType = DataType.STRING,
                    paramType = ParamType.BODY, defaultValue = ""),
            @ApiImplicitParam(name = "delete_flag", value = "逻辑删除", dataType = DataType.INT, paramType = ParamType.BODY,
                    defaultValue = "0")})
    public Object insertOrUpdateSysMenu(@RequestBody SysMenu sysMenu) {
        return sysMenuService.insertOrUpdateSysMenu(sysMenu);
    }


    /**
     * 删除菜单
     */
    @DeleteMapping(value = "/{menu_id:\\d+}/delete")
    @ApiOperation(value = "删除单条菜单信息", notes = "根据菜单id删除单条菜单信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "menu_id", value = "菜单id", dataType = DataType.LONG,
        paramType = ParamType.PATH, required = true, defaultValue = "123L")})
    public Object deleteSysMenuById(@PathVariable(value = "menu_id") Long menuId) {
        return sysMenuService.deleteMenuById(menuId);
    }
}
