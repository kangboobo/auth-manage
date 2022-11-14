package cn.coolcollege.fast.task.msg;

import lombok.Data;
import lombok.ToString;

/**
 * 基本的任务消息
 *
 * @author pk
 * @date 2021-06-27 10:11
 */
@Data
@ToString
public class BaseTaskMsg {
    private String msgId;
    private String appId;
    private String eid;
    private String domainId;
    private String resourceType;
}
