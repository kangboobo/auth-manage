package com.byd.auth.manage.service.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AsyncHttpClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpClientConfig.class);

    @Autowired
    private AsyncRestTemplateProperties asyncRestTemplateProperties;

    @Bean
    public PoolingNHttpClientConnectionManager poolingNHttpClientConnectionManager() throws IOReactorException, KeyManagementException, NoSuchAlgorithmException {

        SSLContextBuilder sslBuilder = new SSLContextBuilder();

//        SSLContextBuilder sslBuilder = SSLContexts.custom().setSecureRandom(new SecureRandom());
        SSLContext sslContext = sslBuilder.build();

        DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();

        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", new SSLIOSessionStrategy(sslContext))
                .build();


        PoolingNHttpClientConnectionManager poolingNHttpClientConnectionManager = new PoolingNHttpClientConnectionManager(ioReactor, sessionStrategyRegistry);

        poolingNHttpClientConnectionManager.setMaxTotal(asyncRestTemplateProperties.getMAX_TOTAL_CONNECTIONS());
        poolingNHttpClientConnectionManager.setDefaultMaxPerRoute(asyncRestTemplateProperties.getDEFAULT_MAX_PER_ROUTE());
        return poolingNHttpClientConnectionManager;
    }


    @Bean
    public CloseableHttpAsyncClient asyncHttpClient() throws IOReactorException, NoSuchAlgorithmException, KeyManagementException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(asyncRestTemplateProperties.getREQUEST_TIMEOUT())
                .setConnectTimeout(asyncRestTemplateProperties.getCONNECT_TIMEOUT())
                .setSocketTimeout(asyncRestTemplateProperties.getSOCKET_TIMEOUT()).build();
        return HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingNHttpClientConnectionManager())
                .build();
    }


}
