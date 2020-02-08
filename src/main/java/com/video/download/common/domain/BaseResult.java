package com.video.download.common.domain;

import com.video.download.common.enums.ExceptionEnum;

public class BaseResult {
    private String code = "";

    private String message = "";

    private Object responseBody = null;

    private Integer page;

    private Integer pageSize;

    private long total = 0;

    public BaseResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResult(String code, String message, Object responseBody) {

        this.code = code;
        this.message = message;
        this.responseBody = responseBody;
    }

    public BaseResult(ExceptionEnum exceptionEnum){
        this.message = exceptionEnum.getMessage();
        this.code = exceptionEnum.getCode();
    }

    public BaseResult(Object responseBody) {
        this.code = ExceptionEnum.SUCCESS.getCode();
        this.message = ExceptionEnum.SUCCESS.getMessage();
        this.responseBody = responseBody;
    }

    public static BaseResult makeExceptionResult(String message){
        BaseResult result = new BaseResult();
        result.setMessage(message);
        result.setCode(ExceptionEnum.UNKNOWNERR.getCode());
        return result;
    }

    public BaseResult(){}

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isOk(){
        return this.code.equals(ExceptionEnum.SUCCESS.getCode());
    }

    public static BaseResult success(Object responseBody){
        BaseResult result = new BaseResult();
        result.setCode(ExceptionEnum.SUCCESS.getCode());
        result.setMessage(ExceptionEnum.SUCCESS.getMessage());
        result.setResponseBody(responseBody);
        return result;
    }

    public static BaseResult makeExceptionResult(String code, String message){
        BaseResult result = new BaseResult();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static BaseResult makeResult(ExceptionEnum exceptionEnum){
        BaseResult result = new BaseResult();
        result.setCode(exceptionEnum.getCode());
        result.setMessage(exceptionEnum.getMessage());
        return result;
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

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }
}
