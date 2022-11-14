package cn.coolcollege.fast.entity.request;

import cn.coolcollege.fast.constants.ResourceAuthTypeEnum;
import cn.coolcollege.fast.entity.EsBaseQueryParam;
import cn.coolcollege.fast.entity.EsSortOrderParam;
import lombok.Data;
import net.coolcollege.platform.util.model.BasePageRequest;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/7 20:06
 */
@Data
public class EsOutputRequest extends BasePageRequest {

    /**
     * 领域名称，不用领域调用的es index不同
     */
    private String domainId;

    private List<String> resourceTypes;

    private String userId;

    /**
     * 资源权限类型 {@link cn.coolcollege.fast.constants.ResourceAuthTypeEnum}
     */
    private String resourceAuthType = ResourceAuthTypeEnum.QUERY.getValue();

    /**
     * 是否查询全部资源(一般情况, 管辖范围为全部的可以查询全部资源)
     */
    private Boolean queryAll = false;

    /**
     * 只查询自己创建的资源，默认为false,true表示只查询自己创建的资源
     */
    private Boolean onlyMe = false;

    /**
     * resourceId在这里面
     */
    private List<String> inResourceIds;

    /**
     * resourceId不在这里
     */
    private List<String> excludeResourceIds;

    /**
     * 与关系查询参数
     */
    private List<EsBaseQueryParam> andQueryParam;

    /**
     * 或关系查询参数
     */
    private List<EsBaseQueryParam> orQueryParam;

    /**
     * 排序参数
     */
    private List<EsSortOrderParam> sortOrderParam;

    /**
     * 分组字段
     * 如果按照是领域特有字段分类，需增加.
     */
    private String groupFiled;

}
