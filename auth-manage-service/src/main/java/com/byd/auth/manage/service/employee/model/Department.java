package com.byd.auth.manage.service.employee.model;

import lombok.Data;

import java.util.List;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/12/2 13:39
 * @title 组织架构实体
 * @description
 */
@Data
public class Department {

    /**
     * 部门类型
     *
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 子部门列表
     */
    private List<Department> sub;
}
