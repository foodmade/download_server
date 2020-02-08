package com.video.download.common.domain;

import com.video.download.common.BeanUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xiaom
 * @Date 2020/1/9 16:14
 * @Version 1.0.0
 * @Description <>
 **/
@Data
@AllArgsConstructor
public class Request91Entity {

    private String timestamp;

    private String data;

    private String sign;

    public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<>();
        map.put("timestamp",timestamp);
        map.put("data",data);
        map.put("sign",sign);
        return map;
    }

}
