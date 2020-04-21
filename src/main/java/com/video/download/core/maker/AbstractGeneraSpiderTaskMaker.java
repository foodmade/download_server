package com.video.download.core.maker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.video.download.common.CommonUtils;
import com.video.download.common.DateUtils;
import com.video.download.common.context.HttpRequestContextHelper;
import com.video.download.common.context.SpringContext;
import com.video.download.common.enums.SaveEnum;
import com.video.download.common.enums.SpiderTypeEnum;
import com.video.download.common.spiderUtils.SpiderTypeConst;
import com.video.download.core.handler.AbsSave;
import com.video.download.core.taskPool.QueryInfo;
import com.video.download.core.taskPool.QueryTaskNode;
import com.video.download.core.taskPool.TaskParams;
import com.video.download.core.taskPool.TaskPool;
import com.video.download.dao.IParserService;
import com.video.download.dao.repository.MoviesRepository;
import com.video.download.domain.TaskData;
import com.video.download.domain.WorkData;
import com.video.download.domain.entity.DynamicParser;
import com.video.download.domain.entity.Movies;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 爬虫处理器抽象类
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractGeneraSpiderTaskMaker {

    private ServletContext servletContext;

    private Properties appProperties;

    private CommonUtils commonUtils;

    private SpiderTypeEnum typeEnum;

    private IParserService parserService;

    public Object getTask(String curTaskType, HttpServletRequest request, String spiderType) {
        Object result;
        switch (curTaskType) {
            case SpiderTypeConst.taskTypeUpdateConfigState:
                result = getUpdateConfigTask();
                log(request, curTaskType);
                return result;
            case SpiderTypeConst.taskTypeUpdateParserState:
                result = getUpdateParserTask();
                log(request, curTaskType);
                return result;
            case SpiderTypeConst.taskTypeCommonState:
                return getCommonTask();
        }
        return null;
    }

    public void commitTask(String curTaskType) {
        if (curTaskType.equals(SpiderTypeConst.taskTypeCommonState)) {
            commitCommonTaskResult();
        }
    }

    private void log(HttpServletRequest request, String type){
        String spider_ip = null;
        if(request != null){
            spider_ip = request.getRemoteAddr();
        }
        if(spider_ip == null){
            return;
        }
        log.info("【日期：{"+ DateUtils.formatDate(new Date(),DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS) +"}爬虫ip:{"+spider_ip+"}  请求获取配置 type:{"+type+"}】");
    }

    private Object getUpdateConfigTask() {
        ArrayList<Object> result = new ArrayList<>();
        HashMap<String, Object> innerMap = new HashMap<>();
        innerMap.put("config", transStringToMap(loadSpiderConfig()));
        HashMap<String, Object> outerMap = getTaskResultTransdMap();
        outerMap.put("taskData", innerMap);
        outerMap.put("type", 3);
        result.add(outerMap);
        return result;
    }

    private String loadSpiderConfig(){
        DynamicParser config = parserService.getConfig(this.typeEnum);
        return config == null ? null : config.getValue();
    }

    private HashMap<String, Object> getTaskResultTransdMap() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("type", 1);
        result.put("info", "");
        result.put("taskData", null);
        return result;
    }

    private Object transStringToMap(String s) {
        return JSON.parseObject(s, HashMap.class);
    }

    private Object getUpdateParserTask() {
        ArrayList<HashMap<String, Object>> parsers = loadSpiderParser();
        return transToParserString(parsers);
    }

    private Object transToParserString(ArrayList<HashMap<String, Object>> parsers) {
        HashMap<String, Object> result = getTaskResultTransdMap();
        result.put("type", 2);
        HashMap<String, Object> parserMapItem = new HashMap<String, Object>();
        parserMapItem.put("parsers", parsers);
        result.put("taskData", parserMapItem);
        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(result);
        return arrayList;
    }

    private ArrayList<HashMap<String, Object>> loadSpiderParser(){

        ArrayList<HashMap<String, Object>> parsers = new ArrayList<>();
        List<DynamicParser> parserList = parserService.getParsers(this.typeEnum);
        if(parserList == null){
            return parsers;
        }

        parserList.forEach(parser -> {
            HashMap<String, Object> tmp = new HashMap<String, Object>();
            tmp.put("parserCode", parser.getValue());
            parsers.add(tmp);
        });

        return parsers;
    }


    protected WorkData parserTaskData(HashMap<String, Object> commitMap){
        WorkData workData = new WorkData();
        if(commitMap == null || commitMap.isEmpty()){
            return workData;
        }
        workData.setRetcode(SpiderTypeConst._FAIL_CODE);
        Object task = commitMap.get("task");
        if(task == null){
            return workData;
        }
        HashMap<String, Object> taskOptions = JSON.parseObject(task+"",new TypeReference<HashMap<String, Object>>(){});
        if(taskOptions == null || taskOptions.isEmpty()){
            return workData;
        }
        task = taskOptions.get("task");
        if(task == null){
            return workData;
        }
        HashMap<String, Object> taskMap = JSON.parseObject(task+"",new TypeReference<HashMap<String, Object>>(){});
        TaskData taskData = CommonUtils.mapTransModel(taskMap,TaskData.class);
        workData.setTaskData(taskData);
        //解析爬虫提交内容
        HashMap<String, Object> itemMap = JSON.parseObject(commitMap.get("result")+"",new TypeReference<HashMap<String, Object>>(){});
        if(itemMap == null || itemMap.isEmpty()){
            return workData;
        }
        int retcode = 0;
        try {
            retcode = Integer.parseInt(itemMap.get("retcode")+"");
        } catch (NumberFormatException e) {
            log.error("解析retcode异常 retMap:"+ JSON.toJSONString(itemMap));
        }
        workData.setRetcode(retcode);
        if(retcode == SpiderTypeConst._FAIL_CODE){
            return workData;
        }
        JSONArray jsonArray = JSON.parseArray(itemMap.get("rows")+"");
        if(jsonArray == null || jsonArray.isEmpty()){
            return workData;
        }
        List<Movies> items = getItems(jsonArray);
        workData.setItems(items);
        return workData;
    }

    private List<Movies> getItems(JSONArray jsonArray) {
        List<Movies> result = new ArrayList<>();
        if(jsonArray.size() == 0){
            return result;
        }
        HashMap itemMap;
        for (Object itemObj :jsonArray){
            itemMap = JSON.parseObject(itemObj+"",new TypeReference<HashMap>(){});
            if(itemMap == null){
                continue;
            }
            result.add(CommonUtils.mapTransModel(itemMap,Movies.class));
        }
        return result;
    }

    private void checkTaskIsFinish(WorkData workData) {
        String query_id = workData.getTaskData().getQuery_id();
        QueryTaskNode queryTaskNode = TaskPool.getQueryTaskNode(query_id);
        if(queryTaskNode == null){
            log.error("检查任务是否完成时,queryNode为空");
            return;
        }
        QueryInfo queryInfo = TaskPool.getQueryInfo(query_id,workData.getTaskData().getTask_type());
        if(queryInfo == null  || queryTaskNode.getFinish()){
            log.info("任务已完成.........");
            return;
        }
        if(queryInfo.getUnOverTaskCnt() == 0){
            log.info("任务已完成 query_id:"+query_id);
            //说明任务已经完成 标志当前任务已完成 防止其他线程进行重复操作
            queryTaskNode.setFinish(true);
            //清理缓存数据
            AbsSave.cacheVideoMap.remove(query_id);
//            //开始保存缓存数据
//            log.debug("最终缓存数据:"+ JSON.toJSONString(AbsSave.cacheVideoMap.get(query_id)));
//            try {
//                saveData(workData);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            TaskPool.closeQueryNode(workData.getTaskData().getTask_type(),query_id);
        }
    }

    protected Object getCommonTask(){
        return null;
    }

    protected void commitCommonTaskResult(){
        String commitData = CommonUtils.readRequestBuff(HttpRequestContextHelper.getContextRequest());
        log.info("提交内容:"+ commitData);

        HashMap<String, Object> commitMap = JSON.parseObject(commitData,new TypeReference<HashMap<String, Object>>(){});
        if(commitMap == null || commitMap.keySet().size() == 0){
            log.error("解析提交文本失败");
            return;
        }
        WorkData workData = parserTaskData(commitMap);
        log.info("workData:"+ JSON.toJSONString(workData));
        if(workData.getRetcode().equals(SpiderTypeConst._FAIL_CODE)){
            log.info("爬虫提交异常 retcode:"+workData.getRetcode()+" commitStr:"+commitData);
            TaskPool.closeTask(workData.getTaskData().getTask_type(),workData.getTaskData().getTask_id(),workData.getTaskData().getQuery_id());
            return;
        }
        try {
            //获取当前maker对应的保存实例
            SaveEnum saveEnum = SaveEnum.find(workData.getTaskData().getTask_type());
            if(saveEnum == null){
                log.error("未获取到task_type:"+workData.getTaskData().getTask_type()+" 对应的保存类,请声明定义");
                return;
            }
            //缓存数据至内存
            doCacheData(saveEnum,workData);
            //创建详情页任务
            doCreateDetailTask(workData);
        } catch (Exception e) {
            log.error("处理【GVideo】提交任务出现异常 e:"+e.getMessage());
            e.printStackTrace();
        } finally {
            TaskPool.closeTask(workData.getTaskData().getTask_type(),workData.getTaskData().getTask_id(),workData.getTaskData().getQuery_id());
        }
        //检查任务是否全部完成
        checkTaskIsFinish(workData);
    }

    private void doCacheData(SaveEnum saveEnum,WorkData workData) {
        if(saveEnum == null){
            return;
        }
        Class<? extends AbsSave> cls = saveEnum.getCls();
        try {
            switch (workData.getTaskData().getStage()){
                case 0:
                    cls.newInstance().afterStageOneDataCache(workData);
                    return;
                case 1:
                    cls.newInstance().afterStageTwoDataCache(workData);
            }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void doCreateDetailTask(WorkData workData) {
        if(checkPageIsOver(workData.getTaskData())){
            log.debug("重复的提交,舍弃创建第二阶段任务");
            return;
        }
        List<TaskParams> taskParams = getTaskParams(workData);
        if(taskParams == null){
            return;
        }
//        TaskData taskData = workData.getTaskData();
        QueryTaskNode queryTaskNode = TaskPool.getQueryTaskNode(workData.getTaskData().getQuery_id());
        for (TaskParams taskParam:taskParams){
            TaskPool.appendSubTaskToNode(queryTaskNode,taskParam);
        }
        log.info("创建第二阶段任务成功 page:"+workData.getTaskData().getPage()+ " cateId:"+workData.getTaskData().getCateid()+" taskData:"+ JSON.toJSONString(workData.getTaskData()));
    }

    private boolean checkPageIsOver(TaskData taskData) {
        QueryTaskNode queryTaskNode = TaskPool.getQueryTaskNode(taskData.getQuery_id());
        if(queryTaskNode == null){
            return true;
        }
        return queryTaskNode.getOverPageSet().contains(taskData.getPage());
    }

    private List<TaskParams> getTaskParams(WorkData workData) {
        List<Movies> items = workData.getItems();
        if(items == null || items.isEmpty()){
            return null;
        }
        List<TaskParams> result = new ArrayList<>();
        TaskData taskData = workData.getTaskData();
        HashMap<String, String> childMap = taskData.getChildurl();
        Integer stage = taskData.getStage()+1;
        if(stage > taskData.getTotalstage()){
            log.debug("已到达第【"+stage+"】阶段,最大阶段:【"+taskData.getTotalstage()+"】,无法创建任务");
            return null;
        }
        QueryTaskNode queryTaskNode = TaskPool.getQueryTaskNode(taskData.getQuery_id());
        if(queryTaskNode == null){
            return null;
        }
        queryTaskNode.getOverPageSet().add(taskData.getPage());
        for (Movies item : items) {
            TaskParams taskParams = new TaskParams();
            taskParams.setStatus(1);
            taskParams.setDesc(workData.getTaskData().getDesc());
            taskParams.setUrl(childMap.get(stage.toString()));
            taskParams.setStage(stage);
            taskParams.setChildUrl(taskData.getChildurl());
            taskParams.setTaskType(taskData.getTask_type());
            taskParams.setMaxPage(1);
            taskParams.setVideoId(item.getVideoId());
            taskParams.setChildCateInfo(taskData.getChildCateInfo());
            result.add(taskParams);
        }
        return result;
    }

    protected void saveData(WorkData workData){
        String query_id = workData.getTaskData().getQuery_id();
        if(CommonUtils.isEmpty(query_id)){
            return;
        }
        HashMap<String,Movies> cacheItems = AbsSave.cacheVideoMap.get(query_id);
        if(cacheItems == null || cacheItems.size() == 0){
            return;
        }
        List<Movies> data = new ArrayList<>(cacheItems.values());

        long start = System.currentTimeMillis();

        SpringContext.getBean(MoviesRepository.class).saveAll(data);
        log.info("【保存数据完毕 耗时】:"+(System.currentTimeMillis()-start));
        log.info("【资源更新完毕：】taskType:{"+workData.getTaskData().getTask_type()+"} size:{"+data.size()+"}");
    }
}
