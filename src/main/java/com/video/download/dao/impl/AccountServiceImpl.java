package com.video.download.dao.impl;

import com.video.download.common.enums.Status;
import com.video.download.dao.IAccountService;
import com.video.download.dao.repository.UserRepository;
import com.video.download.domain.entity.UUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Author xiaom
 * @Date 2020/2/14 17:31
 * @Version 1.0.0
 * @Description <>
 **/
@Service
@Slf4j
public class AccountServiceImpl implements IAccountService {

    private final UserRepository userRepository;

    public AccountServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UUser> getAllRobotAccount() {
        return userRepository
                .findByTypeAndStatus(20, Status.NORMAL.getType())
                .orElse(null);
    }
}
