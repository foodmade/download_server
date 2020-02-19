package com.video.download.dao;

import com.video.download.domain.entity.UUser;

import java.util.List;

/**
 * @Author xiaom
 * @Date 2020/2/14 17:30
 * @Version 1.0.0
 * @Description <>
 **/
public interface IAccountService {

    /**
     * 获取所有机器人账户列表
     * @return 机器人账户
     */
    List<UUser> getAllRobotAccount();
}
