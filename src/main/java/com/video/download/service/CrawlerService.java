package com.video.download.service;


import com.video.download.common.enums.SpiderTypeEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author xiaom
 * @Date 2020/3/24 10:45
 * @Version 1.0.0
 * @Description <>
 **/
public interface CrawlerService {

    /**
     * 委派任务
     * @param typeEnum 爬虫类型
     * @return 任务结构模型
     */
    Object dispatchCrawlerTask(SpiderTypeEnum typeEnum);

    /**
     *
     * @param typeEnum
     */
    void dispatchCommitTaskResult(SpiderTypeEnum typeEnum);

    /**
     * 恢复爬虫状态
     * @return
     */
    boolean restoreSpiderStatus();

    /**
     * 生成爬虫任务
     */
    void generateTask();

}
