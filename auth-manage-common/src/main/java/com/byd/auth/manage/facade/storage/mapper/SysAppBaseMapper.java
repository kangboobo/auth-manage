package com.byd.auth.manage.facade.storage.mapper;

import com.byd.auth.manage.facade.storage.entity.dao.SysAppBase;
import com.byd.auth.manage.facade.storage.entity.vo.BaseAuthVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:31
 * @description
 */
@Repository
public interface SysAppBaseMapper extends Mapper<SysAppBase> {

    Integer insertList(@Param("list") List<SysAppBase> list);

    List<BaseAuthVo> selectSysAppVosByBaseId(@Param("baseId") Long baseId);
}
