package com.video.download.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.download.common.context.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Author xiaom
 * @Date 2019/11/22 11:00
 * @Version 1.0.0
 * @Description <>
 **/
@Slf4j
public class HttpUtils {

    /**
     * Post request.
     */
    public static ResponseEntity<String> postForEntity(String url, Map body){
        RestTemplate restTemplate = getRestTemplate();
        HttpEntity<MultiValueMap<String, String>> map = buildHttpEntity(body);
        log.info("Process server. url:[{}] body:[{}]",url, JSON.toJSONString(map));
        return restTemplate.postForEntity(url,map, String.class);
    }

    public static <T> ResponseEntity<T> postForEntity(String url, Map body,Class<T> cls){
        ResponseEntity<String> responseEntity = postForEntity(url, body);
        if(responseEntity.getStatusCodeValue() != HttpStatus.OK.value()){
            return new ResponseEntity(null,HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(JSON.parseObject(responseEntity.getBody(),cls));
    }


    public static ResponseEntity<JSONObject> postForEntity(String url, Object body){
        RestTemplate restTemplate = getRestTemplate();
        log.info("Process server. url:[{}] body:[{}]",url, JSON.toJSONString(body));
        return restTemplate.postForEntity(url,body, JSONObject.class);
    }

    private static RestTemplate getRestTemplate(){
        return SpringContext.getApplicationContext().getBean(RestTemplate.class);
    }

    private static HttpEntity<MultiValueMap<String, String>> buildHttpEntity(Map map){
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        if(map != null && !map.isEmpty()){
            map.keySet().parallelStream().forEach(t -> multiValueMap.set(t+"",map.get(t)+""));
        }
        return new HttpEntity<>(multiValueMap, buildHeaders());
    }

    private static HttpHeaders buildHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

}
