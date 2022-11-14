package cn.coolcollege.fast.entity;

/**
 * ES查询基础实体
 *
 * @Author bai bin
 * @Date 2021/5/17 13:36
 */
public class EsBaseQueryParam {

    /**
     * 需要查询的字段key
     */
    private String key;

    /**
     * 逻辑关系
     */
    private String logicalRelation;

    /**
     * 需要查询的字段value
     */
    private Object value;

    public EsBaseQueryParam() {
    }

    public EsBaseQueryParam(String key, String logicalRelation, Object value) {
        this.key = key;
        this.logicalRelation = logicalRelation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLogicalRelation() {
        return logicalRelation;
    }

    public void setLogicalRelation(String logicalRelation) {
        this.logicalRelation = logicalRelation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EsBaseQueryParam{" +
                "key='" + key + '\'' +
                ", logicalRelation='" + logicalRelation + '\'' +
                ", value=" + value +
                '}';
    }
}
