package com.sztus.unicorn.lib.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * json 工具类
 *
 * @author ulin
 */
public class JsonUtil {

    public static Boolean containsKey(JSONObject dataJson, String... keys) {
        Boolean result = true;

        if (dataJson != null) {
            for (String key : keys) {
                if (!dataJson.containsKey(key)) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }

        return result;
    }

    /**
     * Get Any List
     */
    public static <E> List<E> fetchFromCollection(Collection<JSONObject> collection, String key, Class<E> clazz) {
        List<E> result = new ArrayList<>();
        if (null != collection && collection.size() > 0) {
            for (JSONObject jsonObj : collection) {
                result.add(JSON.parseObject(jsonObj.getString(key), clazz));
            }
        }

        return result;
    }

    public static <T> List<T> extractFieldValueList(JSONArray dataArray, String fieldKey) {
        List<T> valueList = new ArrayList<>();

        for (int i = 0; i < dataArray.size(); ++i) {
            JSONObject dataJson = dataArray.getJSONObject(i);

            T fieldValue = (T) dataJson.get(fieldKey);
            valueList.add(fieldValue);
        }

        return valueList;
    }

    /**
     * 获取 jsonArray 中关键字的集合
     */
    public static List<String> getAssignFieldList(JSONArray dataArray, String keyName) {
        ArrayList<String> assignFieldList = new ArrayList<>();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataJson = dataArray.getJSONObject(i);
            String string = dataJson.getString(keyName);
            assignFieldList.add(string);
        }
        return assignFieldList;
    }

