package com.byd.auth.manage.facade.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class HttpClientConfig {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientConfig.class);


    @Autowired
    private RestTemplateProperties restTemplateProperties;

    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();

        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());


        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create().register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingConnectionManager.setMaxTotal(restTemplateProperties.getMAX_TOTAL_CONNECTIONS());
        poolingConnectionManager.setDefaultMaxPerRoute(restTemplateProperties.getDEFAULT_MAX_PER_ROUTE());
        return poolingConnectionManager;
    }


    @Bean
    public CloseableHttpClient httpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(restTemplateProperties.getREQUEST_TIMEOUT())
                .setConnectTimeout(restTemplateProperties.getCONNECT_TIMEOUT())
                .setSocketTimeout(restTemplateProperties.getSOCKET_TIMEOUT()).build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnectionManager())
                .build();
    }


}
