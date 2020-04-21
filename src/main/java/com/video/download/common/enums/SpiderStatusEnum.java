package com.video.download.common.enums;

import lombok.Data;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @Author xiaom
 * @Date 2020/3/24 15:34
 * @Version 1.0.0
 * @Description <>
 **/
public enum SpiderStatusEnum {
    STOP(1,"停止"),
    ACTIVATION(1,"激活");

    SpiderStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    @Getter
    private Integer status;
    @Getter
    private String desc;

    public static SpiderStatusEnum match(Integer type){
        return Stream.of(SpiderStatusEnum.values())
                .filter(typeEnum -> typeEnum.getStatus().equals(type))
                .findFirst()
                .orElse(null);
    }
}
