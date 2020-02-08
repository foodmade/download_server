package com.video.download.common.redis;


/**
 * @Author xiaom
 * @Date 2020/1/13 10:42
 * @Version 1.0.0
 * @Description <>
 **/
public class RedisKeyBuild {

    private static final String CACHE_PREFIX = ":";

    private static final String PREFIX = "API";

    public static final String PUSH_FINISH_KEY = "PUSH_FINISH_KEY";

    /**
     * 已推送完毕的视频队列
     * @param key1 时间/日期
     * @param key2 ip
     */
    public static String buildPushFinishKey(String key1,String key2){
        return buildKey(PUSH_FINISH_KEY,key1,key2);
    }

    private static String buildKey(Object str1, Object... array) {
        StringBuilder stringBuffer = new StringBuilder(PREFIX);
        stringBuffer.append(CACHE_PREFIX).append(str1);
        for (Object obj : array) {
            stringBuffer.append(CACHE_PREFIX).append(obj);
        }
        return stringBuffer.toString();
    }

}
