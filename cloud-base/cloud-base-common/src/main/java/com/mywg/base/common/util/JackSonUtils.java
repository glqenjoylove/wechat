package com.mywg.base.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Json工具
 *
 * @Author: Xusg
 * @Date: 20/08/11
 * @Email: 18620066064@gdyitop.com
 */
public class JackSonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 日期格式化
     */
    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        // 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        // 忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 大小写脱敏 默认为false  需要改为true
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    /**
     * 对象转Json格式字符串
     *
     * @param obj 对象
     * @return Json格式字符串
     */
    public static <T> String beanToJson(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转Map
     *
     * @param obj 对象
     * @return Json格式字符串
     */
    public static Map<String, Object> beanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return jsonToBean(obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj), Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     *
     * @param obj 对象
     * @return 格式化的Json格式字符串
     */
    public static <T> String beanToJsonPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转换为自定义对象
     *
     * @param str   要转换的字符串
     * @param clazz 自定义对象的class对象
     * @return 自定义对象
     */
    public static <T> T jsonToBean(String str, Class<T> clazz) {
        if (StringUtils.isBlank(str) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转换为自定义对象
     *
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T jsonToBean(String str, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转换为自定义对象
     *
     * @param str
     * @param collectionClazz
     * @param elementClazzes
     * @param <T>
     * @return
     */
    public static <T> T jsonToCollection(String str, Class<?> collectionClazz, Class<?>... elementClazzes) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClazz, elementClazzes);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
