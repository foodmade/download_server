package com.video.download.core.taskPool;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class QueryTaskNode {

    /**
     * 未完成任务队列
     */
    private ConcurrentLinkedQueue<TaskData> unOverTaskQueue = new ConcurrentLinkedQueue<>();

    /**
     * 已完成任务队列
     */
    private Set<TaskData> overTaskQueue = new HashSet<>();

    /**
     * 任务索引
     */
    private ConcurrentHashMap<String, TaskData> taskDataMap = new ConcurrentHashMap<>();
    /**
     * 已完成的页码
     */
    private Set<Integer> overPageSet = new HashSet<>();

    private String query_id;

    private String task_type;

    private Boolean isFinish = false;

    public String getQuery_id() {
        return query_id;
    }

    public void setQuery_id(String query_id) {
        this.query_id = query_id;
    }

    public String getTask_type() {
        return task_type;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }

    public Set<Integer> getOverPageSet() {
        return overPageSet;
    }

    public void setOverPageSet(Set<Integer> overPageSet) {
        this.overPageSet = overPageSet;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public ConcurrentLinkedQueue<TaskData> getUnOverTaskQueue() {
        return unOverTaskQueue;
    }

    public void setUnOverTaskQueue(ConcurrentLinkedQueue<TaskData> unOverTaskQueue) {
        this.unOverTaskQueue = unOverTaskQueue;
    }

    public Set<TaskData> getOverTaskQueue() {
        return overTaskQueue;
    }

    public void setOverTaskQueue(Set<TaskData> overTaskQueue) {
        this.overTaskQueue = overTaskQueue;
    }

    /**
     * 添加任务
     */
    public void addTask(TaskData taskData){
        if(taskData == null){
            log.error("不能添加空任务");
            return;
        }
        if(taskDataMap.containsKey(taskData.getTask_id())){
            log.error("任务已存在,不能重复添加 task_id:"+taskData.getTask_id());
            return;
        }
        unOverTaskQueue.add(taskData);
        taskDataMap.put(taskData.getTask_id(),taskData);
    }

    /**
     * 关闭任务
     */
    public void closeTask(String task_id){
        if(task_id == null || task_id.equals("")){
            log.error("异常的任务id task_id:"+task_id);
            return;
        }
        if(!taskDataMap.containsKey(task_id)){
            log.error("不存在的task_id,任务关闭失败 task_id:"+task_id);
            return;
        }
        TaskData taskData = taskDataMap.get(task_id);

        if(taskData == null){
            log.error("从索引表中获取到的任务为空 task_id:"+task_id);
            return;
        }

        taskData.setStatus(TaskConst.FINISH_EXECUTE);
        overTaskQueue.add(taskData);
        unOverTaskQueue.remove(taskData);

        log.info("关闭任务成功 task_id:"+task_id+" 剩餘任務:"+unOverTaskQueue.size());
    }

    /**
     * 获取任务
     */
    TaskData getUnOverTask(){
        if(unOverTaskQueue.isEmpty()){
            log.debug("任务池已无任务 task_type:"+this.task_type);
            return null;
        }
        TaskData taskData = null;
        Long timeStamp = System.currentTimeMillis();
        for (TaskData data : unOverTaskQueue){
            if(data == null){
                continue;
            }
            if(data.getStatus().equals(TaskConst.WAIT_EXECUTE)){             //说明任务处于初始状态,可以执行
                unOverTaskQueue.remove(data);
                data.setStatus(1);
                data.setTimestamp(timeStamp);
                unOverTaskQueue.add(data);
                taskData = data;
                break;
            }
            if(data.getStatus().equals(TaskConst.LOGING_EXECUTE)){          //说明任务是处于执行状态  需要判断任务是否超时,是则继续交给爬虫执行 反之跳过
                unOverTaskQueue.remove(data);
                if( 1==1 ||(data.getTimestamp() + 5000) >= timeStamp){               //说明距离上次取任务时间已经超过5S 可以判定上次爬虫执行任务已经失败,进行重试
                    data.setRetry(data.getRetry()+1);
                    if(data.getRetry() >= 5){
                        log.error("task_id:"+data.getTask_id() + " 已经重试:"+data.getRetry()+ "次 移除任务");
                        taskDataMap.remove(data.getTask_id());
                        break;
                    }
                    data.setTimestamp(timeStamp);
                    data.setStatus(1);
                    unOverTaskQueue.add(data);
                    taskData = data;
                    break;
                }else{                                                      //任务处于等待提交阶段,重新放回队列
                    unOverTaskQueue.add(data);
                    break;
                }
            }
        }
        return taskData;
    }
}
