package cn.coolcollege.fast.controller;

import cn.coolcollege.fast.authService.ISysDepartmentService;
import cn.coolcollege.fast.authService.ISysUserService;
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
@RequestMapping("/v1/sys/department")
public class SysDepartmentController {

    @Autowired
    private ISysDepartmentService sysDepartmentService;

    /**
     * 查询部门树
     * @param departmentId 部门id（查询某一部门下的树，不传时默认查询所有部门结构）
     * @param pageNumber 分页参数
     * @param pageSize 分页参数
     * @return
     */
    @GetMapping(value = "/list")
    public Object getDepartmentList(@RequestParam(value = "department_id", required = false) String departmentId,
                                    @RequestParam(value = "page_number", defaultValue = "1") Integer pageNumber,
                                    @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        return sysDepartmentService.getDepartmentList(departmentId, pageNumber, pageSize);
    }
}
