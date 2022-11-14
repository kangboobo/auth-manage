package cn.coolcollege.fast.storage.mapper;

import cn.coolcollege.fast.storage.entity.dao.SysApp;
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
public interface SysAppMapper extends Mapper<SysApp> {

    List<SysApp> selectSysAppListByIds(@Param("ids") List<Long> ids);
}
