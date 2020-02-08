package com.video.download.common.domain;

import lombok.Data;
import lombok.ToString;

/**
 * @Author xiaom
 * @Date 2020/1/9 15:44
 * @Version 1.0.0
 * @Description <>
 **/
@Data
@ToString
public class Response91Entity {

    private Integer errcode;

    private String timestamp;

    private String data;

    private String sign;

}
