package com.sztus.unicorn.lib.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sztus.unicorn.lib.core.type.AjaxResult;

/**
 * @author Andy
 * @create 2023/5/17 10:52
 */
public class AjaxResultUtil {

    /**
     * 处理返回值为Ajax Result的请求， 将其转换为传入的对象
     * @param ajaxResultStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T processAjaxResult(String ajaxResultStr, Class<T> clazz) {
        AjaxResult ajaxResult = JSONObject.parseObject(ajaxResultStr, AjaxResult.class);
        if(ajaxResult.getCode() > 0) {
            return JSONObject.parseObject(JSON.toJSONString(ajaxResult.getData()), clazz);
        } else {
            return null;
        }
    }

}
