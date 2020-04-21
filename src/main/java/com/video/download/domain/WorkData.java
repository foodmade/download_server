package com.video.download.domain;


import com.video.download.domain.entity.Movies;

import java.util.List;

public class WorkData {

    private TaskData taskData;

    private List<Movies> items;

    private Integer retcode;

    public Integer getRetcode() {
        return retcode;
    }

    public void setRetcode(Integer retcode) {
        this.retcode = retcode;
    }

    public TaskData getTaskData() {
        return taskData;
    }

    public void setTaskData(TaskData taskData) {
        this.taskData = taskData;
    }

    public List<Movies> getItems() {
        return items;
    }

    public void setItems(List<Movies> items) {
        this.items = items;
    }
}
