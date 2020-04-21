package com.video.download.control;

import com.video.download.common.enums.SpiderTypeEnum;
import com.video.download.service.CrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xiaom
 * @Date 2020/3/24 10:44
 * @Version 1.0.0
 * @Description <>
 **/
@RestController
@RequestMapping(value = "/crawler")
@Slf4j
public class CrawlerApi {

    private final CrawlerService crawlerService;

    public CrawlerApi(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    /**
     * 博天堂资源网任务获取
     */
    @GetMapping(value = "/getBttTaskResult")
    public Object getBttTaskResult(){
        return crawlerService.dispatchCrawlerTask(SpiderTypeEnum.BTT);
    }

    /**
     * 博天堂采集数据提交
     */
    @PostMapping(value = "/commitBttTaskResult")
    public void commitBttTaskResult(){
        crawlerService.dispatchCommitTaskResult(SpiderTypeEnum.BTT);
    }
}
