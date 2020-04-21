package com.video.download.common.exception;

import com.video.download.common.enums.ExceptionEnum;
import lombok.Data;

/**
 * @Author xiaom
 * @Date 2020/3/24 15:26
 * @Version 1.0.0
 * @Description <>
 **/
@Data
public abstract class CException extends RuntimeException {

    private ExceptionEnum exceptionEnum = ExceptionEnum.UNKNOWNERR;

    private String code = exceptionEnum.getCode();

    private String message = exceptionEnum.getMessage();

    public CException(String message) {
        this.message = message;
    }

    public CException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CException(String message, Throwable cause) {
        super(message, cause);
    }

    public CException() {
    }

}
