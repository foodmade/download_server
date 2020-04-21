package com.video.download.dao.repository;

import com.video.download.domain.entity.DynamicParser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author xiaom
 * @Date 2020/3/24 11:42
 * @Version 1.0.0
 * @Description <>
 **/
public interface ParserRepository extends CrudRepository<DynamicParser,Long>, JpaSpecificationExecutor<DynamicParser> {

    /**
     * 获取爬虫配置文件
     * @param config  类型
     * @param spiderType 爬虫类型
     */
    Optional<List<DynamicParser>> findByConfigTypeAndSpiderType(String config, String spiderType);
}
