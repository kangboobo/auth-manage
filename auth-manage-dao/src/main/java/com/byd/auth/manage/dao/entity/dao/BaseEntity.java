package com.byd.auth.manage.dao.entity.dao;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/10 09:38
 * @description 公共实体类
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 备注
     */
    private String remark;

    /**
     * 逻辑删除 0:未删除 1:删除 数据库中设置默认为0
     */
    @JSONField(name = "delete_flag")
    @Column(name = "delete_flag")
    private Byte deleteFlag;

    /**
     * 创建人
     */
    @JSONField(name = "create_user")
    @Column(name = "create_user")
    private String createUser;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 修改人
     */
    @JSONField(name = "update_user")
    @Column(name = "update_user")
    private String updateUser;

    /**
     * 修改时间
     */
    @JSONField(name = "update_time")
    @Column(name = "update_time")
    private Long updateTime;


    /** 请求参数 */
    private Map<String, Object> params;


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }
}
