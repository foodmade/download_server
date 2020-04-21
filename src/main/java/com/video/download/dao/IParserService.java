package com.video.download.dao;

import com.video.download.common.enums.SpiderTypeEnum;
import com.video.download.domain.entity.DynamicParser;

import java.util.List;
import java.util.Optional;

/**
 * @Author xiaom
 * @Date 2020/3/24 13:57
 * @Version 1.0.0
 * @Description <>
 **/
public interface IParserService {

    /**
     * 获取爬虫配置文件
     * @param typeEnum 爬虫类型
     * @return 配置文件文本
     */
    DynamicParser getConfig(SpiderTypeEnum typeEnum);

    /**
     * 获取爬虫解析器
     * @param typeEnum 爬虫类型
     * @return 爬虫解析器
     */
    List<DynamicParser> getParsers(SpiderTypeEnum typeEnum);

}
