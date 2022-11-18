package com.byd.auth.manage.service.aspect;

import com.byd.auth.manage.common.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/18 11:24
 * @description 接口请求日志记录
 */
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestId = request.getHeader(Constants.REQUEST_ID_KEY);
        if (StringUtils.isNotBlank(requestId)) {
            MDC.put(Constants.REQUEST_ID_KEY, requestId);
        }
        StringBuilder sb = new StringBuilder();
        String requestURI = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        sb.append(requestURI);
        String requestStr = null;
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null) {
            sb.append("?");
            parameterMap.forEach((key, value) -> sb.append(key + "=" + value[0] + "&"));
            if (StringUtils.isNotBlank(userAgent)) {
                sb.append("UA=" + userAgent);
            }
            requestStr = sb.toString();
        }
        // 打印请求参数
        log.info("http request input:{}", requestStr);
        return super.preHandle(request, response, handler);
    }
}
