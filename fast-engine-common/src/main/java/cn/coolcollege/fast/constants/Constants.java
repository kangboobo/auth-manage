package cn.coolcollege.fast.constants;

import com.google.common.collect.Maps;
import org.assertj.core.util.Lists;

import java.util.List;
import java.util.Map;

/**
 * 常量类
 *
 * @author bai bin
 */
public interface Constants {

    /**
     * 记录日志的时候隐式传递 msgId, 方便定位问题(设置的地方和使用的地方需要在同一个线程)
     */
    String MSG_ID = "MSG-ID";
    /**
     * 索引名称
     */
    String CLASS_DOMAIN_INDEX_NAME = "resource-index";

    /**
     * 类型名称
     */
    String TYPE_NAME = "doc";

    /**
     * 异常消息索引名称
     */
    String WRONG_MSG_INDEX_NAME = "msg-index";

    /**
     * 异常消息类型名称
     */
    String WRONG_MSG_TYPE_NAME = "wrong";

    /**
     * 判断消息类型key
     */
    String msgType = "msg_type";

    String eventTs = "event_ts";

    String appId = "app_id";

    String eId = "eid";

    String msgId = "msg_id";

    String domainId = "domain_id";

    String enterpriseId = "enterprise_id";

    String status = "status";

    String id = "id";

    String resourceId = "resource_id";

    String resourceIds = "resource_ids";

    String resourceTitle = "resource_title";

    String desc = "desc";

    String createUserId = "create_user_id";

    String createUserName = "create_user_name";

    String createTs = "create_ts";

    String resourceType = "resource_type";

    String newClassifyIds = "new_classify_ids";

    String resourceStatus = "status";

    String publishStatus = "publish_status";

    String visibleType = "visible_type";

    String userIds = "user_ids";

    String referVisibleType = "refer_visible_type";

    String referUserIds = "refer_user_ids";

    String editVisibleType = "edit_visible_type";

    String editUserIds = "edit_user_ids";

    String classifyIds = "classify_ids";

    String users = "users";

    String orgType = "org_type";

    String userId = "user_id";

    String addOrgIds = "add_org_ids";

    String removeOrgIds = "remove_org_ids";

    String orgId = "org_id";

    String fromParentOrgId = "from_parent_org_id";

    String toParentOrgId = "to_parent_org_id";

    String departmentIds = "department_ids";

    String groupIds = "group_ids";

    String positionIds = "position_ids";

    String studying = "studying";

    String finished = "finished";

    String operateUserId = "operate_user_id";

    String operateUserName = "operate_user_name";

    String filed = "field";

    /**
     * between类型值个数
     */
    int BETWEEN_VALUE_SIZE = 2;

    String authorityRangeModify = "authority_range_modify";

    String supervisorModify = "supervisor_modify";

    /**
     * 获取数据大小
     */
    int DATA_SIZE = 100;

    String COMMA = ",";

    String FILL_POINT = ".";

    String ES_ANALYZER = "analyzer";

    String ES_COMMA_ANALYZER = "comma";

    String ES_SPACE_ANALYZER = "whitespace";

    String ES_DATA_DICT = "data_dict";

    String ES_TYPE = "type";

    String ES_NESTED = "nested";

    String ES_PROPERTIES = "properties";

    String ORDER_ASC = "asc";

    String ES_QUERY_CLASSIFY_ID = "classify_id";

    String ES_QUERY_CREATE_TS = "create_ts";

    String ES_QUERY_STUDY_STATE = ".study_state";

    String ES_QUERY_LEARNING_UIDS = "learning_uids";

    String ES_QUERY_LEARNED_UIDS = "learned_uids";

    String ES_QUERY_UNLEARNED = "unlearned";

    String ES_QUERY_LEARNING = "learning";

    String ES_QUERY_LEARNED = "learned";

    String ES_QUERY_SHARE_STATUS = ".share_status";

    String ES_QUERY_SHARING = "sharing";

    String sortNo = "sort_no";

    String isRecommend = "is_recommend";

    String PUBLISH_STATUS = "publish_status";

    String IS_RECOMMEND = "is_recommend";

    String DATA_DICT = "data_dict";

    String KEYWORD = "keyword";

    int ES_BULK_LOAD_ADD_SIZE = 200;

    String SUPER_USER_ID = "10";

    String NESTED_AGG = "agg";

    String GROUP_FIELD = "group_field";

    /**
     * domainId 和 esIndex一对一映射
     */
    Map<String, String> domainEsIndexMaps = Maps.newHashMap();

    /**
     * resourceType和 domainId的一对一映射
     */
    Map<String, String> resourceTypeDomainMaps = Maps.newHashMap();

    /**
     * 服务和企业路由的外层key
     */
    String GATEWAY_ENV_KEY = "gateway_env";

    String COURSE_JTH = "course_jth";

    /**
     * 字段分组统计，桶最大个数
     */
    int GROUP_BUCKET_COUNT = 1000;

    /**
     * es不分页时默认查询全部资源信息
     */
    int MAX_RESOURCE_SIZE = 1000000;

    /**
     * 负1
     */
    Integer MINUS_ONE_VALUE  = -1;

    /**
     * 集团化课程按钮关闭
     */
    int JTH_COURSE_BUTTON_CLOSE = 0;
    /**
     * 集团化课程按钮部分开启
     */
    int JTH_COURSE_BUTTON_PART_ON = 1;

    /**
     * 集团化课程按钮全部开启
     */
    int JTH_COURSE_BUTTON_ALL_ON = 2;

    /**
     * 非课程领域资源类型
     */
    List<String> classDomainResourceTypeLists =
        Lists.newArrayList("course", "micro_course", "image_text_course", "study_project", "live");


    String EMPTY_STR = "";

    String SUCCESS_STR = "success";

    int SUCCESS_CODE = 0;

    int INTEGER_ONE_VALUE = 1;

    byte BYTE_ZERO_VALUE = 0;

    byte BYTE_ONE_VALUE = 1;

    int ZERO_VALUE = 0;

    int MINUS_ONE = -1;

    /** 校验返回结果码 */
    String UNIQUE = "0";
    String NOT_UNIQUE = "1";
}
