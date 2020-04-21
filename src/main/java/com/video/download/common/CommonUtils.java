package com.video.download.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xiaom
 * @Date 2020/1/10 9:37
 * @Version 1.0.0
 * @Description <>
 **/
@Service
public class CommonUtils {

    /**
     * MD5加密
     * @param str  原始字符串
     * @return 加密字符串
     */
    public static String MD5(String str){
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析除?号以后的url地址
     * @param url  原始url
     * @return 地址
     */
    public static String originalUrl(String url){
        Pattern pattern = Pattern.compile("^(http[s]?:\\/\\/m3u8\\.cnkamax\\.com\\/useruploadfiles\\/)+.*?(\\.m3u8)");
        Matcher matcher= pattern.matcher(url);
        while(matcher.find()){
            return matcher.group();
        }
        return null;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;

    }
    public static <T> T mapTransModel(HashMap map, Class<T> cls){
        if(map == null || map.isEmpty()){
            return null;
        }
        //获取类加载器
        T model = null;
        try {
            model = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if(model == null){
            return null;
        }
        Method[] methods = model.getClass().getMethods();
        if(methods == null || methods.length == 0){
            return null;
        }
        String key;
        Object value;
        for (Method m:methods){
            key = m.getName().toLowerCase();
            if(!key.contains("set")){
                continue;
            }
            key = key.replace("set","");
            if(CommonUtils.isEmpty(key)){
                continue;
            }
            value = map.get(key);
            if(value == null){
                continue;
            }
            //如果参数是集合类型,则需要转换为相应类型
            value = specialHandle(m.getParameterTypes()[0].getSimpleName(),value);
            try {
                m.invoke(model,value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    private static Object specialHandle(String simpleName,Object value) {
        switch (simpleName){
            case "HashMap":
                return JSON.parseObject(value.toString(),new TypeReference<HashMap>(){});
            case "List":
                return JSON.parseObject(value.toString(),new TypeReference<List>(){});
            case "Integer":
                return Integer.parseInt(value.toString());
            case "String":
                return value.toString();
            case "Double":
                return Double.parseDouble(value + "");
            default:
                return value;
        }
    }

    /**
     * Check if the object is empty (If the string is 'null' and 'undefined')
     * @param arg  object
     * @param <T>  T
     * @return is empty?
     */
    public static <T> boolean isEmpty(T arg) {
        try {
            return Optional.of(arg)
                    .map(Object::toString)
                    .map(String::trim)
                    .map(c -> c.equals("null"))
                    .map(b -> b || arg.equals(""))
                    .map(a -> a || arg.equals("undefined"))
                    .orElse(false);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 获取随机数
     */
    public static String getUuid() {
        return UUID.randomUUID().toString();
    }

    public static String readRequestBuff(HttpServletRequest request){
        StringBuilder submitData = new StringBuilder();
        try {
            while (true) {
                String str = request.getReader().readLine();
                if (str == null)
                    break;
                submitData.append(str);
            }
        } catch (IOException e) {
            return null;
        }
        return submitData.toString();
    }
}
