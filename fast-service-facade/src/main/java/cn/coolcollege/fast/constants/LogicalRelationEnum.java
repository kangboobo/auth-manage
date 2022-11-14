package cn.coolcollege.fast.constants;

/**
 * @Author bai bin
 * @Date 2021/5/18 15:59
 */
public enum LogicalRelationEnum {
    /**
     * 等于
     */
    EQ("eq"),
    /**
     * 不等于
     */
    NEQ("neq"),
    /**
     * 包含
     */
    LIKE("like"),
    /**
     * 不包含
     */
    NOT_LIKE("not_like"),
    /**
     * 大于
     */
    GT("gt"),
    /**
     * 大于等于
     */
    GEQ("geq"),
    /**
     * 小于
     */
    LT("lt"),
    /**
     * 小于等于
     */
    LEQ("leq"),
    /**
     * 位于其中(此设置暂不允许使用，后续废弃)
     */
    IN("in"),
    /**
     * 不位于其中(此设置暂不允许使用，后续废弃)
     */
    NOT_IN("not_in"),
    /**
     * 介于两者中间
     */
    BETWEEN("between"),
    /**
     *  包含
     */
    CONTAINS("contains"),
    /**
     * or里面包含and
     */
    OR_CONTAINS_AND("or_contains_add"),
    /**
     * and里面包含or
     */
    AND_CONTAINS_OR("and_contains_or"),
    /**
     * 其它
     */
    OTHER("other");

    private final String logical;

    LogicalRelationEnum(String logical) {
        this.logical = logical;
    }

    public String getLogical() {
        return logical;
    }

    public static LogicalRelationEnum getByValue(String value) {
        for (LogicalRelationEnum relationEnum : values()) {
            if (relationEnum.getLogical().equals(value)) {
                return relationEnum;
            }
        }
        return OTHER;
    }
}
