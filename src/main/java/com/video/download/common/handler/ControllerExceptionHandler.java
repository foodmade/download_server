package com.video.download.common.handler;

import com.video.download.common.domain.BaseResult;
import com.video.download.common.exception.CException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author xiaom
 * @Date 2020/3/24 15:23
 * @Version 1.0.0
 * @Description <>
 **/
@RestControllerAdvice({"com.video.download.control"})
@Slf4j
public class ControllerExceptionHandler {

    /**
     * Global exception interceptor. Write to response. Just for OWCException .
     * @param e Exception example
     * @return error data
     */
    @ExceptionHandler({CException.class})
    public ResponseEntity<BaseResult> handleHaloException(CException e) {
        BaseResult baseResponse = handleBaseException(e);
        baseResponse.setCode(e.getCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    private  BaseResult handleBaseException(Throwable t) {
        Assert.notNull(t, "Throwable must not be null");

        log.error("Captured an exception throw message", t);

        BaseResult baseResponse = new BaseResult();
        baseResponse.setMessage(t.getMessage());

        return baseResponse;
    }

}
