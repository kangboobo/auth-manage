package com.byd.auth.manage.service.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by gavin on 16/3/10.
 */
@Slf4j
@Component
public class UserDefinedFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {

    /**
     * 字符集
     */
    private Charset charset = Charset.forName("UTF-8");

    /**
     * JSON序列号规则
     */
    public static SerializerFeature[] features = new SerializerFeature[]{SerializerFeature.WriteMapNullValue,
            SerializerFeature.PrettyFormat,// 输出格式化，解决了基本类型的自定义序列化问题，如果不加此配置那么boolean和long的数据不能被序列化成String
            SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullListAsEmpty, SerializerFeature.DisableCircularReferenceDetect};

    private static final SerializeConfig config = new SerializeConfig();
    static {
        config.put(Long.class, ToStringSerializer.instance);
        config.put(Long.TYPE, ToStringSerializer.instance);
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream out = outputMessage.getBody();
        // 接口响应json序列化
        String text = JSON.toJSONString(obj, config, features);
        byte[] bytes = text.getBytes(charset);
        log.info("http response output:{}", text);
        out.write(bytes);
    }
}
