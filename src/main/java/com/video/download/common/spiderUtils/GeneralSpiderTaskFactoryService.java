package com.video.download.common.spiderUtils;
import com.video.download.common.CommonUtils;
import com.video.download.common.enums.SpiderTypeEnum;
import com.video.download.core.maker.AbstractGeneraSpiderTaskMaker;
import com.video.download.dao.IParserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;

@Service
public class GeneralSpiderTaskFactoryService {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private IParserService iParserService;

    private static Logger logger = Logger.getLogger(GeneralSpiderTaskFactoryService.class);

    public AbstractGeneraSpiderTaskMaker getTaskMaker(String taskType) throws IllegalAccessException, InstantiationException {
        SpiderTypeEnum taskEnum = SpiderTypeEnum.find(taskType);
        if(taskEnum == null){
            logger.error("【异常的爬虫任务类型:{"+taskType+"}】");
            return null;
        }
        Class<? extends AbstractGeneraSpiderTaskMaker> cls = taskEnum.getSpiderCls();
        AbstractGeneraSpiderTaskMaker taskMaker = cls.newInstance();
        taskMaker.setServletContext(servletContext);
        taskMaker.setCommonUtils(commonUtils);
        taskMaker.setTypeEnum(SpiderTypeEnum.find(taskType));
        taskMaker.setParserService(iParserService);
        return taskMaker;
    }
}
