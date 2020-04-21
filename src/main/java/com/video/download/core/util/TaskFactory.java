package com.video.download.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.video.download.core.taskPool.TaskData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author xiaom
 * @Date 2020/3/24 14:40
 * @Version 1.0.0
 * @Description <>
 **/
public class TaskFactory {

    private static final String _cateId = "cateId";
    private static final String _pageId = "pageId";
    private static final String _sourceId = "sourceId";
    private static final String _dirId = "dirId";


    /**
     * 博天堂资源网 首页任务
     */
    public static List<Object> getBttTaskOptions(TaskData taskData) {
        List<Object> result = new ArrayList<>();
        String url = taskData.getUrl().replace(_cateId,taskData.getSourcecateid()+"");
        url = url.replace(_pageId,taskData.getPage()+"");
        String taskJsonStr = "{type:1, info:'', \n" +
                "            taskData:{\n" +
                "                crawlingOptions:{\n" +
                "                    url:'"+url+"',\n" +
                "                    method: 'GET',\n" +
                "                    gzip:true,\n" +
                "                    headers:{\n" +
                "                        'Host': 'btt904.com',\n" +
                "                    },\n" +
                "                    agent:false\n" +
                "                }\n" +
                "            }\n" +
                "        }";
        HashMap<String,Object> resultMap = JSON.parseObject(taskJsonStr,new TypeReference<HashMap<String, Object>>(){});
        resultMap.put("task",taskData);

        result.add(resultMap);
        return result;

    }

    /**
     * 获取博天堂 详情页任务
     */
    public static List<Object> getBttDetailOptions(TaskData taskData) {

        List<Object> result = new ArrayList<>();
        String url = taskData.getUrl().replace(_sourceId,taskData.getVideoid());
        String taskJsonStr = "{type:1, info:'', \n" +
                "            taskData:{\n" +
                "                crawlingOptions:{\n" +
                "                    url:'"+url+"',\n" +
                "                    method: 'GET',\n" +
                "                    gzip:true,\n" +
                "                    headers:{\n" +
                "                        'Host': 'btt904.com',\n" +
                "                    },\n" +
                "                    agent:false\n" +
                "                }\n" +
                "            }\n" +
                "        }";
        HashMap<String,Object> resultMap = JSON.parseObject(taskJsonStr,new TypeReference<HashMap<String, Object>>(){});
        resultMap.put("task",taskData);
        result.add(resultMap);
        return result;
    }

}
