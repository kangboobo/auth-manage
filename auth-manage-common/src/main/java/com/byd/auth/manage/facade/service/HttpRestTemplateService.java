package com.byd.auth.manage.facade.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class HttpRestTemplateService {

    @Autowired
    private RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取请求实体对象
     *
     * @param object
     * @param access_token
     * @return
     */
    public HttpEntity getHttpEntity(Object object, String access_token) {
        return new HttpEntity(object, this.getHeaders(access_token));
    }


    /**
     * restTemplate统一请求
     *
     * @param uri            请求路径
     * @param httpMethod     方法类型
     * @param httpEntity     请求实体
     * @param responseEntity 返回对象
     * @param params         uri参数列表
     * @param <T>
     * @return 返回实体
     */
    public <T> ResponseEntity<T> exchange(String uri, HttpMethod httpMethod, HttpEntity httpEntity, Class<T> responseEntity, Map<String, ?> params) {
        List<String> queryStrings = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach((k, v) -> {
                queryStrings.add(k + "=" + v);
            });
        }
        String queryString = null;
        if (!CollectionUtils.isEmpty(queryStrings)) {
            queryString = String.join("&", queryStrings);
        }
        if (StringUtils.isNotBlank(queryString)) {
            uri = uri + "?" + queryString;
        }
        ResponseEntity<T> exchange = null;
        logger.info("start exchange=uri:{},httpMethod:{},httpEntity:{},responseEntity:{},params:{}",
                uri, httpMethod.toString(), httpEntity.toString(), responseEntity.toString(), Objects.nonNull(params) ? params.toString() : null);
        try {
            exchange = restTemplate.exchange(uri, httpMethod, httpEntity, responseEntity);
        } catch (HttpStatusCodeException e) {
            logger.error("exchange err, resp={}", e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("exchange err:", e);
        }
        logger.info("end exchange=exchange:{},", exchange);
        return exchange;
    }

    /**
     * GET:HTTP请求封装
     *
     * @param url
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     */
    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        logger.info("getForObject start:url={},responseType={},uriVariables={}",
                url, responseType.getName(), JSONObject.toJSONString(uriVariables));
        T result = null;
        try {
            result = restTemplate.getForObject(transUrl(url, uriVariables), responseType, uriVariables);
        } catch (RestClientException e) {
            logger.error("getForObject error:{}", e);
            throw e;
        }
        logger.info("getForObject end:result={}", JSONObject.toJSONString(result));
        return result;
    }

    private String transUrl(String url, Map<String, ?> uriVariables) {
        StringBuilder finalUrl = new StringBuilder(url);
        if (Objects.isNull(uriVariables)) {
            return finalUrl.toString();
        }
        if (StringUtils.isNotBlank(url)) {
            if (url.contains("?")) {
                if (url.endsWith("?")) {
                    finalUrl.append("1=1");
                }
            } else {
                finalUrl.append("?1=1");
            }
            uriVariables.forEach((k, v) -> {
                finalUrl.append("&").append(k).append("={").append(k).append("}");
            });

        }
        return finalUrl.toString();
    }

    /**
     * POST:HTTP请求封装
     *
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> T postForObject(String url, Object request, Class<T> responseType) {
        logger.info("postForObject start:url={},request={},responseType={}", url, JSONObject.toJSONString(request), responseType.getName());
        T result = null;
        try {
            result = restTemplate.postForObject(url, getHttpEntity(request, null), responseType);
        } catch (RestClientException e) {
            logger.error("postForObject error:{}", e);
            throw e;
        }
        logger.info("postForObject end:result={}", JSONObject.toJSONString(result));
        return result;
    }

    /**
     * Put:HTTP请求封装
     *
     * @param url
     * @param request
     * @param responseType
     */
    public void putForObject(String url, Object request, Class responseType) {
        logger.info("putForObject start:url={},request={},responseType={}",url, JSONObject.toJSONString(request), responseType.getName());
        try {
            restTemplate.put(url, getHttpEntity(request, null), responseType);
        } catch (RestClientException e) {
            logger.error("putForObject error:{}", e);
            throw e;
        }
    }

    /**
     * Delete:HTTP请求封装
     *
     * @param url
     * @param request
     * @param responseType
     */
    public void deleteForObject(String url, Object request, Class responseType) {
        logger.info("deleteForObject start:url={},request={},responseType={}",
                url, JSONObject.toJSONString(request), responseType.getName());
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, getHttpEntity(request, null), responseType);
        } catch (RestClientException e) {
            logger.error("deleteForObject error:{}", e);
            throw e;
        }
    }

    /**
     * 获取Headers
     *
     * @param access_token
     * @return
     */
    private MultiValueMap<String, String> getHeaders(String access_token) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.add("X-Access-Token", access_token);
        headers.add("Content-Type", "application/json");
        return headers;
    }
}
