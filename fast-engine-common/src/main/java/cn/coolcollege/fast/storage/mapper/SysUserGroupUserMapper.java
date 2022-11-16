package cn.coolcollege.fast.storage.mapper;

import java.util.List;

import cn.coolcollege.fast.storage.entity.dao.SysUserGroupUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import tk.mybatis.mapper.common.Mapper;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/8 13:31
 * @description
 */
@Repository
public interface SysUserGroupUserMapper extends Mapper<SysUserGroupUser> {

    Integer insertList(@Param("list") List<SysUserGroupUser> list);
}
