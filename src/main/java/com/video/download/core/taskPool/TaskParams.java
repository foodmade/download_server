package com.video.download.core.taskPool;

import lombok.Data;

import java.util.HashMap;

@Data
public class TaskParams {

    private String url;

    private Integer maxPage;

    private String taskType;

    private String desc;

    private Integer status;

    private Integer stage = 0;

    private String videoId;

    private Integer parentId;

    private HashMap<String, String> childCateInfo;

    private HashMap<String,String> childUrl;
}
