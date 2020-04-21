package com.video.download.domain.entity;

import com.video.download.common.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author xiaom
 * @Date 2020/3/24 10:56
 * @Version 1.0.0
 * @Description <>
 **/
@NoArgsConstructor
@Data
@Entity
@Table(name = "movies")
public class Movies {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "cover_img",columnDefinition = " varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电影封面图'")
    private String coverImg;
    @Column(name = "video_type",columnDefinition = " varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '影片类型'")
    private String videoType;
    @Column(name = "video_name",columnDefinition = " varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电影名称'")
    private String videoName;
    @Column(name = "figures",columnDefinition = " text COLLATE utf8mb4_unicode_ci COMMENT '电影详细信息 导演 演员'")
    private String figures;
    @Column(name = "video_id",columnDefinition = " varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电影ID (非本平台ID,源网站ID)'")
    private String videoId;
    @Column(name = "video_desc",columnDefinition = " text COLLATE utf8mb4_unicode_ci COMMENT '电影简介'")
    private String videoDesc;
    @Column(name = "parent_id",columnDefinition = " int(11) DEFAULT NULL")
    private Integer parentId;
    @Column(name = "cate_id",columnDefinition = " int(11) NOT NULL COMMENT '类目ID'")
    private Integer cateId;
    @Column(name = "areacity",columnDefinition = " varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地区'")
    private String areacity;
    @Column(name = "year",columnDefinition = " int(10) DEFAULT NULL COMMENT '年份'")
    private Integer year;
    @Column(name = "score",columnDefinition = " decimal(3,2) DEFAULT NULL COMMENT '评分'")
    private Double score;
    @Column(name = "favorit_cnt",columnDefinition = " int(20) DEFAULT '0' COMMENT '被喜欢的数量'")
    private Integer favoriteCnt;
    @Column(name = "dot_cnt",columnDefinition = " int(20) DEFAULT '0' COMMENT '点赞数量'")
    private Integer dotCnt;
    @Column(name = "video_source_list",columnDefinition = " text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电影播放地址 key --> 地址描述  value-->视频地址'")
    private String videoSourceList;
    @Column(name = "create_time",columnDefinition = " datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '采集日期'")
    private Date createTime;
    @Column(name = "sort",columnDefinition = " int(10) DEFAULT '0' COMMENT '排序'")
    private Integer sort;
    @Column(name = "hot",columnDefinition = " bit(1) DEFAULT b'0' COMMENT '最热'")
    private Integer hot;
    @Column(name = "tags",columnDefinition = " varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签'")
    private String tags;
    @Column(name = "cate_name",columnDefinition = " varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类目名称'")
    private String cateName;

}
