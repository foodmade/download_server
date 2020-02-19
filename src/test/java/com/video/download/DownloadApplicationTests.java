package com.video.download;

import com.video.download.dao.IAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DownloadApplicationTests {

    @Autowired
    private IAccountService accountService;

    @Test
    void contextLoads() {
        accountService.getAllRobotAccount();
    }

}
