package com.video.download.common.enums;


import com.video.download.common.spiderUtils.SpiderTypeConst;
import com.video.download.core.maker.AbstractGeneraSpiderTaskMaker;
import com.video.download.core.maker.BttTaskMaker;

/**
 * 爬虫类型映射
 */
public enum SpiderTypeEnum {

    BTT(SpiderTypeConst.BTT_TYPE, BttTaskMaker.class,"博天堂资源网 https://btt904.com/")
    ;

    private String spiderType;

    private Class<? extends AbstractGeneraSpiderTaskMaker> spiderCls;

    private String desc;

    SpiderTypeEnum(String spiderType, Class<? extends AbstractGeneraSpiderTaskMaker> spiderCls, String desc) {
        this.spiderType = spiderType;
        this.spiderCls = spiderCls;
        this.desc = desc;
    }

    public String getSpiderType() {
        return spiderType;
    }

    public void setSpiderType(String spiderType) {
        this.spiderType = spiderType;
    }

    public Class<? extends AbstractGeneraSpiderTaskMaker> getSpiderCls() {
        return spiderCls;
    }

    public void setSpiderCls(Class<? extends AbstractGeneraSpiderTaskMaker> spiderCls) {
        this.spiderCls = spiderCls;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static SpiderTypeEnum find(String type){
        SpiderTypeEnum[] list = SpiderTypeEnum.values();
        for (SpiderTypeEnum spiderTypeEnum:list){
            if(spiderTypeEnum.getSpiderType().equals(type)){
                return spiderTypeEnum;
            }
        }
        return null;
    }
}
