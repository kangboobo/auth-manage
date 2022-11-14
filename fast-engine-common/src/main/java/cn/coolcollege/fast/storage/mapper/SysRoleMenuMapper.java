package cn.coolcollege.fast.storage.mapper;

import cn.coolcollege.fast.storage.entity.dao.SysAppBaseRole;
import cn.coolcollege.fast.storage.entity.dao.SysRoleMenu;
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
public interface SysRoleMenuMapper extends Mapper<SysRoleMenu> {
//    /**
//     * 查询菜单使用数量
//     *
//     * @param menuId 菜单ID
//     * @return 结果
//     */
//     int checkMenuExistRole(@Param("menuId") Long menuId);

    Integer insertList(@Param("list") List<SysRoleMenu> list);
}
