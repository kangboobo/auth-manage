package com.byd.auth.manage.dao.mapper;

import com.byd.auth.manage.dao.entity.dao.SysAppBase;
import com.byd.auth.manage.dao.entity.vo.AppBaseDto;
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

    List<AppBaseDto> selectSysAppBaseVosByBaseIds(@Param("list") List<Long> baseIds);
}
