package com.byd.auth.manage.service.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.byd.auth.manage.common.model.BaseResponse;
import com.byd.auth.manage.common.constants.Constants;
import com.byd.auth.manage.common.exception.AuthManageErrConstant;
import com.byd.auth.manage.dao.entity.dao.SysAppBase;
import com.byd.auth.manage.dao.entity.dao.SysBase;
import com.byd.auth.manage.dao.entity.dto.AppBaseDto;
import com.byd.auth.manage.dao.entity.vo.BaseAuthVo;
import com.byd.auth.manage.dao.entity.vo.SysBaseVo;
import com.byd.auth.manage.dao.mapper.SysAppBaseMapper;
import com.byd.auth.manage.dao.mapper.SysBaseMapper;
import com.byd.auth.manage.service.service.ISysBaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/9 10:55
 * @description 基地管理Service类
 */
@Service
@Slf4j
public class SysBaseServiceImpl implements ISysBaseService {

    @Autowired
    private SysBaseMapper sysBaseMapper;

    @Autowired
    private SysAppBaseMapper sysAppBaseMappingMapper;

    /**
     * 分页查询基地
     *
     * @param baseName
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public Object getSysBasePageList(String baseName, Integer pageNumber, Integer pageSize) {
        log.info("getSysBasePageList::baseName={},pageNumber={},pageSize={}", baseName, pageNumber, pageSize);
        if (pageNumber < Constants.INTEGER_ONE_VALUE || pageSize < Constants.ZERO_VALUE) {
            log.error("getSysBasePageList params invalid, baseName={},pageNumber={},pageSize={}", baseName, pageNumber,
                pageSize);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        Example example = new Example(SysBase.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag" ,Constants.BYTE_ZERO_VALUE);
        if (StringUtils.isNotBlank(baseName)) {
            criteria.andLike("baseName", "%" + baseName + "%");
        }
        example.orderBy("createTime").desc();
        List<SysBase> sysBaseList;
        List<AppBaseDto> appBaseDtos = null;
        try {
            sysBaseList = sysBaseMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(sysBaseList)) {
                List<Long> baseIds = sysBaseList.stream().map(SysBase::getId).collect(Collectors.toList());
                appBaseDtos = sysAppBaseMappingMapper.selectSysAppBaseVosByBaseIds(baseIds);
            }
        } catch (Exception e) {
            log.error("getSysBasePageList err", e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        if (Objects.isNull(sysBaseList)) {
            log.error("getSysBasePageList response is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.RESPONSE_IS_NULL);
        }
        Map<Long, List<AppBaseDto>> appBaseMap = null;
        if (CollectionUtils.isNotEmpty(appBaseDtos)) {
            appBaseMap = appBaseDtos.stream().collect(Collectors.groupingBy(AppBaseDto::getBaseId));
        }
        List<SysBaseVo> sysBaseVoList = Lists.newArrayList();
        Map<Long, List<AppBaseDto>> finalAppBaseMap = appBaseMap;
        sysBaseList.forEach(sysBase -> {
            SysBaseVo sysBaseVo = convertParams(sysBase);
            if (MapUtils.isNotEmpty(finalAppBaseMap) && finalAppBaseMap.containsKey(sysBase.getId())) {
                List<AppBaseDto> appBaseDtoList = finalAppBaseMap.get(sysBase.getId());
                List<BaseAuthVo> subAppList = Lists.newArrayList();
                for (AppBaseDto subDto: appBaseDtoList) {
                    BaseAuthVo subApp = new BaseAuthVo();
                    subApp.setId(subDto.getAppId());
                    subApp.setName(subDto.getAppName());
                    subAppList.add(subApp);
                }
                sysBaseVo.setSysApps(subAppList);
            }
            sysBaseVoList.add(sysBaseVo);
        });
        PageHelper.startPage(pageNumber, pageSize);
        return BaseResponse.getSuccessResponse(new PageInfo(sysBaseVoList));
    }

    /**
     * 查询单个基地信息
     *
     * @param id
     */
    @Override
    public Object getSysBaseById(Long id) {
        log.info("getSysBaseById::id={}", id);
        if (Objects.isNull(id)) {
            log.error("getSysBaseById::params invalid");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        SysBase sysBase;
        List<AppBaseDto> appBaseDtoList;
        try {
            sysBase = sysBaseMapper.selectByPrimaryKey(id);
            appBaseDtoList = sysAppBaseMappingMapper.selectSysAppBaseVosByBaseIds(Lists.newArrayList(id));
        } catch (Exception e) {
            log.error("getSysBaseById err,id={}", id, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        // 再根据应用-基地关联关系查询到对应的应用信息
        if (Objects.isNull(sysBase)) {
            return BaseResponse.getFailedResponse(AuthManageErrConstant.RESPONSE_IS_NULL);
        }
        SysBaseVo baseVo = convertParams(sysBase);
        List<BaseAuthVo> sysAppList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(appBaseDtoList)) {
            appBaseDtoList.forEach(appBaseDto -> {
                if (Objects.equals(appBaseDto.getBaseId(), baseVo.getId())) {
                    BaseAuthVo sysApp = new BaseAuthVo();
                    sysApp.setId(appBaseDto.getAppId());
                    sysApp.setName(appBaseDto.getAppName());
                    sysAppList.add(sysApp);
                }
            });
        }
        baseVo.setSysApps(sysAppList);
        log.info("getSysBaseById::resultVos={}", JSON.toJSONString(baseVo));
        return BaseResponse.getSuccessResponse(baseVo);
    }

    /**
     * 新增｜修改基地
     *
     * @param sysBaseVo
     * @return
     */
    @Override
    public Object insertOrUpdateSysBase(SysBaseVo sysBaseVo) {
        log.info("insertOrUpdateSysBase start, sysApp={}", JSON.toJSONString(sysBaseVo));
        if (StringUtils.isAnyBlank(sysBaseVo.getBaseName())) {
            log.error("insertOrUpdateSysBase::params invalid");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        try {
            SysBase sysBase = convertParams(sysBaseVo);
            if (Objects.isNull(sysBase.getId())) {
                // 新增基地
                sysBase.setCreateUser("");
                sysBase.setCreateTime(System.currentTimeMillis());
                sysBaseMapper.insertSysBase(sysBase);
                Long baseId = sysBase.getId();
                if (Objects.isNull(baseId)) {
                    log.error("insertOrUpdateSysBase::baseId error return");
                    throw new IllegalArgumentException();
                }
                // 新增基地和应用的绑定关系
                insertAppBaseMappingList(sysBaseVo.getSysApps(), baseId);
            } else {
                // 编辑基地
                sysBase.setUpdateUser("");
                sysBase.setUpdateTime(System.currentTimeMillis());
                sysBaseMapper.updateByPrimaryKeySelective(sysBase);
                // 删除此基地关联的源应用
                Example deleteExample = new Example(SysAppBase.class);
                deleteExample.createCriteria().andEqualTo("baseId", sysBase.getId());
                sysAppBaseMappingMapper.deleteByExample(deleteExample);
                // 增加此基地此次修改的应用
                insertAppBaseMappingList(sysBaseVo.getSysApps(), sysBase.getId());
            }
        } catch (Exception e) {
            log.error("insertOrUpdateSysBase err, sysApp={}", sysBaseVo, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除单个基地
     *
     * @param id
     */
    @Override
    public Object deleteSysBaseById(Long id) {
        log.info("deleteSysBaseById start, id={}", id);
        if (Objects.isNull(id)) {
            log.error("deleteSysBaseById::param is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.PARAMS_INVALID);
        }
        try {
            // 逻辑删除
            SysBase deleteSysBase = new SysBase();
            deleteSysBase.setId(id);
            deleteSysBase.setDeleteFlag(Constants.INTEGER_ONE_VALUE);
            deleteSysBase.setUpdateUser("");
            deleteSysBase.setUpdateTime(System.currentTimeMillis());
            sysBaseMapper.updateByPrimaryKeySelective(deleteSysBase);
            // 删除应用基地关联关系
            Example example = new Example(SysAppBase.class);
            example.createCriteria().andEqualTo("baseId", id);
            sysAppBaseMappingMapper.deleteByExample(example);
        } catch (Exception e) {
            log.error("deleteSysBaseById err, id={}", id, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 根据基地id集合查询基地信息列表
     *
     * @param baseIds
     * @return
     */
    @Override
    public List<SysBase> selectSysBaseList(List<Long> baseIds) {
        return sysBaseMapper.selectSysBaseListByIds(baseIds);
    }

    /**
     * 查询基地列表(不分页)
     *
     * @param appId
     * @return
     */
    @Override
    public Object getSysBaseList(Long appId) {
        List<SysBase> sysBaseList;
        try {
            sysBaseList = sysBaseMapper.selectSysBaseListByAppId(appId);
        } catch (Exception e) {
            log.error("getSysBaseList err, appId={}", appId, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        return BaseResponse.getSuccessResponse(sysBaseList);
    }

    /**
     * 转化参数 SysBaseVo 转 SysBase
     * 
     * @return
     */
    private SysBase convertParams(SysBaseVo sysBaseVo) {
        SysBase sysBase = new SysBase();
        sysBase.setId(sysBaseVo.getId());
        sysBase.setBaseCode(sysBaseVo.getBaseCode());
        sysBase.setBaseName(sysBaseVo.getBaseName());
        sysBase.setFactoryCode(sysBaseVo.getFactoryCode());
        sysBase.setArea(sysBaseVo.getArea());
        sysBase.setDescription(sysBaseVo.getDescription());
        sysBase.setRemark(sysBaseVo.getRemark());
        return sysBase;
    }

    /**
     * 转化参数 SysBaseVo 转 SysBase
     * 
     * @return
     */
    private SysBaseVo convertParams(SysBase sysBase) {
        SysBaseVo sysBaseVo = new SysBaseVo();
        sysBaseVo.setId(sysBase.getId());
        sysBaseVo.setBaseCode(sysBase.getBaseCode());
        sysBaseVo.setBaseName(sysBase.getBaseName());
        sysBaseVo.setFactoryCode(sysBase.getFactoryCode());
        sysBaseVo.setArea(sysBase.getArea());
        sysBaseVo.setDescription(sysBase.getDescription());
        sysBaseVo.setRemark(sysBase.getRemark());
        sysBaseVo.setDeleteFlag(sysBase.getDeleteFlag());
        sysBaseVo.setCreateTime(sysBase.getCreateTime());
        sysBaseVo.setUpdateTime(sysBase.getUpdateTime());
        return sysBaseVo;
    }

    /**
     * 批量增加
     * 
     * @param baseId
     */
    private void insertAppBaseMappingList(List<BaseAuthVo> sysAppList, Long baseId) {
        // 新增基地和应用的绑定关系
        log.info("insertAppBaseMappingList::sysAppList={},baseId={}", JSON.toJSONString(sysAppList), baseId);
        if (CollectionUtils.isNotEmpty(sysAppList) && Objects.nonNull(baseId)) {
            List<SysAppBase> sysAppBaseMappingList = Lists.newArrayList();
            sysAppList.forEach(v -> {
                SysAppBase sysAppBaseMapping = new SysAppBase();
                sysAppBaseMapping.setAppId(v.getId());
                sysAppBaseMapping.setBaseId(baseId);
                sysAppBaseMappingList.add(sysAppBaseMapping);
            });
            sysAppBaseMappingMapper.insertList(sysAppBaseMappingList);
        }
    }
}
