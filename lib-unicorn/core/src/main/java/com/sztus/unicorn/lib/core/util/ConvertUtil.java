package com.sztus.unicorn.lib.core.util;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Base64;
import java.util.List;
import java.util.Set;

public class ConvertUtil {
    public static <E> E[] listToArray(Class<E> dataClass, List<E> dataList) {
        E[] dataArray = (E[]) Array.newInstance(dataClass, dataList.size());

        dataArray = dataList.toArray(dataArray);

        return dataArray;
    }

    public static JSONObject toUnderscoreJson(JSONObject sourceJson) {
        JSONObject targetJson = formatJson(sourceJson, FORMAT_JSON_RULE_CAMEL_TO_UNDERSCORE);

        sourceJson = targetJson;

        return targetJson;
    }

    public static JSONObject toCamelJson(JSONObject sourceJson) {
        JSONObject targetJson = formatJson(sourceJson, FORMAT_JSON_RULE_UNDERSCORE_TO_CAMEL);

        sourceJson = targetJson;

        return targetJson;
    }

    private static JSONObject formatJson(JSONObject sourceJson, Integer rule) {
        JSONObject targetJson = null;

        if (sourceJson != null) {
            Set<String> sourceKeySet = sourceJson.keySet();
            for (String sourceKey : sourceKeySet) {
                String targetKey = null;

                switch (rule) {
                    case FORMAT_JSON_RULE_CAMEL_TO_UNDERSCORE:
                        targetKey = StringUtil.formatCamelToUnderscore(sourceKey);
                        break;
                    case FORMAT_JSON_RULE_UNDERSCORE_TO_CAMEL:
                        targetKey = StringUtil.formatUnderscoreToCamel(sourceKey);
                        break;
                    default:
                        break;
                }

                if (targetKey != null) {
                    targetJson.put(targetKey, sourceJson.get(sourceKey));
                }
            }
        }

        return targetJson;
    }


    /**
     * 将流转换成字符串 使用Base64加密
     */
    public static String streamToString(InputStream inputStream) throws IOException {
        byte[] bt = toByteArray(inputStream);
        inputStream.close();
        return Base64.getEncoder().encodeToString(bt);
    }

    /**
     * 字符串转换成流 使用Base64解密
     */
    public static InputStream stringToStream(String str) throws IOException {
        return new ByteArrayInputStream(Base64.getDecoder().decode(str));
    }

    /**
     * summary:将流转化为字节数组
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        byte[] result;
        try {
            int n;
            while ((n = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            result = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
        return result;
    }

    private static final int FORMAT_JSON_RULE_CAMEL_TO_UNDERSCORE = 1;
    private static final int FORMAT_JSON_RULE_UNDERSCORE_TO_CAMEL = 2;

}
