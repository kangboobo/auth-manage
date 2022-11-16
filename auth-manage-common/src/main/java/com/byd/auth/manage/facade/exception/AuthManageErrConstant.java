package com.byd.auth.manage.facade.exception;
/**
 * @author baibin
 * @version 1.0
 * @date 2022/4/22 16:25
 * @description 自动分派异常常量类
 */
public class AuthManageErrConstant {

//    public static final ErrorContext DISPATCH_SERVER_ERROR = new ErrorContext("auto_dispatch.44441", "auto dispatch server error");

    public static final ErrorContext PARAMS_INVALID = new ErrorContext( 40001, "params invalid");

    public static final ErrorContext DELETE_NOT_ALLOWED = new ErrorContext( 40004, "delete not allowed");

    public static final ErrorContext OPERATE_DB_ERR = new ErrorContext( 40005, "operate db error");

    public static final ErrorContext RESPONSE_IS_NULL = new ErrorContext( 40006, "response is null");

    public static final ErrorContext NAME_DUPLICATE = new ErrorContext( 40007, "name duplicate");

    public static final ErrorContext CALL_USER_API_FAILED = new ErrorContext( 40008, "调用用户中心接口失败");

//    public static final ErrorContext USER_ID_PARAM_ERROR = new ErrorContext("auto_dispatch.44452", "create_id or update_id error");
//
//    public static final ErrorContext USER_CENTER_AUTH_DEPT_ERROR =
//        new ErrorContext("auto_dispatch.44471", "user center auth dept error");
//
//    public static final ErrorContext USER_CENTER_AUTH_POST_ERROR =
//        new ErrorContext("auto_dispatch.44471", "user center auth post error");
//
//    public static final ErrorContext QUERY_DB_ERROR = new ErrorContext("auto_dispatch.44491", "query db error");
//
//    public static final ErrorContext QUERY_DB_PARAM_ERROR = new ErrorContext("auto_dispatch.44492", "query db param error");
//
//    public static final ErrorContext DB_PERSIST_ERROR = new ErrorContext("auto_dispatch.44493", "db persist error");
}
