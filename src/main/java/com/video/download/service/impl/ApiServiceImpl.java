package com.video.download.service.impl;

import com.alibaba.fastjson.JSON;
import com.video.download.common.context.HttpRequestContextHelper;
import com.video.download.common.domain.BaseResult;
import com.video.download.common.redis.RedisConst;
import com.video.download.common.redis.RedisKeyBuild;
import com.video.download.common.redis.RedisUtil;
import com.video.download.core.taskPool.TaskPool;
import com.video.download.service.ApiService;
import com.video.download.service.CrawlerService;
import com.video.download.vo.PushTaskVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.video.download.common.CommonUtils.MD5;
import static com.video.download.common.CommonUtils.originalUrl;

/**
 * @Author xiaom
 * @Date 2020/1/8 11:39
 * @Version 1.0.0
 * @Description <>
 **/
@Service
@Slf4j
public class ApiServiceImpl implements ApiService {

    @Autowired
    private CrawlerService crawlerService;

    @Override
    public String getDownloadUrl() {
        try {
            return RedisUtil.getInstance().spop(RedisConst.DOWNLOAD_QUEUE);
        }catch (Exception e){
            log.error("获取下载资源地址异常 exMsg:{}",e.getMessage());
            return "";
        }
    }

    @Override
    public void commitUrl(String url) {

        if(!invalidAndCacheSource(url)){
            log.info("重复资源. url:{}",url);
            return;
        }
        RedisUtil.getInstance().sadd(RedisConst.DOWNLOAD_QUEUE,url);
    }

    @Override
    public void commitTaskStatus(PushTaskVo pushTaskVo) {

        if(pushTaskVo == null){
            return;
        }

        log.info("Received from video download server commit push finish task. FinishTime:{},taskInfo:{}"
                ,pushTaskVo.getFinishTime(), JSON.toJSONString(pushTaskVo.getTask()));

        //以(时间:ip) 为key,记录推送成功的视频信息
        RedisUtil.getInstance().sadd(RedisKeyBuild.PUSH_FINISH_KEY + ":" + HttpRequestContextHelper.getContextRequest().getRemoteAddr()
                ,pushTaskVo.getTask());
    }

    @Override
    public BaseResult restoreSpiderStatus() {
        crawlerService.restoreSpiderStatus();
        return BaseResult.success(true);
    }

    @Override
    public BaseResult generateTask() {
        crawlerService.generateTask();
        return BaseResult.success(true);
    }

    @Override
    public BaseResult clearTaskPool() {
        TaskPool.clear();
        return BaseResult.success(true);
    }

    /**
     * 校验资源是否已存在
     * https://m3u8.cnkamax.com/useruploadfiles/f9e8ab7c26f46a316bf5e1571bd483c1/f9e8ab7c26f46a316bf5e1571bd483c1.m3u8?md5=faD44KmHzNVhSehe08z6tw
     * @param url 视频地址
     */
    private boolean invalidAndCacheSource(String url) {

        String originalUrl = originalUrl(url);
        if(url == null){
            return false;
        }
        String md5 = MD5(originalUrl);

        if(md5 == null){
            return false;
        }

        boolean over = RedisUtil.getInstance().isMember(RedisConst.OVER_PAGE_SET,md5);
        if(over){
            return false;
        }else{
            RedisUtil.getInstance().sadd(RedisConst.OVER_PAGE_SET,md5);
        }
        return true;
    }
}
