package com.byd.auth.manage.common.util;

import com.alibaba.fastjson.JSON;
import com.byd.auth.manage.common.model.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);

    private static void addCors(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
    }

    /**
     * send success response
     *
     * @param response
     * @param result
     */
    public static void responseResult(HttpServletResponse response, BaseResponse result) {
        int status = HttpStatus.OK.value();
        addCors(response);
        response.setStatus(status);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            logger.error("responseResult error", e);
        }
    }
}
