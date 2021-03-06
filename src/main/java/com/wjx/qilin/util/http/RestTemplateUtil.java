package com.wjx.qilin.util.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @author wangjinxin
 * @date 2021/2/18 11:04
 * @mail wjxScott@icloud.com
 * @description resttemplate 封装工具类
 */
@Slf4j
public class RestTemplateUtil {

    private static final Integer MAX_CONNECT_TIME = 5000;

    private static final Integer MAX_READ_TIME = 3000;

    private RestTemplateUtil() {
    }

    private static RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(MAX_CONNECT_TIME);
        requestFactory.setReadTimeout(MAX_READ_TIME);
        return new RestTemplate(requestFactory);
    }


    private static RestTemplate getRestTemplate(Integer maxConnectTime, Integer maxReadTime) {
        log.info("自定义resttemplate的参数是：maxConnectTime:{},maxReadTime:{}", maxConnectTime, maxReadTime);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(maxConnectTime);
        requestFactory.setReadTimeout(maxReadTime);
        return new RestTemplate(requestFactory);
    }

    /**
     * 返回结果解析
     *
     * @param responseEntity
     * @return
     */
    private static Object resolveHttpResult(ResponseEntity responseEntity) throws Exception {
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        throw new IllegalStateException("http调用失败，返回的code是" + responseEntity.getStatusCodeValue());
    }

    /**
     * get 请求
     *
     * @param url
     * @param restTemplate
     * @return
     * @throws Exception
     */
    public static Object doGet(String url, RestTemplate restTemplate) throws Exception {
        RestTemplate restTemplates = Objects.nonNull(restTemplate) ? restTemplate : getRestTemplate();
        ResponseEntity responseEntity = restTemplates.getForEntity(url, Object.class);
        return resolveHttpResult(responseEntity);
    }

    /**
     * post请求，请求方式：JSON
     *
     * @param param
     * @param url
     * @param restTemplate
     * @return
     * @throws Exception
     */
    public static Object doPostJson(JSONObject param, String url, RestTemplate restTemplate) throws Exception {
        RestTemplate restTemplates = Objects.nonNull(restTemplate) ? restTemplate : getRestTemplate();
        //设置提交json格式数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity<>(param, headers);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, request, Object.class);
        return resolveHttpResult(responseEntity);
    }

    /**
     * post请求 form格式
     *
     * @param param
     * @param url
     * @return
     * @throws Exception
     */
    public static Object doPostForm(MultiValueMap<String, String> param, String url, RestTemplate restTemplate) throws Exception {
        RestTemplate restTemplates = Objects.nonNull(restTemplate) ? restTemplate : getRestTemplate();
        //设置表单提交
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity<>(param, headers);
        ResponseEntity responseEntity = restTemplates.postForEntity(url, request, Object.class);
        return resolveHttpResult(responseEntity);
    }
}
