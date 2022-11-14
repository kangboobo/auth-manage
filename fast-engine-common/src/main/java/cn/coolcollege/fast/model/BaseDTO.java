package cn.coolcollege.fast.model;

import lombok.Data;

@Data
public class BaseDTO {
    private String msgId;
    private String appId;
    private String eId;
    private String messageType;
    private Long eventTs;
    private String operateUserId;
    private String operateUserName;
}
