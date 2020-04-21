package com.video.download.core.maker;

import com.video.download.core.taskPool.TaskData;
import com.video.download.core.taskPool.TaskPool;
import com.video.download.core.util.TaskFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author xiaom
 * @Date 2020/3/24 11:10
 * @Version 1.0.0
 * @Description <>
 **/
public class BttTaskMaker extends AbstractGeneraSpiderTaskMaker {

    @Override
    protected Object getCommonTask() {
        TaskData taskData = TaskPool.getSubTask(this.getTypeEnum().getSpiderType());
        if(taskData == null){
            return null;
        }
        if(taskData.getStage() == 0){
            return TaskFactory.getBttTaskOptions(taskData);
        }else if(taskData.getStage() == 1){
            return TaskFactory.getBttDetailOptions(taskData);
        }
        return null;
    }

    @Override
    protected void commitCommonTaskResult() {
        super.commitCommonTaskResult();
    }
}
