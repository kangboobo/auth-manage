package cn.coolcollege.fast.model;

import lombok.Data;

import java.util.List;

@Data
public class UserModifyOrgDTO extends BaseDTO{
    private String orgType;
    private String userId;
    private List<String> addOrgIds;
    private List<String> removeOrgIds;
}
