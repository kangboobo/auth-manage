package cn.coolcollege.fast.storage.entity.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户对象 sys_user
 * 
 * @author ruoyi
 */
@Table(name = "sys_user")
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 部门ID */
    @Column(name = "dept_id")
    private Long deptId;

    /** 用户账号 */
    @Column(name = "user_name")
    private String userName;

    /** 用户昵称 */
    @Column(name = "nick_name")
    private String nickName;

    /** 用户邮箱 */
    private String email;

    /** 手机号码 */
    @Column(name = "phone_number")
    private String phoneNumber;

    /** 用户性别 */
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;

    /** 帐号状态（0正常 1停用） */
    private String status;

    /** 最后登录IP */
    @Column(name = "login_ip")
    private String loginIp;

    /** 最后登录时间 */
    @Column(name = "login_date")
    private Date loginDate;

    private SysDept dept;

    /** 角色对象 */
    private List<SysRole> roles;

    /** 角色组 */
    @Column(name = "role_ids")
    private Long[] roleIds;

    /** 岗位组 */
    @Column(name = "post_ids")
    private Long[] postIds;

    /** 角色ID */
    @Column(name = "role_id")
    private Long roleId;

    public SysUser() {

    }

    public SysUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long id) {
        return id != null && -1L == id;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
            .append("deptId", getDeptId()).append("userName", getUserName()).append("nickName", getNickName())
            .append("email", getEmail()).append("phonenumber", getPhoneNumber()).append("sex", getSex())
            .append("avatar", getAvatar()).append("password", getPassword()).append("status", getStatus())
            .append("loginIp", getLoginIp()).append("loginDate", getLoginDate()).append("createUser", getCreateUser())
            .append("createTime", getCreateTime()).append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime()).append("remark", getRemark()).append("dept", getDept()).toString();
    }
}
