package com.video.download.control;

import com.alibaba.fastjson.JSON;
import com.video.download.common.domain.BaseResult;
import com.video.download.job.CrawlerJob;
import com.video.download.service.ApiService;
import com.video.download.vo.PushTaskVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author xiaom
 * @Date 2020/1/8 11:29
 * @Version 1.0.0
 * @Description <>
 **/
@RestController
@RequestMapping(value = "/api/")
@Slf4j
public class Api {

    @Autowired
    private ApiService apiService;

    @Autowired
    private CrawlerJob crawlerJob;

    @GetMapping(value = "/commitUrl")
    public BaseResult commitM3u8Path(String url){
        System.out.println("提交的url:" + url);
        apiService.commitUrl(url);
        return BaseResult.success(null);
    }

    @GetMapping(value = "/getDownloadTask")
    public BaseResult getDownloadTask(){
        BaseResult baseResult = BaseResult.success(apiService.getDownloadUrl());
        log.info("获取到任务:{}", JSON.toJSONString(baseResult));
        return baseResult;
    }

    @GetMapping(value = "/testPostUrl")
    public BaseResult testPostUrl(){
        crawlerJob.fetchPathJob();
        return BaseResult.success(null);
    }

    /**
     * 提交已推送完毕的视频信息
     */
    @PostMapping(value = "/commitTaskStatus")
    public BaseResult commitTaskStatus(@RequestBody PushTaskVo pushTaskVo){
        apiService.commitTaskStatus(pushTaskVo);
        return BaseResult.success(null);
    }

    /**
     * 初始化爬虫为最初始状态 (用于更新配置文件或者解析器时调用)
     * 调用此接口后,所有爬虫将会执行更新config和parser操作
     */
    @GetMapping(value = "/restoreSpiderStatus")
    public BaseResult restoreSpiderStatus(){
        return apiService.restoreSpiderStatus();
    }

    /**
     * 生成爬虫任务
     * 1 查询数据库中处于未激活状态的任务表达式
     * 2 创建任务到本机任务池
     */
    @GetMapping(value = "/generateTask")
    public BaseResult generateTask(){
        return apiService.generateTask();
    }
}
