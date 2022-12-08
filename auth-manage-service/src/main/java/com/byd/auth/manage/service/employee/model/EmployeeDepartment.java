package com.byd.auth.manage.service.employee.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/12/2 13:39
 * @title 组织架构实体
 * @description
 */
@Data
public class EmployeeDepartment {

    /**
     * 所属事业部名称
     */
    @Column(name = "emp_division")
    @JSONField(name = "emp_division")
    private String empDivision;

    /**
     * 所属工厂名称
     */
    @Column(name = "emp_factory")
    @JSONField(name = "emp_factory")
    private String empFactory;

    /**
     * 所属部门名称
     */
    @Column(name = "emp_department")
    @JSONField(name = "emp_factory")
    private String empDepartment;

    /**
     * 所属科室名称
     */
    @Column(name = "emp_office")
    @JSONField(name = "emp_factory")
    private String empOffice;

}
