package com.byd.auth.manage.service.configuration;

import com.byd.auth.manage.common.model.BaseResponse;
import com.byd.auth.manage.common.exception.AuthManageErrConstant;
import com.byd.auth.manage.common.exception.ServiceException;
import com.byd.auth.manage.common.model.ParamValidationResult;
import com.byd.auth.manage.common.util.ResponseUtil;
import com.byd.auth.manage.service.aspect.LogInterceptor;
import com.byd.auth.manage.service.aspect.UserDefinedFastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshua on 2017/6/22.
 */
@Configuration
public class ServletContextConfig extends WebMvcConfigurationSupport {

    private final Logger logger = LoggerFactory.getLogger(ServletContextConfig.class);

    @Autowired
    private LogInterceptor logInterceptor;

    /**
     * 配置servlet处理
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 接口白名单
        String[] excludePathPatterns = {
                "/example/**",
        };
        // 接口请求日志记录
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        UserDefinedFastJsonHttpMessageConverter fastJsonHttpMessageConverter = new UserDefinedFastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(fastJsonHttpMessageConverter);
    }

    /**
     * 统一异常处理
     *
     * @param exceptionResolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add((request, response, handler, e) -> {
            boolean isError = false;
            BaseResponse result;
            String uri = request.getRequestURI();
            if (e instanceof ServiceException) {
                // 业务异常
                ServiceException serviceException = (ServiceException) e;
                if (serviceException.getErrorContext() != null) {
                    result = BaseResponse.getFailedResponse(serviceException.getErrorContext());
                } else {
                    result = BaseResponse.getFailedResponse(AuthManageErrConstant.SERVICE_IS_BUSY);
                }
            } else if (e instanceof MethodArgumentNotValidException) {
                // 参数异常
                isError = true;
                List<ParamValidationResult> paramValidationResults = Lists.newArrayList();
                for (FieldError error : ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors()) {
                    ParamValidationResult paramValidationResult = new ParamValidationResult();
                    paramValidationResult.setMessage(error.getDefaultMessage());
                    paramValidationResult.setParam(error.getField());
                    paramValidationResults.add(paramValidationResult);
                }
                result = BaseResponse.getFailedResponse(
                        AuthManageErrConstant.METHOD_ARGUMENT_NOT_VALID, paramValidationResults);
            } else {
                // 其他未知异常
                isError = true;
                result = BaseResponse.getFailedResponse(AuthManageErrConstant.SERVICE_IS_BUSY);
            }

            if (isError) {
                logger.error("api error, uri:{}, msg:{}", uri, result.getMsg(), e);
            } else {
                logger.warn("api warn, uri:{}, msg:{}", uri, result.getMsg());
            }
            ResponseUtil.responseResult(response, result);
            return new ModelAndView();
        });
    }


}
