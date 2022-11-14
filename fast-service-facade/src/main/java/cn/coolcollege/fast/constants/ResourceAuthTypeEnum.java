package cn.coolcollege.fast.constants;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资源权限类型枚举
 *
 * @author pk
 * @date 2021-06-24 17:24
 */
public enum ResourceAuthTypeEnum {
    QUERY("query"), REFER("refer"), EDIT("edit");

    private static final Map<String, ResourceAuthTypeEnum> map = Arrays.stream(values()).collect(Collectors.toMap(ResourceAuthTypeEnum::getValue, Function.identity()));

    private String value;

    ResourceAuthTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static final ResourceAuthTypeEnum parseValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return QUERY;
        }
        return map.get(value);
    }
}