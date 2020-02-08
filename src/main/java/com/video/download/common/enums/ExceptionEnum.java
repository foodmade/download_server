package com.video.download.common.enums;

public enum ExceptionEnum {
    SERVER_ERR("500","服务器内部错误"),
    TASKTYPE_ERR("10001","任务类型异常"),
    SUCCESS("200","成功"),
    UNKNOWNERR("100101","未知错误");

    private String code;

    private String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
