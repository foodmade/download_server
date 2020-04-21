package com.video.download.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author xiaom
 * @Date 2020/3/24 11:43
 * @Version 1.0.0
 * @Description <>
 **/
@NoArgsConstructor
@Data
@Entity
@Table(name = "dynamic_parser")
public class DynamicParser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "config_type",columnDefinition = " varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置类型 (parser)爬虫解析器  (config)爬虫配置'")
    private String configType;

    @Column(name = "value",columnDefinition = " text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置参数'")
    private String value;

    @Column(name = "spider_type",columnDefinition = " varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '爬虫类型'")
    private String spiderType;

    @Column(name = "update_time",columnDefinition = " datetime DEFAULT NULL COMMENT '更新时间'")
    private Date updateTime;
}
