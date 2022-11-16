package com.byd.auth.manage.dao.entity.dao;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/10 09:06
 * @description 角色实体
 */
@Table(name = "sys_role")
public class SysRole extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用Id
     */
    @Column(name = "app_id")
    private Long appId;

    /**
     * 基地id集合
     */
    @Column(name = "base_id_str")
    private String baseIdStr;

    /** 角色名称 */
    @Column(name = "role_name")
    @JSONField(name = "role_name")
    private String roleName;

    /** 角色权限 */
    @Column(name = "role_key")
    @JSONField(name = "role_key")
    private String roleKey;

    /** 角色排序 */
    @Column(name = "role_sort")
    @JSONField(name = "role_sort")
    private Integer roleSort;

    /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限） */
    @Column(name = "data_scope")
    @JSONField(name = "data_scope")
    private String dataScope;

    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    @Column(name = "menu_check_strictly")
    @JSONField(name = "menu_check_strictly")
    private Boolean menuCheckStrictly;

    /** 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ） */
    @Column(name = "dept_check_strictly")
    @JSONField(name = "dept_check_strictly")
    private Boolean deptCheckStrictly;

    /** 角色状态（0正常 1停用） */
    private String status;

    /** 用户是否存在此角色标识 默认不存在 */
    @Transient
    private boolean flag = false;

    /** 角色菜单权限 */
    @Transient
    private Set<String> permissions;

    public SysRole()
    {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getBaseIdStr() {
        return baseIdStr;
    }

    public void setBaseIdStr(String baseIdStr) {
        this.baseIdStr = baseIdStr;
    }

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey()
    {
        return roleKey;
    }

    public void setRoleKey(String roleKey)
    {
        this.roleKey = roleKey;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getRoleSort()
    {
        return roleSort;
    }

    public void setRoleSort(Integer roleSort)
    {
        this.roleSort = roleSort;
    }

    public String getDataScope()
    {
        return dataScope;
    }

    public void setDataScope(String dataScope)
    {
        this.dataScope = dataScope;
    }

    public Boolean getMenuCheckStrictly() {
        return menuCheckStrictly;
    }

    public void setMenuCheckStrictly(Boolean menuCheckStrictly) {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public Boolean getDeptCheckStrictly() {
        return deptCheckStrictly;
    }

    public void setDeptCheckStrictly(Boolean deptCheckStrictly) {
        this.deptCheckStrictly = deptCheckStrictly;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }


    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }


    public Set<String> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Set<String> permissions)
    {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("appId", getAppId())
                .append("baseIdStr", getBaseIdStr())
                .append("roleName", getRoleName())
                .append("roleKey", getRoleKey())
                .append("roleSort", getRoleSort())
                .append("dataScope", getDataScope())
                .append("menuCheckStrictly", getMenuCheckStrictly())
                .append("deptCheckStrictly", getDeptCheckStrictly())
                .append("status", getStatus())
                .append("deleteFlag", getDeleteFlag())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getCreateUser())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }

}
