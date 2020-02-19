package com.video.download.domain.entity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author xiaom
 * @Date 2020/2/14 17:39
 * @Version 1.0.0
 * @Description <>
 **/
@Entity
@Data
@Table(name = "u_user")
public class UUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cyId;

    /**
     * 手机号
     */
    private String boundCellphone;

    /**
     * 登录名
     */
    private String loginUsername;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String portrait;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 用户类型
     */
    private Integer type;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 推荐人
     */
    private String spreadCode;

    /**
     * 签名
     */
    private String signature;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 查看记录数
     */
    private Long viewCount;

    /**
     * 点赞记录数
     */
    private Long likeCount;

    /**
     * 收藏记录数
     */
    private Long collectCount;

    /**
     * 描述
     */
    private String description;

    /**
     * 性别 0男 1女 2 保密
     */
    private Integer sex;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 推广码
     */
    private String invitationCode;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 城市
     */
    private String city;

    /**
     * mqttClientId
     */
    private String mqttClientId;

    /**
     * 关注数量
     */
    private Integer followAmount;

    /**
     * 粉丝数量
     */
    private Integer fansAmount;

    /**
     * 背景图
     */
    private String backgroundPic;

    /**
     * 相框
     */
    private String userFrame;

}
