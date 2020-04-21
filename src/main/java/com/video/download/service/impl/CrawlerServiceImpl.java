package com.video.download.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.video.download.common.BeanUtils;
import com.video.download.common.context.HttpRequestContextHelper;
import com.video.download.common.enums.ExceptionEnum;
import com.video.download.common.enums.SpiderStatusEnum;
import com.video.download.common.enums.SpiderTypeEnum;
import com.video.download.common.exception.CommonException;
import com.video.download.common.spiderUtils.GeneralSpiderTaskFactoryService;
import com.video.download.common.spiderUtils.SpiderInfoObj;
import com.video.download.common.spiderUtils.SpiderTypeConst;
import com.video.download.core.taskPool.TaskParams;
import com.video.download.core.taskPool.TaskPool;
import com.video.download.dao.repository.CrawlerTaskRepository;
import com.video.download.dao.repository.SpiderControlRepository;
import com.video.download.domain.entity.CrawlerTask;
import com.video.download.domain.entity.SpiderControl;
import com.video.download.service.CrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author xiaom
 * @Date 2020/3/24 10:45
 * @Version 1.0.0
 * @Description <>
 **/
@Service
@Slf4j
public class CrawlerServiceImpl implements CrawlerService {

    public static ConcurrentHashMap<String, SpiderInfoObj> spiderHostMap  = new ConcurrentHashMap<>();

    @Resource
    private SpiderControlRepository spiderControlRepository;
    @Resource
    private CrawlerTaskRepository crawlerTaskRepository;

    private final GeneralSpiderTaskFactoryService generalSpiderTaskFactoryService;

    public CrawlerServiceImpl(GeneralSpiderTaskFactoryService generalSpiderTaskFactoryService) {
        this.generalSpiderTaskFactoryService = generalSpiderTaskFactoryService;
    }

    @Override
    public Object dispatchCrawlerTask(SpiderTypeEnum typeEnum) {
        return dispatchCrawlerTaskResult(typeEnum.getSpiderType());
    }

    @Override
    public void dispatchCommitTaskResult(SpiderTypeEnum typeEnum) {
        String hostAddr = HttpRequestContextHelper.getRemoteHost();
        SpiderInfoObj spiderInfoObj = getSpiderInfoObj(hostAddr, typeEnum.getSpiderType());
        if (spiderInfoObj == null) {
            return;
        }
        doCommitTaskResult(spiderInfoObj);
    }

    @Override
    public boolean restoreSpiderStatus() {
        if(spiderHostMap.isEmpty()){
            throw new CommonException("Not found spider handler.");
        }
        spiderHostMap.values().forEach(info -> info.setCurTaskType(SpiderTypeConst.taskTypeUpdateConfigState));
        log.info("初始化爬虫状态成功,爬虫数量:[{}]",spiderHostMap.size());
        return true;
    }

    @Override
    public void generateTask() {
        //获取任务
        List<CrawlerTask> crawlerTask = crawlerTaskRepository.findByStatus(SpiderStatusEnum.ACTIVATION.getStatus());
        if(crawlerTask == null || crawlerTask.isEmpty()){
            throw new CommonException("数据库中不存在激活任务");
        }
        createTaskToPool(crawlerTask);

    }

    private void createTaskToPool(List<CrawlerTask> allTasks) {
        long start = System.currentTimeMillis();
        List<TaskParams> taskParams = getTaskParams(allTasks);

        if(taskParams == null || taskParams.size() == 0){
            throw new CommonException(ExceptionEnum.SERVER_ERR);
        }
        for(TaskParams params : taskParams){
            TaskPool.addSubTask(params);
    }
        log.info("任务创建完毕,任务数量:"+taskParams.size()+" 耗时:"+(System.currentTimeMillis()-start));
    }

