package cn.coolcollege.fast.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Author bai bin
 * @Date 2021/5/12 17:30
 */
@Data
public class EsBaseResourceDo extends EsBaseDto {

    private String resourceId;

    /**
     * userId列表，使用逗号分隔
     */
    private String userIds;

    /**
     * 分类id列表，使用逗号分隔
     */
    private String classifyIds;

    /**
     * 资源名称
     */
    private String resourceTitle;

    /**
     * 资源描述信息
     */
    private String desc;

    /**
     * 创建人用户id
     */
    private String createUserId;

    /**
     * 创建人名字
     */
    private String createUserName;

    /**
     * 资源可见性类型
     * {@link cn.coolcollege.fast.constants.ResourceVisibleTypeEnum}
     */
    private String visibleType;

    /**
     * 引用用户id
     */
    private String referUserIds;

    /**
     * 资源引用类型
     */
    private String referVisibleType;

    /**
     * 编辑用户id
     */
    private String editUserIds;

    /**
     * 编辑用户类型
     */
    private String editVisibleType;


    /**
     * 资源创建时间
     */
    private Long createTs;

    /**
     * 业务特有字段
     */
    private JSONObject dataDict;

}
