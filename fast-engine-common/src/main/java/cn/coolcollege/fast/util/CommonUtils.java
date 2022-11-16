package cn.coolcollege.fast.util;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.dto.EsBaseResourceDo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 对象类型转换，并且将userIds从List<String>转换成以逗号拼接的String 将classificationIds转换成以空格拼接的String
     *
     * @param userResourceDo
     * @return
     */
    public static EsBaseResourceDo listConversionString(UserResourceDo userResourceDo) {
        EsBaseResourceDo baseResourceDo = new EsBaseResourceDo();
        baseResourceDo.setAppId(userResourceDo.getAppId());
        baseResourceDo.setEnterpriseId(userResourceDo.getEnterpriseId());
        baseResourceDo.setResourceId(userResourceDo.getResourceId());
        baseResourceDo.setResourceType(userResourceDo.getResourceType());
        baseResourceDo.setResourceTitle(userResourceDo.getName());
        baseResourceDo.setDesc(userResourceDo.getDesc());
        baseResourceDo.setVisibleType(userResourceDo.getVisibleType());
        baseResourceDo.setCreateUserId(userResourceDo.getCreateUserId());
        baseResourceDo.setCreateUserName(userResourceDo.getCreateUserName());
        baseResourceDo.setCreateTs(userResourceDo.getCreateTs());
        if (CollectionUtils.isNotEmpty(userResourceDo.getUserIds())) {
            // 过滤userIdList,去重
            baseResourceDo.setUserIds(StringUtils
                .join(userResourceDo.getUserIds().stream().distinct().collect(Collectors.toList()), Constants.COMMA));
        }
        if (CollectionUtils.isNotEmpty(userResourceDo.getClassifyIds())) {
            baseResourceDo.setClassifyIds(StringUtils.join(userResourceDo.getClassifyIds(), Constants.COMMA));
        }
        //引用用户id
        if (CollectionUtils.isNotEmpty(userResourceDo.getReferUserIds())) {
            baseResourceDo.setReferUserIds(StringUtils.join(userResourceDo.getReferUserIds(), Constants.COMMA));
        }
        //引用类型
        baseResourceDo.setReferVisibleType(userResourceDo.getReferVisibleType());
        //编辑用户id
        if(CollectionUtils.isNotEmpty(userResourceDo.getEditUserIds())){
            baseResourceDo.setEditUserIds(StringUtils.join(userResourceDo.getEditUserIds(),Constants.COMMA));
        }
        //编辑类型
        baseResourceDo.setEditVisibleType(userResourceDo.getEditVisibleType());
        //业务特有字段
        baseResourceDo.setDataDict(JSON.parseObject(userResourceDo.getDataDict()));
        return baseResourceDo;
    }

    public static UserResourceDo stringConversionList(EsBaseResourceDo esBaseResourceDo) {
        UserResourceDo userResourceDo = new UserResourceDo();
        userResourceDo.setAppId(esBaseResourceDo.getAppId());
        userResourceDo.setEnterpriseId(esBaseResourceDo.getEnterpriseId());
        userResourceDo.setResourceId(esBaseResourceDo.getResourceId());
        userResourceDo.setResourceType(esBaseResourceDo.getResourceType());
        userResourceDo.setName(esBaseResourceDo.getResourceTitle());
        userResourceDo.setDesc(esBaseResourceDo.getDesc());
        userResourceDo.setVisibleType(esBaseResourceDo.getVisibleType());
        userResourceDo.setCreateUserId(esBaseResourceDo.getCreateUserId());
        userResourceDo.setCreateUserName(esBaseResourceDo.getCreateUserName());
        userResourceDo.setCreateTs(esBaseResourceDo.getCreateTs());
        if (StringUtils.isNotBlank(esBaseResourceDo.getUserIds())) {
            userResourceDo.setUserIds(Arrays.asList(esBaseResourceDo.getUserIds().split(Constants.COMMA)));
        }
        if (StringUtils.isNotBlank(esBaseResourceDo.getClassifyIds())) {
            List<String> classifyIdList = Arrays.asList(esBaseResourceDo.getClassifyIds().split(Constants.COMMA));
            userResourceDo.setClassifyIds(classifyIdList.stream().map(Long::parseLong).collect(Collectors.toList()));
        }
        if(StringUtils.isNotBlank(esBaseResourceDo.getReferUserIds())){
            userResourceDo.setReferUserIds(Arrays.asList(esBaseResourceDo.getReferUserIds().split(Constants.COMMA)));
        }
        userResourceDo.setReferVisibleType(esBaseResourceDo.getReferVisibleType());
        if(StringUtils.isNotBlank(esBaseResourceDo.getEditUserIds())){
            userResourceDo.setEditUserIds(Arrays.asList(esBaseResourceDo.getEditUserIds().split(Constants.COMMA)));
        }
        userResourceDo.setEditVisibleType(esBaseResourceDo.getEditVisibleType());
        userResourceDo.setDataDict(esBaseResourceDo.getDataDict().toJSONString());
        return userResourceDo;
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

    /**
     * 拼接data_dict字段
     *
     * @param fieldName
     * @return
     */
    public static String splicingEsDataDictField(String fieldName) {
        return Constants.ES_DATA_DICT + Constants.FILL_POINT + fieldName;
    }

}
