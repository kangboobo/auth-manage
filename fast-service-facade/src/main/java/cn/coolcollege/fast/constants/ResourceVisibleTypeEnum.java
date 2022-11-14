package cn.coolcollege.fast.constants;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资源可见性类型枚举
 *
 * @author pk
 */
public enum ResourceVisibleTypeEnum {
    /**
     * 全员可见
     */
    ALL("all"),
    /**
     * 部分可见(包括了业务的部分可见和仅自己可见)
     */
    PART("part"),
    /**
     * 跟随资源分类权限
     */
    FLOW_CLASSIFY("flow_classify"),

    /**
     * 管辖范围及下属
     */
    AUTH_WITH_SUB("auth_with_sub"),

    /**
     * 仅自己可见
     */
    ONLY_ME("only_me");

    private String value;

    private static final Map<String, ResourceVisibleTypeEnum> MAP =
        Arrays.stream(values()).collect(Collectors.toMap(ResourceVisibleTypeEnum::getValue, Function.identity()));

    ResourceVisibleTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static final ResourceVisibleTypeEnum parseValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return MAP.get(value);
    }
}
