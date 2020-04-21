package com.video.download.dao.repository;

import com.video.download.domain.entity.Movies;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @Author xiaom
 * @Date 2020/3/24 11:35
 * @Version 1.0.0
 * @Description <>
 **/
public interface MoviesRepository extends CrudRepository<Movies,Long>, JpaSpecificationExecutor<Movies> {

}
