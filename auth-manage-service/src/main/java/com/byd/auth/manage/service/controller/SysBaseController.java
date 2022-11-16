package com.byd.auth.manage.common.controller;

import com.byd.auth.manage.common.storage.entity.vo.SysBaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.byd.auth.manage.common.authService.ISysBaseService;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:38
 * @description 基地管理controller类
 */
@RestController
@RequestMapping("/v1/sys/base")
public class SysBaseController {

    @Autowired
    private ISysBaseService ISysBaseService;

    /**
     * 查询base列表(分页)
     */
    @GetMapping(value = "/page/list")
    public Object getPageSysBaseList(@RequestParam(value = "base_name", required = false) String baseName,
                                     @RequestParam(value = "page_number", defaultValue = "1") Integer pageNumber,
                                     @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        return ISysBaseService.getSysBasePageList(baseName, pageNumber, pageSize);
    }

    /**
     * 根据单独baseId查询
     */
    @GetMapping(value = "/{base_id:\\d+}/info")
    public Object getSysBaseById(@PathVariable(value = "base_id") Long baseId) {
        return ISysBaseService.getSysBaseById(baseId);
    }

    /**
     * 新建｜修改基地
     * 
     * @return
     */
    @PostMapping(value = "/save")
    public Object insertOrUpdateSysBase(@RequestBody SysBaseVo sysBaseVo) {
        return ISysBaseService.insertOrUpdateSysBase(sysBaseVo);
    }

    /**
     * 删除app
     */
    @DeleteMapping(value = "/{base_id:\\d+}/delete")
    public Object deleteSysBaseById(@PathVariable(value = "base_id") Long baseId) {
        return ISysBaseService.deleteSysBaseById(baseId);
    }
}
