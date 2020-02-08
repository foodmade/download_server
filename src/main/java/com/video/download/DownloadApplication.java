package com.video.download;

import com.video.download.common.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class DownloadApplication {

    public static void main(String[] args) {
        SpringApplication.run(DownloadApplication.class, args);
    }
    @Bean
    public DataLoader dataLoader() {
        return new DataLoader();
    }

    @Order
    static class DataLoader implements CommandLineRunner {

        @Override
        public void run(String... args) {
            //Init redis component
            RedisUtil.initRedis();
            log.info("System before component init successful.");
        }
    }
}
