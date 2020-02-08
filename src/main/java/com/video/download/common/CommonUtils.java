package com.video.download.common;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xiaom
 * @Date 2020/1/10 9:37
 * @Version 1.0.0
 * @Description <>
 **/
@Service
public class CommonUtils {

    /**
     * MD5加密
     * @param str  原始字符串
     * @return 加密字符串
     */
    public static String MD5(String str){
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析除?号以后的url地址
     * @param url  原始url
     * @return 地址
     */
    public static String originalUrl(String url){
        Pattern pattern = Pattern.compile("^(http[s]?:\\/\\/m3u8\\.cnkamax\\.com\\/useruploadfiles\\/)+.*?(\\.m3u8)");
        Matcher matcher= pattern.matcher(url);
        while(matcher.find()){
            return matcher.group();
        }
        return null;
    }

}
