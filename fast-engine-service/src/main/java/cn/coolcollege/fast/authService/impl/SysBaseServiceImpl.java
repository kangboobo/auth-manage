package cn.coolcollege.fast.authService.impl;

import java.util.List;
import java.util.Objects;

import cn.coolcollege.fast.storage.entity.vo.BaseAuthVo;
import cn.coolcollege.fast.storage.entity.vo.SysBaseVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.coolcollege.fast.authService.ISysBaseService;
import cn.coolcollege.fast.constants.BaseResponse;
import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.exception.AuthManageErrConstant;
import cn.coolcollege.fast.storage.entity.dao.SysAppBase;
import cn.coolcollege.fast.storage.entity.dao.SysBase;
import cn.coolcollege.fast.storage.mapper.SysAppBaseMapper;
import cn.coolcollege.fast.storage.mapper.SysBaseMapper;
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
            criteria.andLike("baseName", baseName);
        }
        List<SysBase> sysBaseList;
        try {
            sysBaseList = sysBaseMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("getSysBasePageList err", e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        if (Objects.isNull(sysBaseList)) {
            log.error("getSysBasePageList response is null");
            return BaseResponse.getFailedResponse(AuthManageErrConstant.RESPONSE_IS_NULL);
        }
        PageHelper.startPage(pageNumber, pageSize);
        return new PageInfo<>(sysBaseList);
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
        List<BaseAuthVo> baseAuthVos;
        try {
            sysBase = sysBaseMapper.selectByPrimaryKey(id);
            baseAuthVos = sysAppBaseMappingMapper.selectSysAppVosByBaseId(id);
        } catch (Exception e) {
            log.error("getSysBaseById err,id={}", id, e);
            return BaseResponse.getFailedResponse(AuthManageErrConstant.OPERATE_DB_ERR);
        }
        // 再根据应用-基地关联关系查询到对应的应用信息
        if (Objects.isNull(sysBase)) {
            return BaseResponse.getFailedResponse(AuthManageErrConstant.RESPONSE_IS_NULL);
        }
        SysBaseVo resultVo = convertParams(sysBase);
        resultVo.setAuthAppList(baseAuthVos);
        log.info("getSysBaseById::resultVos={}", JSON.toJSONString(resultVo));
        return BaseResponse.getSuccessResponse(resultVo);
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
        if (StringUtils.isAnyBlank(sysBaseVo.getBaseName(), sysBaseVo.getFactoryCode())) {
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
                insertAppBaseMappingList(sysBaseVo.getSysAppList(), baseId);
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
                insertAppBaseMappingList(sysBaseVo.getSysAppList(), sysBase.getId());
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
            deleteSysBase.setDeleteFlag(Constants.BYTE_ONE_VALUE);
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
    private SysBaseVo convertParams(SysBase SysBase) {
        SysBaseVo sysBaseVo = new SysBaseVo();
        sysBaseVo.setId(SysBase.getId());
        sysBaseVo.setBaseCode(SysBase.getBaseCode());
        sysBaseVo.setBaseName(SysBase.getBaseName());
        sysBaseVo.setFactoryCode(SysBase.getFactoryCode());
        sysBaseVo.setArea(SysBase.getArea());
        sysBaseVo.setDescription(SysBase.getDescription());
        sysBaseVo.setRemark(SysBase.getRemark());
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
