package com.sztus.unicorn.lib.core.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sztus.unicorn.lib.core.base.BaseError;
import com.sztus.unicorn.lib.core.exception.ProcedureException;

/**
 * AJAX结果数据结构定义
 */
public class AjaxResult {


    //region Structure Methods
    protected AjaxResult() {
    }

    public AjaxResult(Integer code) {
        this.code = code;
    }

    private AjaxResult(BaseError error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    private AjaxResult(BaseError error, String message) {
        this.code = error.getCode();
        this.message = message;
    }

    private AjaxResult(BaseError error, Object data) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.data = data;
    }

    private AjaxResult(ProcedureException e) {
        this.code = e.getCode();
        this.message = e.getMessage();
    }
    //endregion

    //region Public Methods
    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.IgnoreNonFieldGetter);
    }

    public String toDataString() {
        String dataString = null;

        if (data != null) {
            Class<?> dataClass = data.getClass();
            if (!dataClass.isPrimitive()) {
                dataString = JSON.toJSONString(data);
            } else {
                dataString = data.toString();
            }
        }

        return dataString;
    }

    public static String result(BaseError error) {
        AjaxResult result = new AjaxResult(error);
        return result.toString();
    }

    public static String result(BaseError error, Object data) {
        AjaxResult result = new AjaxResult(error, data);
        return result.toString();
    }

    public static String success() {
        return result(ErrorCode.SUCCESS);
    }

    public static String success(Object data) {
        return result(ErrorCode.SUCCESS, data);
    }

    public static String result(Integer code) {
        return JSON.toJSONString(new AjaxResult(code));
    }

    public static String success(Object data,String message) {
        AjaxResult result = new AjaxResult();
        result.setCode(ErrorCode.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(data);
        return result.toString();
    }

    public static String failure(Object data) {
        return result(ErrorCode.FAILURE, data);
    }

    public static String failure(Object data,String message) {
        AjaxResult result = new AjaxResult();
        result.setCode(ErrorCode.FAILURE.getCode());
        result.setMessage(message);
        result.setData(data);
        return result.toString();
    }

    public static String failure() {
        return result(ErrorCode.FAILURE);
    }

    public static String failure(BaseError error) {
        return result(error);
    }

    public static String failure(ProcedureException e) {
        AjaxResult result = new AjaxResult(e);
        return result.toString();
    }

    public static String failure(String message) {
        AjaxResult result = new AjaxResult(ErrorCode.FAILURE, message);
        return result.toString();
    }
    //endregion

    //region Getter Methods
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    //endregion

    private Integer code;
    private String message;
    private Object data;
}
