package cn.coolcollege.fast.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig implements RestClientBuilderCustomizer {
    /**
     * ES请求地址
     */
    @Value("${elasticsearch.host}")
    private String host;

    /**
     * 端口
     */
    @Value("${elasticsearch.port}")
    private int port;

    /**
     * 用户名
     */
    @Value("${elasticsearch.username}")
    private String username;

    /**
     * 密码
     */
    @Value("${elasticsearch.password}")
    private String password;

    /**
     * 超时时间设为5分钟
     */
    private static final int TIME_OUT = 5 * 60 * 1000;

    @Bean(name = "restHighLevelClientPre")
    public RestHighLevelClient getRestHighLevelClientPre() {
        // 阿里云Elasticsearch集群需要basic auth验证。
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //访问用户名和密码为您创建阿里云Elasticsearch实例时设置的用户名和密码，也是Kibana控制台的登录用户名和密码。
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        // 通过builder创建rest client，配置http client的HttpClientConfigCallback。
        // 单击所创建的Elasticsearch实例ID，在基本信息页面获取公网地址，即为ES集群地址。
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        // RestHighLevelClient实例通过REST low-level client builder进行构造。
        return new RestHighLevelClient(builder);
    }

    @Override
    public void customize(RestClientBuilder builder) {
        //第一个异常解决方案
        builder.setMaxRetryTimeoutMillis(TIME_OUT);
        //第二个异常解决方案
        RestClientBuilder.RequestConfigCallback configCallback = requestConfigBuilder -> requestConfigBuilder
                .setConnectTimeout(TIME_OUT)
                //更改客户端的超时限制默认30秒现在改为5分钟
                //默认最大连接数为30，和并发执行书时修改DEFAULT_MAX_CONN_TOTAL和DEFAULT_MAX_CONN_PER_ROUTE参数
                .setSocketTimeout(TIME_OUT);
        builder.setRequestConfigCallback(configCallback);
    }

}
