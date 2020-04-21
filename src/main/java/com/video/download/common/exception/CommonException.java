package com.video.download.common.exception;

import com.video.download.common.enums.ExceptionEnum;

/**
 * @Author xiaom
 * @Date 2020/3/24 15:28
 * @Version 1.0.0
 * @Description <>
 **/
public class CommonException extends CException {

    public CommonException(String message) {
        super(message);
    }

    public CommonException(ExceptionEnum exceptionEnum){
        super(exceptionEnum.getMessage());
        this.setCode(exceptionEnum.getCode());
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

}
