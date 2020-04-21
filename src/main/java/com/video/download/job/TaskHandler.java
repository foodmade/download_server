package com.video.download.job;

import com.alibaba.fastjson.JSON;
import com.video.download.common.domain.Request91Entity;
import com.video.download.common.domain.Response91Entity;
import com.video.download.common.encrypt.Encryption;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Author xiaom
 * @Date 2020/1/9 16:12
 * @Version 1.0.0
 * @Description <>
 **/
@Component
@ConfigurationProperties(prefix = "crawler")
@Slf4j
@Data
public class TaskHandler implements Serializable {

    private static final long serialVersionUID = -4588923720114655613L;

    private String url;

    private String data;

    public Request91Entity buildRequest(){
        String currentTime = System.currentTimeMillis() + "";
        String timestamp = currentTime.substring(0,10);
        String sign = Encryption.sign(timestamp,data);
        return new Request91Entity(timestamp,data,sign);
    }

    public Response91Entity decryptData(String data){
        String jsonStr = Encryption.decrypt(data);
        return JSON.parseObject(jsonStr,Response91Entity.class);
    }

}
