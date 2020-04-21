package com.video.download.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author xiaom
 * @Date 2020/3/24 11:54
 * @Version 1.0.0
 * @Description <>
 **/
@NoArgsConstructor
@Data
@Entity
@Table(name = "crawler_task")
public class CrawlerTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url",columnDefinition = " text text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '源网站url (表达式)'")
    private String url;

    @Column(name = "max_page",columnDefinition = " int(10) NOT NULL COMMENT '爬取最大页码'")
    private Integer maxPage;

    @Column(name = "task_type",columnDefinition = " varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务类型'")
    private String taskType;

    @Column(name = "desc",columnDefinition = " varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述'")
    private String desc;

    @Column(name = "status",columnDefinition = " int(2) NOT NULL COMMENT '状态 (1)已激活 (0)未激活 '")
    private Integer status;

    @Column(name = "parent_id",columnDefinition = " int(10) DEFAULT NULL COMMENT '父任务id (已属于顶级填-1)'")
    private Integer parentId;

    @Column(name = "child_url",columnDefinition = " text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '子级任务 (map结构,key(层级数),value(子级任务url))'")
    private String childUrl;

    @Column(name = "child_cate_info",columnDefinition = " text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务类目定义'")
    private String childCateInfo;

}
