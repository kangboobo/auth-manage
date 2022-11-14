package cn.coolcollege.fast.storage.mapper;

import cn.coolcollege.fast.storage.entity.dao.SysApp;
import cn.coolcollege.fast.storage.entity.dao.SysBase;
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
public interface SysBaseMapper extends Mapper<SysBase> {

    Integer insertSysBase(@Param("sysBase") SysBase sysBase);

    List<SysBase> selectSysBaseListByIds(@Param("ids") List<Long> ids);

}
