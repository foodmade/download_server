package com.video.download.common.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author xiaom
 * @Date 2020/1/9 16:34
 * @Version 1.0.0
 * @Description <>
 **/
@NoArgsConstructor
@Data
public class RecommendVideoEntity {
    private String id;
    private String thumb;
    private String title;
    private int coins;
    private String duration;
    private String durationStr;
    private String like;
    private String comment;
    private String thumbImg;
    private int thumbWidth;
    private int thumbHeight;
    private String playUrl;
    private String shareUrl;
    private String nickName;
    private String status;
    private List<String> tags;
}
