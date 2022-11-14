package cn.coolcollege.fast.constants;

/**
 * 组织类型枚举
 *
 * @author baibin
 * @date 2021/7/6 16:34
 */
public enum FastEngineOrgTypeEnum {

    /**
     * 部门
     */
    DEPARTMENT("department"),
    /**
     * 岗位
     */
    POSITION("position"),
    /**
     * 群组
     */
    GROUP("group");

    private final String orgType;

    FastEngineOrgTypeEnum(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgType() {
        return orgType;
    }

    public static FastEngineOrgTypeEnum getByValue(String value) {
        for (FastEngineOrgTypeEnum relationEnum : values()) {
            if (relationEnum.getOrgType().equals(value)) {
                return relationEnum;
            }
        }
        return DEPARTMENT;
    }
}
