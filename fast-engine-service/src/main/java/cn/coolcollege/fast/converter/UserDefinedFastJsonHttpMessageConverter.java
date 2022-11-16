package cn.coolcollege.fast.converter;

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


    public final static Charset UTF8 = Charset.forName("UTF-8");
    private Charset charset = UTF8;
    public static SerializerFeature[] features = new SerializerFeature[]{SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.DisableCircularReferenceDetect};

    public static SerializerFeature[] featuresNew = new SerializerFeature[]{SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullListAsEmpty, SerializerFeature.DisableCircularReferenceDetect};

    public static SerializerFeature[] featuresNew_ = new SerializerFeature[]{SerializerFeature.WriteMapNullValue,
            SerializerFeature.PrettyFormat,// 输出格式化，解决了基本类型的自定义序列化问题，如果不加此配置那么boolean和long的数据不能被序列化成String
            SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullListAsEmpty, SerializerFeature.DisableCircularReferenceDetect};

    public static String[] patterns = new String[]{
            "/v2/*",
    };

    // 默认老API用驼峰格式
    private static final SerializeConfig config = new SerializeConfig();
    // 下划线格式配置
    private static final SerializeConfig snakeCaseConfig = new SerializeConfig();

    static {
        config.put(Long.class, ToStringSerializer.instance);
        config.put(Long.TYPE, ToStringSerializer.instance);

        snakeCaseConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        snakeCaseConfig.put(Long.class, ToStringSerializer.instance);
        snakeCaseConfig.put(Long.TYPE, ToStringSerializer.instance);
        snakeCaseConfig.put(Boolean.class, ToStringSerializer.instance);
        snakeCaseConfig.put(Boolean.TYPE, ToStringSerializer.instance);
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

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String text = "";
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();

            String requestUri = request.getRequestURI();
            if (requestUri.startsWith("/v") && !requestUri.startsWith("/v1")) {
                //这个地方增加config配置
                text = JSON.toJSONString(obj, config, featuresNew);
            } else {
                //这个地方增加config配置
                text = JSON.toJSONString(obj, config, features);
            }
        } else {
            text = JSON.toJSONString(obj, config, featuresNew);
        }

        log.info("UserDefinedFastJsonHttpMessageConverter.writeInternal.text outside:{}", text);
        byte[] bytes = text.getBytes(charset);
        out.write(bytes);
    }
}
