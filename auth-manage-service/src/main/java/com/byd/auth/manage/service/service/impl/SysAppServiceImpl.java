package com.byd.auth.manage.service.service.impl;

import java.util.List;
import java.util.Objects;

import com.byd.auth.manage.common.constants.BaseResponse;
import com.byd.auth.manage.common.constants.Constants;
import com.byd.auth.manage.common.exception.AuthManageErrConstant;
import com.byd.auth.manage.dao.entity.dao.SysApp;
import com.byd.auth.manage.dao.entity.dao.SysAppBase;
import com.byd.auth.manage.dao.mapper.SysAppBaseMapper;
import com.byd.auth.manage.dao.mapper.SysAppMapper;
import com.byd.auth.manage.service.service.ISysAppService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:55
 * @description
 */
@Service
@Slf4j
public class SysAppServiceImpl implements ISysAppService {

    @Autowired
    private SysAppMapper sysAppMapper;

    @Autowired
    private SysAppBaseMapper sysAppBaseMappingMapper;

    /**
     * 查询sysApp不分页
     *
     * @param appName
     * @return
     */
    @Override
    public Object getSysAppList(String appName) {
        log.info("getSysAppList::appName={}", appName);
        List<SysApp> sysAppList;
        try {
            sysAppList = querySysAppList(appName);
        } catch (Exception e) {
            log.error("getSysAppList query db error", e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        if (Objects.isNull(sysAppList)) {
            return BaseResponse.getFailedResponse(AuthManageErrConstant.RESPONSE_IS_NULL);
        }
        return BaseResponse.getSuccessResponse(sysAppList);
    }

    /**
     * 分页查询应用
     *
     * @param appName
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public Object getSysAppPageList(String appName, Integer pageNumber, Integer pageSize) {
        log.info("getSysAppPageList::appName={},pageNumber={},pageSize={}", appName, pageNumber, pageSize);
        if (pageNumber < Constants.INTEGER_ONE_VALUE || pageSize < Constants.ZERO_VALUE) {
            log.error("getSysAppPageList params invalid, appName={},pageNumber={},pageSize={}", appName, pageNumber,
                pageSize);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        List<SysApp> sysAppList;
        try {
            sysAppList = querySysAppList(appName);
        } catch (Exception e) {
            log.error("getSysAppList query db error", e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        if (Objects.isNull(sysAppList)) {
            log.error("getSysAppPageList response is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.RESPONSE_IS_NULL);
        }
        PageHelper.startPage(pageNumber, pageSize);
        return new PageInfo<>(sysAppList);
    }

    /**
     * 查询单个应用信息
     *
     * @param id
     *            appId
     */
    @Override
    public Object getSysAppById(Long id) {
        log.info("getSysAppById::id={}", id);
        if (Objects.isNull(id)) {
            log.error("getSysAppById::params invalid");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        SysApp sysApp;
        try {
            sysApp = sysAppMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("getSysAppById err,id={}", id, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse(sysApp);
    }

    /**
     * 保存权限应用
     *
     * @param sysApp
     * @return
     */
    @Override
    public Object insertOrUpdateSysApp(SysApp sysApp) {
        log.info("insertOrUpdateSysApp start, sysApp={}", sysApp.toString());
        if (StringUtils.isAnyBlank(sysApp.getAppName(), sysApp.getFactoryCode())) {
            log.error("insertOrUpdateSysApp::params invalid");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        try {
            if (Objects.isNull(sysApp.getId())) {
                // 新增应用
                sysApp.setCreateUser("");
                sysApp.setCreateTime(System.currentTimeMillis());
                sysAppMapper.insertSelective(sysApp);
            } else {
                // 修改应用
                sysApp.setUpdateUser("");
                sysApp.setUpdateTime(System.currentTimeMillis());
                sysAppMapper.updateByPrimaryKeySelective(sysApp);
            }
        } catch (Exception e) {
            log.error("insertOrUpdateSysApp err, sysApp={}", sysApp, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除权限应用
     *
     * @param id
     */
    @Override
    public Object deleteSysAppById(Long id) {
        log.info("deleteSysAppById start, id={}", id);
        if (Objects.isNull(id)) {
            log.error("deleteSysAppById::param is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        try {
            // 判断此应用底下是否关联基地，如果关联则不允许删除
            Example example = new Example(SysAppBase.class);
            example.createCriteria().andEqualTo("appId", id);
            List<SysAppBase> appBaseMappingList = sysAppBaseMappingMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(appBaseMappingList)) {
                return BaseResponse.getFailedResponse(AuthManageErrConstant.DELETE_NOT_ALLOWED);
            }
            // 逻辑删除
            SysApp deleteSysApp = new SysApp();
            deleteSysApp.setId(id);
            deleteSysApp.setDeleteFlag(Constants.BYTE_ONE_VALUE);
            deleteSysApp.setUpdateUser("");
            deleteSysApp.setUpdateTime(System.currentTimeMillis());
            sysAppMapper.updateByPrimaryKeySelective(deleteSysApp);
        } catch (Exception e) {
            log.error("deleteSysAppById err, id={}", id, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    @Override
    public List<SysApp> selectSysAppList(List<Long> appIds) {
        return sysAppMapper.selectSysAppListByIds(appIds);
    }

    /**
     * 查询sysApp列表
     * 
     * @param appName
     * @return
     */
    private List<SysApp> querySysAppList(String appName) {
        Example example = new Example(SysApp.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag", Constants.BYTE_ZERO_VALUE);
        if (StringUtils.isNotBlank(appName)) {
            criteria.andLike("appName", appName);
        }
        List<SysApp> sysAppList;
        try {
            sysAppList = sysAppMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("getSysAppPageList err", e);
            throw new IllegalArgumentException();
        }
        return sysAppList;
    }
}
