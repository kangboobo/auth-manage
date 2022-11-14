package cn.coolcollege.fast.storage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.coolcollege.fast.storage.entity.dao.SysRole;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:31
 * @description
 */
@Repository
public interface SysRoleMapper extends Mapper<SysRole> {

    Long insertSysRole(@Param("sysRole") SysRole sysRole);
}
