package cn.coolcollege.fast.storage.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/14 17:29
 * @description
 */
public class SysUserVo {

    @JSONField(name = "user_id")
    private String userId;

    @JSONField(name = "user_name")
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
