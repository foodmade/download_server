package com.video.download.common;

import com.alibaba.fastjson.JSON;
import com.video.download.common.exception.BeanUtilsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BeanUtils {

    private BeanUtils(){}

    /**
     * Transforms from the source object. (copy same properties only)
     *
     * @param source      source data
     * @param targetClass target class must not be null
     * @param <T>         target class type
     * @return instance with specified type copying from source data; or null if source data is null
     * @throws BeanUtilsException if newing target instance failed or copying failed
     */
    @Nullable
    public static <T> T transformFrom(@Nullable Object source, @NonNull Class<T> targetClass) {
        Assert.notNull(targetClass, "Target class must not be null");

        if (source == null) {
            return null;
        }

        // Init the instance
        try {
            // New instance for the target class
            T targetInstance = targetClass.newInstance();
            // Copy properties
            org.springframework.beans.BeanUtils.copyProperties(source, targetInstance, getNullPropertyNames(source));
            // Return the target instance
            return targetInstance;
        } catch (Exception e) {
            log.error("Failed to new " + targetClass.getName() + " instance or copy properties");
        }
        return null;
    }

    /**
     * Update properties (non null).
     *
     * @param source source data must not be null
     * @param target target data must not be null
     * @throws BeanUtilsException if copying failed
     */
    public static void updateProperties(@NonNull Object source, @NonNull Object target) {
        Assert.notNull(source, "source object must not be null");
        Assert.notNull(target, "target object must not be null");

        // Set non null properties from source properties to target properties
        try {
            org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        } catch (BeansException e) {
            log.error("Failed to copy properties");
        }
    }

    /**
     * Gets null names array of property.
     *
     * @param source object data must not be null
     * @return null name array of property
     */
    @NonNull
    private static String[] getNullPropertyNames(@NonNull Object source) {
        return getNullPropertyNameSet(source).toArray(new String[0]);
    }

    /**
     * Gets null names set of property.
     *
     * @param source object data must not be null
     * @return null name set of property
     */
    @NonNull
    private static Set<String> getNullPropertyNameSet(@NonNull Object source) {

        Assert.notNull(source, "source object must not be null");
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = beanWrapper.getPropertyValue(propertyName);

            // if property value is equal to null, add it to empty name set
            if (propertyValue == null) {
                emptyNames.add(propertyName);
            }
        }

        return emptyNames;
    }

    public static Map<String, String> toMap(Object source) {
        if (source == null) {
            return null;
        }
        Map<String, String> sourceMap = new HashMap<>();
        try {
           org.apache.commons.beanutils.BeanUtils.populate(source,sourceMap);
        } catch (Exception e) {
            log.error("transMap2Bean2 Error ",e);
        }
        return sourceMap;
    }

    /**
     * Quick assignment bean
     */
    public static <T> T buildDefaultAttrModel(String[] fields, Object[] values, Class<T> cls) throws Exception {
        Assert.isTrue((fields != null && values != null),"Build default example failed. Because fields or values all must not be null.");
        Assert.isTrue(fields.length == values.length , "Build default example failed. Because fields's length not equal values's length.");
        Assert.notNull(cls,"Build default example failed. Target source class must not be null.");

        T entity = cls.newInstance();
        Field[] declaredFields = cls.getDeclaredFields();

        for (Field declaredField : declaredFields) {

            if(!Arrays.asList(fields).contains(declaredField.getName())){
                continue;
            }
            ReflectionUtils.invokeSetterMethod(entity,declaredField.getName(),values[findIndex(fields,declaredField.getName())],declaredField.getType());
        }
        return entity;
    }

    /**
     * 将集合转换为map,Key为指定的field字段值,value为V对象
     * @param sources  源集合
     * @param field    字段
     * @param cls      指定转换类型
     */
    public static <T,V> Map<T,V> objectTransKeyMap(@NonNull Collection<V> sources, @NonNull String field, Class<T> cls){
        Assert.notNull(sources,"Target source must not be null.");
        Assert.isTrue(sources.size() != 0,"Target source at least one object.");

        Map<T,V> resMap = new HashMap<>();
        for (V source : sources) {
            Field[] fields = source.getClass().getDeclaredFields();
            for (Field field1 : fields) {
                if(field1.getName().equals(field)){
                    Object fieldVal = ReflectionUtils.getFieldValue(source,field);
                    resMap.put((T)ReflectionUtils.parserAttrType(cls,fieldVal),source);
                    break;
                }
            }
        }
        return resMap;
    }

    public static <T> List<T> coventObject(Collection<?> collection, Class<T> clazz){
        return collection.stream().map(oldObject -> {
            T instance;
            try{
                instance = clazz.newInstance();
                org.springframework.beans.BeanUtils.copyProperties(oldObject, instance);
                return instance;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    };

    private static int findIndex(String[] arrays, String str){
        for (int i = 0; i < arrays.length; i++) {
            if(arrays[i].equals(str)){
                return i;
            }
        }
        return -1;
    }
}
