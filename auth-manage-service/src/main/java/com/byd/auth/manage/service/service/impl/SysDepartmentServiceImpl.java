package com.byd.auth.manage.service.service.impl;

import com.byd.auth.manage.service.properties.SysUserConfig;
import com.byd.auth.manage.common.model.BaseResponse;
import com.byd.auth.manage.common.exception.AuthManageErrConstant;
import com.alibaba.fastjson.JSONObject;
import com.byd.auth.manage.service.service.HttpRestTemplateService;
import com.byd.auth.manage.service.service.ISysDepartmentService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/15 11:24
 * @description
 */
@Service
@Slf4j
public class SysDepartmentServiceImpl implements ISysDepartmentService {

    @Autowired
    private SysUserConfig sysUserConfig;

    @Autowired
    private HttpRestTemplateService httpRestTemplateService;

    /**
     * 查询部门树
     * @param departmentId 部门id（查询某一部门下的树，不传时默认查询所有部门结构）
     * @param pageNumber 分页参数
     * @param pageSize 分页参数
     * @return
     */
    @Override
    public BaseResponse getDepartmentList(String departmentId, Integer pageNumber, Integer pageSize) {
        // 调用部门接口
        String apiUrl = null;
        JSONObject jsonObject = null;
        try {
            apiUrl = String.format(sysUserConfig.getGetDepartmentListApi(), departmentId, pageNumber, pageSize);
            String content = httpRestTemplateService.getForObject(apiUrl, String.class, Maps.newHashMap());
            jsonObject = JSONObject.parseObject(content);
        }catch (Exception e){
            log.error("getDepartmentList error, call api failed, apiUrl:{}", apiUrl, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.CALL_USER_API_FAILED);
        }
        Integer code = jsonObject.getInteger("code");
        if(!Objects.equals(0, code)){
            return BaseResponse.getFailedResponse(AuthManageErrConstant.CALL_USER_API_FAILED);
        }
        // 组织返回结果
        BaseResponse response = BaseResponse.getSuccessResponse(jsonObject.getJSONObject("data"));
        return response;
    }
}
