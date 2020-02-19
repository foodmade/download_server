package com.video.download.dao.repository;

import com.video.download.domain.entity.UUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author xiaom
 * @Date 2020/2/14 17:44
 * @Version 1.0.0
 * @Description <>
 **/
@Repository
public interface UserRepository extends CrudRepository<UUser,Long>, JpaSpecificationExecutor<UUser> {

    /**
     * 查询用户列表
     * @param type    机器人类型(20)
     * @param status  用户状态
     * @return 用户列表
     */
    Optional<List<UUser>> findByTypeAndStatus(Integer type,Integer status);
}
