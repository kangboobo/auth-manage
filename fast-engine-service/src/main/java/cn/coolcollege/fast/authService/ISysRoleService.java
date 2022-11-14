package cn.coolcollege.fast.authService;

import cn.coolcollege.fast.storage.entity.vo.SysRoleVo;
/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/11 10:38
 * @description
 */
public interface ISysRoleService {

    /**
     * 根据角色id查询单个角色信息
     * @param roleId 角色id
     * @return
     */
    Object getSysRoleById(Long roleId);


    Object getSysRoleList(String roleName);

    /**
     * 增加｜修改角色
     * @param sysRoleVo 角色vo
     * @return
     */
    Object insertOrUpdateRole(SysRoleVo sysRoleVo);

    /**
     * 根据角色id删除角色
     * @param roleId 角色id
     * @return
     */
    Object deleteSysRoleById(Long roleId);

}
