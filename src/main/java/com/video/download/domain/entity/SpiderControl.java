package com.video.download.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author xiaom
 * @Date 2020/3/24 11:20
 * @Version 1.0.0
 * @Description <>
 **/
@NoArgsConstructor
@Data
@Entity
@Table(name = "spider_control")
public class SpiderControl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 爬虫类型
     */
    private String spiderType;

    /**
     * 爬虫ip
     */
    private String spiderIp;


}
