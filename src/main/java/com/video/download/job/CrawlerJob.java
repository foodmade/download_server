package com.video.download.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.video.download.common.CommonUtils;
import com.video.download.common.domain.RecommendVideoEntity;
import com.video.download.common.domain.Request91Entity;
import com.video.download.common.domain.Response91Entity;
import com.video.download.common.encrypt.Encryption;
import com.video.download.common.http.HttpUtils;
import com.video.download.common.redis.RedisConst;
import com.video.download.common.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author xiaom
 * @Date 2020/1/9 15:16
 * @Version 1.0.0
 * @Description <>
 **/
@Component
@Slf4j
public class CrawlerJob {

    @Autowired
    private TaskHandler taskHandler;

    /**
     * 每隔20S获取一次91视频列表
     */
    @Scheduled(fixedDelay = 1000 * 20)
    public void fetchPathJob(){
        Request91Entity request = taskHandler.buildRequest();
        ResponseEntity<Response91Entity> responseEntity = HttpUtils.postForEntity(taskHandler.getUrl(),request.toMap(), Response91Entity.class);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            log.warn("获取91视频列表失败, httpCode:[{}]",responseEntity.getStatusCodeValue());
           return;
        }
        Response91Entity response91Entity = responseEntity.getBody();
        if(response91Entity == null){
            return;
        }

        String originalStr = response91Entity.getData();
        String decryptStr = Encryption.decrypt(originalStr);

//        log.debug("原始data:{}", originalStr);
//        log.debug("解密data:{}", decryptStr);

        JSONObject jsonObject = JSONObject.parseObject(decryptStr);
        jsonObject = jsonObject.getJSONObject("data");
        List<RecommendVideoEntity> videoList = jsonObject.getObject("recommendedData",new TypeReference<List<RecommendVideoEntity>>(){});

//        log.info("视频集合:{}", JSON.toJSONString(videoList));
        if(videoList == null || videoList.isEmpty()){
            return;
        }
        //todo: 暂时只缓存到redis, Mysql后面考虑
        saveVideoList(videoList);
    }

    private void saveVideoList(List<RecommendVideoEntity> videoList) {

        StringRedisTemplate stringRedisTemplate = RedisUtil.getInstance().getStringRedisTemplate();

        //使用管道模式
        stringRedisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
            for (RecommendVideoEntity recommendVideoEntity : videoList) {
                try {

                    List<String> tags = recommendVideoEntity.getTags();
                    if(!tags.isEmpty()){
                        tags = tags.stream().map(String::trim).collect(Collectors.toList());
                        if(tags.contains("原創")){
                            //如果是91原创视频,则跳过
                            continue;
                        }
                    }

                    StringRedisConnection redisConn = (StringRedisConnection)redisConnection;
                    //缓存格式. 视频播放地址MD5为key. recommendVideoEntity为data
                    String uri = CommonUtils.originalUrl(recommendVideoEntity.getPlayUrl());

                    String key = CommonUtils.MD5(uri);

                    //判断是否为重复资源
                    boolean isOver = RedisUtil.getInstance().isMember(RedisConst.OVER_PAGE_SET,key);
                    if(isOver){
                        continue;
                    }

                    //任务队列
                    redisConn.sAdd(RedisConst.DOWNLOAD_QUEUE,JSON.toJSONString(recommendVideoEntity));
                    //令牌队列
                    redisConn.sAdd(RedisConst.OVER_PAGE_SET,key);
                }catch (Exception e){
                    log.error("缓存视频集合出现异常 exMsg:{}",e.getMessage());
                }
            }
            return null;
        });
    }

}
