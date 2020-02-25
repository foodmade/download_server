package com.video.download;

import com.alibaba.fastjson.JSONObject;
import com.video.download.common.http.HttpUtils;
import com.video.download.common.redis.RedisUtil;
import com.video.download.dao.IAccountService;
import com.video.download.job.CrawlerJob;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xiaom
 * @Date 2020/1/8 11:21
 * @Version 1.0.0
 * @Description <>
 **/
public class CommonTest extends DownloadApplicationTests{

    @Test
    public void testRedis(){
        String url = "https://m3u8.cnkamax.com/useruploadfiles/37341e12d851dd9e55076aef8af0211f/37341e12d851dd9e55076aef8af0211f.m3u8?md5=M0F1UVFVbaz00nJGcME8tw";
        Pattern pattern = Pattern.compile("^(http[s]?:\\/\\/m3u8\\.cnkamax\\.com\\/useruploadfiles\\/)+.*?(\\.m3u8)");
        Matcher matcher= pattern.matcher(url);
        String patteen = "";
        while(matcher.find()){
            String s=matcher.group();
            System.out.println(s);
        }
    }

}
