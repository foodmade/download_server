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
@Data
@NoArgsConstructor
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
    private String uuid;
    private boolean hasBuy;
    private boolean hasLongVideo;
    private int follow;
    private String is_hide;
    private boolean isLogin;
    private boolean isLiked;
    private boolean isFollowed;
    private String music_id;
    private String news_num;

    @NoArgsConstructor
    @Data
    public static class NewsInfoBean {
        private String idX;
        private String uuidX;
        private String content;
        private String is_best;
        private String photo_num;
        private String video_num;
        private String view_num;
        private String img;
    }
}