    private List<TaskParams> getTaskParams(List<CrawlerTask> allTasks) {

        if(allTasks == null || allTasks.size() == 0){
            return null;
        }
        List<TaskParams> result = new ArrayList<>();
        for (CrawlerTask task:allTasks){
            TaskParams params = BeanUtils.transformFrom(task,TaskParams.class);
            if(params == null){
                continue;
            }
            params.setChildCateInfo(JSON.parseObject(task.getChildCateInfo(),new TypeReference<HashMap<String, String>>(){}));
            params.setChildUrl(JSON.parseObject(task.getChildUrl(),new TypeReference<HashMap<String,String>>(){}));
            result.add(params);
        }
        return result;
    }

    private void doCommitTaskResult(SpiderInfoObj spiderInfoObj) {
        spiderInfoObj.commitTaskResult();
    }

    private Object dispatchCrawlerTaskResult(String spiderType){
        //获取爬虫ip,根据上下文实例判断爬虫请求类型 (更新parser或者获取任务)
        String ip = HttpRequestContextHelper.getRemoteHost();
        SpiderInfoObj spiderInfoObj = getSpiderInfoObj(ip,spiderType);
        log.debug("【获取到信息为 ip:{"+ip+"} 任务类型为:{"+spiderType+"} 的爬虫构造器】");
        if(spiderInfoObj == null){
            return null;
        }
        return getGeneralTask(spiderInfoObj);
    }

    private Object getGeneralTask(SpiderInfoObj spiderInfoObj) {
        Object result = null;
        try {
            result = spiderInfoObj.getTask();
        } catch (Exception e) {
            log.error("【获取任务失败:】"+e.getMessage());
        }
        //切换爬虫状态
        if(result != null){
            curTaskTypeChange(spiderInfoObj);
        }
        return result;
    }

    private void curTaskTypeChange(SpiderInfoObj spiderInfoObj) {
        /**
         * 服务器端进行爬虫控制 第一次请求返回配置文件 第二次请求获取解析器 之后返回执行任务
         */
        if (spiderInfoObj.getCurTaskType().equals(SpiderTypeConst.taskTypeUpdateConfigState)) {
            spiderInfoObj.setCurTaskType(SpiderTypeConst.taskTypeUpdateParserState);
            log.info("【爬虫ip:{"+spiderInfoObj.getSpiderAddr()+"} 执行更新config操作】");
        } else if (spiderInfoObj.getCurTaskType().equals(SpiderTypeConst.taskTypeUpdateParserState)) {
            spiderInfoObj.setCurTaskType(SpiderTypeConst.taskTypeCommonState);
            log.info("【爬虫ip:{"+spiderInfoObj.getSpiderAddr()+"} 执行更新parser操作】");
        }
    }

    private SpiderInfoObj getSpiderInfoObj(String spider_ip,String task_type){
        if(spiderHostMap.isEmpty()){
            initSpiderHostMap();
        }
        SpiderInfoObj result = spiderHostMap.get(spider_ip + "_" + task_type);
        if (result == null) {
            log.info("【没有获取到合法爬虫  ip:{}  spider_type:{}】",spider_ip,task_type);
            return null;
        }
        return result;
    }

    /**
     * 初始化爬虫白名单
     */
    public synchronized void initSpiderHostMap(){
        Iterable<SpiderControl> allSpiderList = spiderControlRepository.findAll();
        if(allSpiderList == null){
            log.warn("【未获取到爬虫白名单相关配置】");
            return;
        }
        for (SpiderControl spiderControl:allSpiderList){
            spiderHostMap.put(spiderControl.getSpiderIp()+"_"+spiderControl.getSpiderType(),transToSpiderInfoObj(spiderControl));
        }
        log.info("【初始化爬虫白名单成功】");
    }

    /**
     * 初始化爬虫状态
     */
    private SpiderInfoObj transToSpiderInfoObj(SpiderControl spiderControl) {
        SpiderInfoObj spiderInfoObj = new SpiderInfoObj();
        spiderInfoObj.setSpiderAddr(spiderControl.getSpiderIp());
        spiderInfoObj.setSpiderType(spiderControl.getSpiderType());
        spiderInfoObj.setCurTaskType(SpiderTypeConst.taskTypeUpdateConfigState);
        spiderInfoObj.setTaskMakerService(generalSpiderTaskFactoryService);
        return spiderInfoObj;
    }
}
