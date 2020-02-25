package com.video.download.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.download.common.context.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
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
        return restTemplate.postForEntity(url,parserBodyMap(body), String.class);
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

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static <T> ResponseEntity<T> sendPostForEntity(String url, Map param,Class<T> cls) {
        String body = sendPost(url,parserBodyMap(param));
        return ResponseEntity.ok(JSON.parseObject(body,cls));
    }

    private static String parserBodyMap(Map map){
        if(map.isEmpty()){
            return "";
        }

        StringBuilder body = new StringBuilder();
        int i = 0;

        for (Object key : map.keySet()) {
            if(i != 0){
                body.append("&");
            }
            body.append(key.toString())
                    .append("=")
                    .append(map.get(key));
            i++;
        }
        return body.toString();
    }

    private static HttpHeaders buildHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

}
