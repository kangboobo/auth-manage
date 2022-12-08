package com.byd.auth.manage.service.employee.impl;

import com.alibaba.fastjson.JSONObject;
import com.byd.auth.manage.common.model.BaseResponse;
import com.byd.auth.manage.service.employee.DepartmentTypeEnum;
import com.byd.auth.manage.service.employee.IDepartmentService;
import com.byd.auth.manage.service.employee.LocalCacheUtil;
import com.byd.auth.manage.service.employee.model.Department;
import com.byd.auth.manage.service.employee.model.EmployeeDepartment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/12/2 13:36
 * @title
 * @description
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {


    @Override
    public BaseResponse getDepartmentList() {
        Department cache = (Department) LocalCacheUtil.load("root");
        if (cache != null) {
            return BaseResponse.getSuccessResponse(cache);
        }
        // TODO 查询用户部门列表，此处为模拟数据，需改造为从数据库查询
        List<EmployeeDepartment> employeeDepartments = listEmployeeDepartment();

        Department root = new Department();
        root.setName("第十一事业部");
        root.setType(DepartmentTypeEnum.division.name());
        root.setSub(new ArrayList<>());
        Map<String, Department> keyMap = new HashMap<>();
        for (EmployeeDepartment employeeDepartment : employeeDepartments) {
            if (StringUtils.isNotEmpty(employeeDepartment.getEmpOffice())) {
                // 科室
                buildOffice(root, keyMap, employeeDepartment);
            } else if (StringUtils.isNotEmpty(employeeDepartment.getEmpDepartment())) {
                // 部门
                buildDepartment(root, keyMap, employeeDepartment);
            } else if (StringUtils.isNotEmpty(employeeDepartment.getEmpFactory())) {
                // 工厂
                buildFactory(root, keyMap, employeeDepartment);
            }
        }
        LocalCacheUtil.save("root", root, 5 * 60 * 1000L);
        return BaseResponse.getSuccessResponse(root);
    }


    /**
     * 查询员工组织架构
     *
     * @return
     */
    private List<EmployeeDepartment> listEmployeeDepartment() {
        String json = "[\n" +
                "        {\n" +
                "            \"emp_division\": \"第十一事业部\",\n" +
                "            \"emp_factory\": \"总装深圳工厂\",\n" +
                "            \"emp_department\": \"技术部\",\n" +
                "            \"emp_office\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"emp_division\": \"第十一事业部\",\n" +
                "            \"emp_factory\": \"总装深圳工厂\",\n" +
                "            \"emp_department\": \"制造部\",\n" +
                "            \"emp_office\": \"分装工段\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"emp_division\": \"第十一事业部\",\n" +
                "            \"emp_factory\": \"总装西安工厂\",\n" +
                "            \"emp_department\": \"制造部\",\n" +
                "            \"emp_office\": \"总装工段\"\n" +
                "        }\n" +
                "    ]";
        return JSONObject.parseArray(json, EmployeeDepartment.class);
    }

    /**
     * 构建科室级组织
     *
     * @param root
     * @param keyMap
     * @param employeeDepartment
     */
    private void buildOffice(Department root, Map<String, Department> keyMap, EmployeeDepartment employeeDepartment) {
        Department office = new Department();
        office.setType(DepartmentTypeEnum.office.name());
        office.setName(employeeDepartment.getEmpOffice());
        office.setSub(Collections.EMPTY_LIST);
        if (StringUtils.isNotEmpty(employeeDepartment.getEmpDepartment())) {
            // 所属部门不为空时，构建科室的所属部门
            Department department = buildDepartment(root, keyMap, employeeDepartment);
            department.getSub().add(office);
        } else if (StringUtils.isNotEmpty(employeeDepartment.getEmpFactory())) {
            // 所属工厂不为空时，构建科室的所属工厂
            Department factory = buildFactory(root, keyMap, employeeDepartment);
            factory.getSub().add(office);
        } else {
            // 所属部门和工厂均为空时，科室直属于事业部
            root.getSub().add(office);
        }
    }

    /**
     * 构建部门级组织
     *
     * @param root
     * @param keyMap
     * @param employeeDepartment
     * @return
     */
    private Department buildDepartment(Department root, Map<String, Department> keyMap, EmployeeDepartment employeeDepartment) {
        String key = employeeDepartment.getEmpDepartment() + "_" + employeeDepartment.getEmpFactory();
        if (keyMap.containsKey(key)) {
            return keyMap.get(key);
        }
        Department department = new Department();
        department.setType(DepartmentTypeEnum.department.name());
        department.setName(employeeDepartment.getEmpDepartment());
        department.setSub(new ArrayList<>());
        if (StringUtils.isNotEmpty(employeeDepartment.getEmpFactory())) {
            // 所属工厂不为空时，构建部门的所属工厂
            Department factory = buildFactory(root, keyMap, employeeDepartment);
            factory.getSub().add(department);
        } else {
            // 所属工厂为空时，部门直属于事业部
            root.getSub().add(department);
        }
        keyMap.put(key, department);
        return department;
    }

    /**
     * 构建工厂级组织
     *
     * @param root
     * @param keyMap
     * @param employeeFactory
     * @return
     */
    private Department buildFactory(Department root, Map<String, Department> keyMap, EmployeeDepartment employeeFactory) {
        if (keyMap.containsKey(employeeFactory.getEmpFactory())) {
            return keyMap.get(employeeFactory.getEmpFactory());
        }
        Department factory = new Department();
        factory.setType(DepartmentTypeEnum.factory.name());
        factory.setName(employeeFactory.getEmpFactory());
        factory.setSub(new ArrayList<>());
        root.getSub().add(factory);
        keyMap.put(employeeFactory.getEmpFactory(), factory);
        return factory;
    }
}
