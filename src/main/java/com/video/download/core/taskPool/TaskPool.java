package com.video.download.core.taskPool;

import com.video.download.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 任务池
 */
@Slf4j
public class TaskPool {

    /**
     * 任务队列
     * key   -- 任务类型
     * value -- 任务队列
     */
    private static HashMap<String, ConcurrentLinkedQueue<QueryTaskNode>> taskPool = new HashMap<>();

    /**
     * 任务索引
     */
    private static ConcurrentHashMap<String,QueryTaskNode> taskIndexMap = new ConcurrentHashMap<>();


    /**
     * 从任务池中获取一个任务
     * @param task_type  任务类型
     */
    public static TaskData getSubTask(String task_type){
        if(taskPool.isEmpty()){
            log.debug("任务池为空,无任务可执行");
            return null;
        }
        ConcurrentLinkedQueue<QueryTaskNode> queue = taskPool.get(task_type);
        if(queue == null || queue.isEmpty()){
            log.debug("当前任务类型:"+task_type+" 主节点任务为空,无需执行");
            return null;
        }
        QueryTaskNode curQueryTaskNode = null;
        try{
            curQueryTaskNode = queue.poll();

            if(!taskIndexMap.containsKey(curQueryTaskNode.getQuery_id())){
                log.error("异常的query_id:"+curQueryTaskNode.getQuery_id());
                return null;
            }
            if(curQueryTaskNode.getFinish()){
                return null;
            }
            if(curQueryTaskNode.getUnOverTaskQueue() == null || curQueryTaskNode.getUnOverTaskQueue().size() == 0){
                return null;
            }
            return curQueryTaskNode.getUnOverTask();

        }catch (Exception e){
            log.error("获取taskData出现异常 e:"+e.getMessage());
        }finally {
            if(curQueryTaskNode != null)
                queue.offer(curQueryTaskNode);
        }
        return null;
    }

    /**
     * 关闭小任务
     * @param task_type  任务类型
     * @param task_id    子任务id
     * @param query_id   大任务id
     */
    public static void closeTask(String task_type, String task_id, String query_id){
        if(CommonUtils.isEmpty(task_id) || CommonUtils.isEmpty(task_type) || CommonUtils.isEmpty(query_id)){
            log.error("关闭taskData时,任务类型和任务id都不能为空");
            return;
        }
        if(!taskIndexMap.containsKey(query_id)){
            log.error("异常的query_id:"+query_id +" 在任务索引表里未找到,任务关闭失败 query_id:"+query_id +" task_id:"+task_id);
            return;
        }
        QueryTaskNode queryTaskNode = taskIndexMap.get(query_id);
        if(queryTaskNode == null || queryTaskNode.getQuery_id() == null){
            log.error("获取到的任务节点为空 ,关闭任务失败 query_id:"+query_id+" task_id:"+task_id);
            return;
        }
        queryTaskNode.closeTask(task_id);
    }

    /**
     * 关闭QueryNode
     * @param task_type  任务类型
     * @param query_id   任务id
     */
    public static void closeQueryNode(String task_type, String query_id){
        if(CommonUtils.isEmpty(task_type) || CommonUtils.isEmpty(query_id)){
            log.error("关闭QueryNode时,任务类型和任务id都不能为空");
            return;
        }
        ConcurrentLinkedQueue<QueryTaskNode> queue = taskPool.get(task_type);
        if(queue == null || queue.isEmpty()){
            log.error("关闭QueryNode时,任务池中不存在query_id:"+query_id+" 的任务,关闭失败");
            return;
        }
        QueryTaskNode queryTaskNode = taskIndexMap.get(query_id);
        taskIndexMap.remove(query_id);
        queue.remove(queryTaskNode);
        log.info("关闭QueryNode成功 query_id:"+query_id +"task_type:"+task_type);
    }

    /**
     * 添加任务
     */
    public static void addSubTask(TaskParams taskParams){
        if(taskParams == null || taskParams.getMaxPage() <= 0){
            log.error("创建任务失败,任务最大页码异常或者任务参数为空");
            return;
        }
        String task_type = taskParams.getTaskType();
        if(CommonUtils.isEmpty(task_type)){
            log.error("创建任务时,task_type不能为空 task_type:"+task_type);
            return;
        }
        if(taskParams.getChildCateInfo() == null){
            log.error("创建任务时,childCateInfo不能为空");
            return;
        }
        QueryTaskNode queryTaskNode = new QueryTaskNode();
        queryTaskNode.setTask_type(task_type);
        String query_id = CommonUtils.getUuid();
        queryTaskNode.setQuery_id(query_id);
        addTaskToPool(queryTaskNode,taskParams,false);
    }

