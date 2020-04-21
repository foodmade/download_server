package com.video.download.core.handler;


import com.video.download.domain.WorkData;
import com.video.download.domain.entity.Movies;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public abstract class AbsSave {

    /**
     * 待完成数据缓存
     */
    public static HashMap<String, HashMap<String, Movies>> cacheVideoMap = new HashMap<>();
    /**
     * 已完成数据缓存
     */
    public static List<Movies> finishCache = new ArrayList<>();

    public static List<Movies> getFinishCache(){
        List<Movies> movies = new ArrayList<>(finishCache);
        finishCache.clear();
        log.info("取出待保存数据:[{}]个",movies.size());
        return movies;
    }

    public static void addOverCache(Movies movie){
        finishCache.add(movie);
        log.info("处理完毕,添加到待保存队列成功,长度:[{}]",finishCache.size());
    }

    /**
     * 第一阶段保存
     */
    public void afterStageOneDataCache(WorkData workData){}

    /**
     * 第二阶段保存
     */
    public void afterStageTwoDataCache(WorkData workData){}

}
