package com.video.download.common.enums;


import com.video.download.common.spiderUtils.SpiderTypeConst;
import com.video.download.core.handler.AbsSave;
import com.video.download.core.handler.DefaultAfterHandler;

public enum SaveEnum {
    DEFAULT(SpiderTypeConst.DEFAULT_TYPE, DefaultAfterHandler.class);

    SaveEnum(String task_type, Class<? extends AbsSave> cls) {
        this.task_type = task_type;
        this.cls = cls;
    }

    private String task_type;

    private Class<? extends AbsSave> cls;

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public Class<? extends AbsSave> getCls() {
        return cls;
    }

    public void setCls(Class<? extends AbsSave> cls) {
        this.cls = cls;
    }

    public static SaveEnum find(String task_type){
        SaveEnum[] list = SaveEnum.values();
        for (SaveEnum saveEnum:list){
            if(saveEnum.getTask_type().equals(task_type)){
                return saveEnum;
            }
        }
        return DEFAULT;
    }
}
