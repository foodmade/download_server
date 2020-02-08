package com.video.download.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author xiaom
 * @Date 2020/1/13 10:35
 * @Version 1.0.0
 * @Description <>
 **/
@Data
public class PushTaskVo implements Serializable {

    private String finishTime;

    private Task task;

    @Data
    public static class Task{

        private String title;

        private String tags;

        private Long fileId;
    }

}
