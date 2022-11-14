package cn.coolcollege.fast.storage.entity.dao;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/14 14:29
 * @description 用户组实体
 */
@Table(name = "sys_use_group")
public class SysUserGroup extends BaseEntity{

    /** 角色ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用id
     */
    @Column(name = "app_id")
    private Long appId;

    /**
     * 基地id
     */
    @Column(name = "base_id")
    private String baseId;

    /**
     * 用户组名称
     */
    @Column(name = "user_group_name")
    private String userGroupName;
}
