package cn.coolcollege.fast.http;

import cn.coolcollege.fast.converter.UserDefinedFastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;

@Configuration
public class AsyncRestTemplateConfig {

    @Autowired
    private CloseableHttpAsyncClient httpAsyncClient;

    @Autowired
    private UserDefinedFastJsonHttpMessageConverter messageConverter;


    @Bean
    public AsyncRestTemplate asyncRestTemplate() {

        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        asyncRestTemplate.setAsyncRequestFactory(asyncClientHttpRequestFactory());
        asyncRestTemplate.setMessageConverters(Lists.newArrayList(messageConverter));

        return asyncRestTemplate;
    }

    @Bean
    public HttpComponentsAsyncClientHttpRequestFactory asyncClientHttpRequestFactory() {
        HttpComponentsAsyncClientHttpRequestFactory asyncClientHttpRequestFactory = new HttpComponentsAsyncClientHttpRequestFactory();
        asyncClientHttpRequestFactory.setHttpAsyncClient(httpAsyncClient);
        return asyncClientHttpRequestFactory;
    }
}
