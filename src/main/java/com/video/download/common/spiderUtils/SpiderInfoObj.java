package com.video.download.common.spiderUtils;

import com.video.download.common.context.HttpRequestContextHelper;
import com.video.download.core.maker.AbstractGeneraSpiderTaskMaker;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class SpiderInfoObj {

    private String spiderAddr;
    private String spiderType;
    private String curTaskType;

    private GeneralSpiderTaskFactoryService taskMakerService;

    public SpiderInfoObj() {
    }

    public void setTaskMakerService(GeneralSpiderTaskFactoryService taskMakerService) {
        this.taskMakerService = taskMakerService;
    }


    public Object getTask() throws InstantiationException, IllegalAccessException {
        AbstractGeneraSpiderTaskMaker taskMaker = taskMakerService.getTaskMaker(this.getSpiderType());
        try {
            return taskMaker.getTask(getCurTaskType(), HttpRequestContextHelper.getContextRequest(),getSpiderType());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【fetch spider task fail e:】"+e.getMessage());
        }
        return null;
    }

    public void commitTaskResult(){
       try {
           AbstractGeneraSpiderTaskMaker taskMaker = taskMakerService.getTaskMaker(this.getSpiderType());
           taskMaker.commitTask(this.curTaskType);
       }catch (Exception e){
           log.error("【爬虫提交时发生错误 e:】"+e.getMessage());
           e.printStackTrace();
       }
    }
}
