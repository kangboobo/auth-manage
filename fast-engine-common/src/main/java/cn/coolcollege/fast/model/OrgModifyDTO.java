package cn.coolcollege.fast.model;

import lombok.Data;

@Data
public class OrgModifyDTO extends BaseDTO{
    String orgId;
    String orgType;
    String fromOrgId;
    String toOrgId;
}
