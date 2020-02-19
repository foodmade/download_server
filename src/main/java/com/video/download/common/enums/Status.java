
package com.video.download.common.enums;

import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 记录状态枚举类。
 *
 * @author admin123.Z at 2015/12/28 22:20
 */
public enum Status {

    NORMAL(0, "正常"),
    COMPLETE(1, "已完成"),
    STOPPED(-1, "停用的"),
    DELETION(-2, "删除的");

    private int type;
    private String description;

    Status(int type, String description) {
        this.type = type;
        this.description = description;
    }

    /**
     * {@link #type}的getter方法。
     */
    public int getType() {
        return type;
    }

    /**
     * {@link #description}的getter方法。
     */
    public String getDescription() {
        return description;
    }

}
