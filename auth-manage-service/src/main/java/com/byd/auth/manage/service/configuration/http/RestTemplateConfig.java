package com.byd.auth.manage.service.configuration.http;

import com.byd.auth.manage.service.aspect.UserDefinedFastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private UserDefinedFastJsonHttpMessageConverter messageConverter;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        restTemplate.setMessageConverters(Lists.newArrayList(messageConverter));

        return restTemplate;
    }


    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }

}