    /**
     * 获取 jsonArray 中关键字的集合类型为long
     */
    public static List<Long> getAssignFieldListLong(JSONArray dataArray, String keyName) {
        List<Long> assignFieldList = new LinkedList<>();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataJson = dataArray.getJSONObject(i);
            Long value = dataJson.getLong(keyName);
            assignFieldList.add(value);
        }
        return assignFieldList;
    }

    /**
     * 提取关键字段值作为key，value 为原jsonObject对象
     */
    public static HashMap<String, JSONObject> getAssignFieldMap(JSONArray dataArray, String keyName) {
        HashMap<String, JSONObject> assignFieldMap = new HashMap<>();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataJson = dataArray.getJSONObject(i);
            String keyValue = dataJson.getString(keyName);
            assignFieldMap.put(keyValue, dataJson);
        }
        return assignFieldMap;
    }

    /**
     * 提取关键字段值作为key 类型改为long，value 为原jsonObject对象
     */
    public static HashMap<Long, JSONObject> getAssignFieldMapLong(JSONArray dataArray, String keyName) {
        HashMap<Long, JSONObject> assignFieldMap = new HashMap<>();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataJson = dataArray.getJSONObject(i);
            Long keyValue = dataJson.getLong(keyName);
            assignFieldMap.put(keyValue, dataJson);
        }
        return assignFieldMap;
    }

    public static HashMap<Long, JSONArray> getAssignFieldMapArray(JSONArray dataArray, String keyName) {
        HashMap<Long, JSONArray> assignFieldMap = new HashMap<>();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataJson = dataArray.getJSONObject(i);
            Long keyValue = dataJson.getLong(keyName);
            JSONArray dataArr = assignFieldMap.get(keyValue);
            if (dataArr == null) {
                dataArr = new JSONArray();
            }
            dataArr.add(dataJson);
            assignFieldMap.put(keyValue, dataArr);
        }
        return assignFieldMap;
    }

    public static HashMap<String, JSONObject> getAssignFieldMap(JSONArray dataArray, String keyName, String keyName2) {
        HashMap<String, JSONObject> assignFieldMap = new HashMap<>();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataJson = dataArray.getJSONObject(i);
            String keyValue = dataJson.getString(keyName);
            String keyValue2 = dataJson.getString(keyName2);
            assignFieldMap.put(keyValue + keyValue2, dataJson);
        }
        return assignFieldMap;
    }

    public static JSONObject array2json(JSONArray itemArray, String keyName, String valueName) {
        JSONObject dataJson = new JSONObject();

        if (itemArray != null) {
            itemArray.forEach((Object itemObject) -> {
                JSONObject itemJson = (JSONObject) itemObject;

                if (itemJson.containsKey(keyName) && itemJson.containsKey(valueName)) {
                    dataJson.put(
                            itemJson.getString(keyName),
                            itemJson.getString(valueName)
                    );
                }
            });
        }

        return dataJson;
    }

    /**
     * 根据数据源和模板解析为JSON
     *
     * @param dataSrc String 数据源
     * @param pattern String 格式源
     * @param flag    Boolean 去除数据源引号
     */
    public static JSONObject parseStringToJson(String dataSrc, String pattern, Boolean flag) {
        String regex = "\\{(\\w)*\\}";
        String[] fixedData = pattern.split(regex);
        ArrayList<String> valuesList = new ArrayList<String>();
        Integer index = 0;
        Integer startIndex = -1;
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(pattern);
        JSONObject dataJson = new JSONObject();

        for (int i = 0; index < dataSrc.length(); i++) {
            int start = 0;
            if (dataSrc.endsWith("\"")) {
                while (true) {
                    start = dataSrc.indexOf(fixedData[i], startIndex);
                    if (start != 0 && start < dataSrc.length() - 1) {
                        char c = dataSrc.charAt(start + 1);
                        String s = String.valueOf(c);
                        if (s.equalsIgnoreCase("\"")) {
                            break;
                        }
                    } else {
                        break;
                    }
                    startIndex += 1;
                }
            } else {
                start = dataSrc.indexOf(fixedData[i], startIndex);

            }
            int end = -1;
            if (i < (fixedData.length - 1)) {
                end = dataSrc.indexOf(fixedData[i + 1], start + fixedData[i].length());
                index = end + fixedData[i + 1].length();
            } else {
                end = dataSrc.length();
                index = end;
            }
            String value = dataSrc.substring(start + fixedData[i].length(), end);
            startIndex = start + value.length();
            if (matcher.find()) {

                String jsonKey = matcher.group();
                if (flag && value.startsWith("\"")) {
                    dataJson.put(jsonKey.substring(1, jsonKey.length() - 1), value.substring(1, value.length() - 1));
                } else {
                    dataJson.put(jsonKey.substring(1, jsonKey.length() - 1), value);
                }
                valuesList.add(value);
            }
        }

        return dataJson;

    }

    /**
     * 根据模板和数据源填充数据
     *
     * @param patterSrc String 数据模板
     * @param dataSrc   JSONObject 格式源
     * @param flag      Boolean 添加引号
     */
    public static String fillStringByJsonObject(String patterSrc, JSONObject dataSrc, Boolean flag) {
        String patterSrcTemp = patterSrc;
        Iterator<Map.Entry<String, Object>> iterator = dataSrc.entrySet().iterator();
        while (!dataSrc.isEmpty()) {
            patterSrcTemp = fillStringByJsonObject(patterSrcTemp, iterator, flag);
        }
        //{__*} 替换为空
        String match = "\\{_(\\w)*\\}";
        if (flag) {
            patterSrcTemp = Pattern.compile(match).matcher(patterSrcTemp).replaceAll("\"\"");
        } else {
            patterSrcTemp = Pattern.compile(match).matcher(patterSrcTemp).replaceAll("");
        }
        return patterSrcTemp;
    }

    /**
     * @param patterSrc String 数据模板
     * @param iterator  Iterator 格式源迭代器
     * @param flag      Boolean 添加引号
     */
    private static String fillStringByJsonObject(String patterSrc, Iterator<Map.Entry<String, Object>> iterator,
                                                 Boolean flag) {
        if (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            iterator.remove();
            String match = "\\{(" + key + ")\\}";
            if (flag) {
                value = "\"" + value + "\"";
            }
            Pattern compile = Pattern.compile(match);
            Matcher matcher = compile.matcher(patterSrc);
            String temp = matcher.replaceAll(String.valueOf(value));
            return temp;
        }
        return patterSrc;
    }

}
