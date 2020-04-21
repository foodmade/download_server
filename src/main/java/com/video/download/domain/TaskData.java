package com.video.download.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class TaskData implements Serializable {

    /**
     * 爬取目标网站
     */
    private String url;
    /**
     * 子任务id
     */
    private String task_id;
    /**
     * 大任务id
     */
    private String query_id;
    /**
     * 任务状态
     */
    private Integer status = 0;
    /**
     * 重试次数
     */
    private Integer retry = 0;
    /**
     * 任务获取时间戳
     */
    private Long timestamp;
    /**
     * 任务类型
     */
    private String task_type;
    /**
     * 爬取页码
     */
    private Integer page;
    /**
     * 类目id
     */
    private Integer parentid;
    /**
     * 类目映射表
     */
    private HashMap<String, String> catemapper;
    /**
     * 资源Id
     */
    private String videoid;
    /**
     * 任务描述
     */
    private String desc;
    /**
     * 子任务集合
     */
    private HashMap<String, String> childurl;
    /**
     * 任务所处阶段
     */
    private Integer stage;
    /**
     * 任务总阶段
     */
    private Integer totalstage;
    /**
     * 目标网站类目id
     */
    private String sourcecateid;
    /**
     * 本地类目子id  用于映射
     */
    private Integer cateid;

    /**
     * 本地类目名称
     */
    private String catename;
    /**
     *  目标源网站和本地类目Id的映射表
     */
    private HashMap<String, String> childCateInfo = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskData)) return false;

        TaskData taskData = (TaskData) o;

        if (!task_id.equals(taskData.getTask_id())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return task_id.hashCode();
    }

    public boolean isFinish(){
        return totalstage <= stage;
    }
}
