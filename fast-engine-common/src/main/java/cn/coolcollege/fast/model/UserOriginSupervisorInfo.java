package cn.coolcollege.fast.model;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.ToString;

/**
 * @author pk
 * @date 2021-07-19 11:49 上午
 */
@Data
@ToString
public class UserOriginSupervisorInfo {
    @JSONField(name = "user_id")
    private String userId;

    /**
     * 原本的直属上级领导
     */
    @JSONField(name = "origin_direct_supervisor_id")
    private String originDirectSupervisorId;
}
