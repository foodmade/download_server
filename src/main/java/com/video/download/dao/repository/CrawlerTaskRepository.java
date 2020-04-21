package com.video.download.dao.repository;

import com.video.download.domain.entity.CrawlerTask;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @Author xiaom
 * @Date 2020/3/24 11:56
 * @Version 1.0.0
 * @Description <>
 **/
public interface CrawlerTaskRepository extends CrudRepository<CrawlerTask,Long>, JpaSpecificationExecutor<CrawlerTask> {

    List<CrawlerTask> findByStatus(Integer status);
}
