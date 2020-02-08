package com.video.download.service;

import com.video.download.vo.PushTaskVo;

/**
 * @Author xiaom
 * @Date 2020/1/8 11:38
 * @Version 1.0.0
 * @Description <>
 **/
public interface ApiService {

    String getDownloadUrl();

    void commitUrl(String url);

    /**
     * 记录视频下载器推送的视频信息
     */
    void commitTaskStatus(PushTaskVo pushTaskVo);
}
