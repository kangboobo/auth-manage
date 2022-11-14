package cn.coolcollege.fast.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author majian
 * The model for output
 */
@ToString
@Data
public class UserResourceDo {
    /**
     * app Id
     */
    private String appId;
    /**
     * 企业id
     */
    private String enterpriseId;

    /**
     * 资源id
     */
    private String resourceId;

    /**
     * 资源类型
     */
    private String resourceType;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源描述
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
     * 资源创建时间戳
     */
    private Long createTs;

    /**
     * 查询资源可见性类型
     * {@link cn.coolcollege.fast.constants.ResourceVisibleTypeEnum}
     */
    private String visibleType;

    /**
     * 查询userId列表
     */
    private List<String> userIds;

    /**
     * 引用资源权限类型
     * {@link cn.coolcollege.fast.constants.ResourceVisibleTypeEnum}
     */
    private String referVisibleType;
    /**
     * 具有引用权限的用户id
     */
    private List<String> referUserIds;

    /**
     * 编辑资源权限类型
     * {@link cn.coolcollege.fast.constants.ResourceVisibleTypeEnum}
     */
    private String editVisibleType;
    /**
     * 具有编辑权限的用户id
     */
    private List<String> editUserIds;

    /**
     * 业务特有字段
     */
    private String dataDict;

    /**
     * 分类Id
     */
    private List<Long> classifyIds;

}
