package com.video.download.core.handler;

import com.alibaba.fastjson.JSON;
import com.video.download.common.CommonUtils;
import com.video.download.domain.TaskData;
import com.video.download.domain.WorkData;
import com.video.download.domain.entity.Movies;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class DefaultAfterHandler extends AbsSave {


    @Override
    public void afterStageOneDataCache(WorkData workData) {
        if(workData.getTaskData() == null){
            log.warn("异常的提交任务 taskData等于空 stage:0");
            return;
        }
        String query_id = workData.getTaskData().getQuery_id();
        if(CommonUtils.isEmpty(query_id)){
            log.warn("seq:47328  query_id为空");
            return;
        }
        HashMap<String, Movies> nodeCacheMap = cacheVideoMap.get(query_id);
        if(nodeCacheMap == null || nodeCacheMap.isEmpty()){
            nodeCacheMap = new HashMap<>();
        }
        List<Movies> videoItems = workData.getItems();
        if(videoItems == null ||videoItems.isEmpty()){
            return;
        }
        TaskData taskData = workData.getTaskData();
        for (Movies item:videoItems){
            item.setCateId(taskData.getCateid());
            item.setParentId(taskData.getParentid());
            item.setCateName(taskData.getCatename());
            nodeCacheMap.put(item.getVideoId(),item);
        }
        cacheVideoMap.put(query_id,nodeCacheMap);
    }

    @Override
    public void afterStageTwoDataCache(WorkData workData) {
        if(workData.getTaskData() == null){
            log.warn("异常的提交任务 taskData等于空 stage:1");
            return;
        }
        String query_id = workData.getTaskData().getQuery_id();
        if(CommonUtils.isEmpty(query_id)){
            log.warn("seq:47329  query_id为空");
            return;
        }
        List<Movies> items = workData.getItems();
        log.debug("第二阶段任务提交内容:"+ JSON.toJSONString(items));
        if(items == null || items.size() == 0){
            return;
        }
        HashMap<String,Movies> itemMap = cacheVideoMap.get(query_id);
        if(itemMap == null || itemMap.isEmpty()){
            return;
        }
        //合并第一阶段数据
        TaskData taskData = workData.getTaskData();
        if(taskData == null){
            return;
        }
        String videoId = taskData.getVideoid();
        if(CommonUtils.isEmpty(videoId)){
            return;
        }
        if(!itemMap.containsKey(videoId)){
            return;
        }
        Movies item = itemMap.get(videoId);
        item.setVideoSourceList(items.get(0).getVideoSourceList());
        if(!CommonUtils.isEmpty(items.get(0).getVideoDesc())){
            item.setVideoDesc(items.get(0).getVideoDesc());
        }

        if(taskData.isFinish()){
            addOverCache(item);
            itemMap.remove(videoId);
        }else{
            itemMap.put(videoId,item);
            cacheVideoMap.put(query_id,itemMap);
        }

    }
}