    private static void forechTaskData(TaskParams taskParams, QueryTaskNode queryTaskNode,
                                       HashMap<String, String> sourceCateIdMap, String sourceCateId){
        for (int i = 1;i<=taskParams.getMaxPage();i++){
            TaskData taskData = new TaskData();
            taskData.setStatus(TaskConst.WAIT_EXECUTE);
            taskData.setTimestamp(System.currentTimeMillis());
            taskData.setRetry(0);
            taskData.setQuery_id(queryTaskNode.getQuery_id());
            taskData.setTask_id(CommonUtils.getUuid());
            taskData.setUrl(taskParams.getUrl());
            taskData.setTask_type(taskParams.getTaskType());
            taskData.setPage(i);
            taskData.setDesc(taskParams.getDesc());
            taskData.setChildurl(taskParams.getChildUrl());
            taskData.setTotalstage(taskParams.getChildUrl().size());
            taskData.setStage(taskParams.getStage());
            taskData.setVideoid(taskParams.getVideoId());
            taskData.setParentid(taskParams.getParentId());
            if(sourceCateIdMap != null && !sourceCateIdMap.isEmpty()){
                String localhostCateStr = sourceCateIdMap.get(sourceCateId);
                taskData.setCateid(Integer.parseInt(localhostCateStr));
                taskData.setSourcecateid(sourceCateId);
                taskData.setCatename("制服");
                taskData.setChildCateInfo(sourceCateIdMap);
            }
            queryTaskNode.addTask(taskData);
        }
    }

    private static void addTaskToPool(QueryTaskNode queryTaskNode, TaskParams taskParams,boolean isAppend){
        long start = System.currentTimeMillis();
        HashMap<String, String> sourceCateIdMap = taskParams.getChildCateInfo();
        if(sourceCateIdMap == null || sourceCateIdMap.isEmpty()){
            forechTaskData(taskParams, queryTaskNode,null,null);
        }else{
            for (String sourceCateId : sourceCateIdMap.keySet()) {
                forechTaskData(taskParams, queryTaskNode,sourceCateIdMap,sourceCateId);
            }
        }
        if(isAppend){
            return;
        }
        String task_type = taskParams.getTaskType();
        ConcurrentLinkedQueue<QueryTaskNode> queue = taskPool.get(task_type);
        if(queue == null ){
            queue = new ConcurrentLinkedQueue<>();
        }
        queue.add(queryTaskNode);
        taskPool.put(task_type,queue);
        taskIndexMap.put(queryTaskNode.getQuery_id(),queryTaskNode);
        log.debug("创建【"+taskParams.getMaxPage()+"】条任务 task_type:【"+task_type+"】 耗时:【"+(System.currentTimeMillis()-start)+"】");
    }

    /**
     * 追加任务至任务节点
     * @param queryTaskNode  待追加的任务节点
     * @param taskParams     新任务参数
     */
    public static void appendSubTaskToNode(QueryTaskNode queryTaskNode, TaskParams taskParams){
        if(queryTaskNode == null){
            return;
        }
        if(CommonUtils.isEmpty(queryTaskNode.getQuery_id())){
            log.error("大任务节点初始结构异常，queryId不能为空");
            return;
        }
        addTaskToPool(queryTaskNode,taskParams,true);
    }

    /**
     * 获取任务节点
     * @param query_id 大任务id
     */
    public static QueryTaskNode getQueryTaskNode(String query_id){
        if(CommonUtils.isEmpty(query_id)){
            return null;
        }
        if(!taskIndexMap.containsKey(query_id)){
            return null;
        }
        return taskIndexMap.get(query_id);
    }

    /**
     * 获取任务节点详情
     */
    public static QueryInfo getQueryInfo(String query_id, String task_type){
        if(CommonUtils.isEmpty(query_id) || CommonUtils.isEmpty(task_type)){
            log.error("获取任务节点详情失败,必须参数不全 query_id:"+query_id+"  task_type:"+task_type);
            return null;
        }
        QueryInfo queryInfo = new QueryInfo();
        if(!taskIndexMap.containsKey(query_id)){
            log.error("获取任务节点详情失败,不存在任务索引表的query_id:"+query_id);
            return queryInfo;
        }
        QueryTaskNode queryTaskNode = taskIndexMap.get(query_id);
        if(queryTaskNode == null){
            return queryInfo;
        }

        queryInfo.setUnOverTaskCnt(queryTaskNode.getUnOverTaskQueue().size());
        queryInfo.setOverTaskCnt(queryTaskNode.getOverTaskQueue().size());
        return queryInfo;
    }

    public static void clear() {
        taskPool.clear();
        taskIndexMap.clear();
    }
}
