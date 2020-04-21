package com.video.download.dao.repository;

import com.video.download.domain.entity.SpiderControl;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author xiaom
 * @Date 2020/3/24 11:19
 * @Version 1.0.0
 * @Description <>
 **/
@Repository
public interface SpiderControlRepository extends CrudRepository<SpiderControl,Long>, JpaSpecificationExecutor<SpiderControl> {
}
