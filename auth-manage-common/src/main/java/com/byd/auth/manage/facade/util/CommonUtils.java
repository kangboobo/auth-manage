package com.byd.auth.manage.facade.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础操作类
 *
 * @author bai bin
 */
public class CommonUtils {
    /**
     * 驼峰转下划线
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String jsonFormatChange(T t) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        return JSON.toJSONString(t, serializeConfig, SerializerFeature.WriteDateUseDateFormat);
    }

    public static List<String> convertToStringList(List<Long> longList) {
        List<String> res = new ArrayList<>();
        for (Long s : longList) {
            res.add(String.valueOf(s));
        }
        return res;
    }

    public static List<Long> convertToLongList(List<String> stringList) {
        List<Long> res = new ArrayList<>();
        for (String s : stringList) {
            res.add(Long.parseLong(s));
        }
        return res;
    }
}
