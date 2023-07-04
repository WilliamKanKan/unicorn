package com.sztus.unicorn.lib.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sztus.unicorn.lib.core.base.BaseEnum;
import com.sztus.unicorn.lib.core.type.SimpleConvertCallback;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 枚举操作工具类
 * <pre>
 * 2018-09-17 Wolf
 *  1. 添加根据值直接获取文本方法
 *  2. 添加根据文本直接获取值方法
 *  3. 添加获取枚举对象值方法
 *  4. 添加获取枚举对象文本方法
 *  5. 优化根据字段查询枚举对象方法
 * 2018-09-16 Wolf
 *  1. 创建枚举操作工具类
 *  2. 实现根据字段查询枚举对象方法
 *  3. 实现根据值获取枚举对象方法
 *  4. 实现获取枚举对象值方法
 * </pre>
 */
public class EnumUtil {

    /**
     * 注： 只能作用于实现了 BaseEnum 的枚举类
     *
     * @param enums enums 传入枚举的 xxxEnum.values()
     * @param text  需要获取 value 的 text
     * @return 对应的 value
     */
    public static Integer getValueByText(BaseEnum[] enums, String text) {
        for (BaseEnum loopEnum : enums) {
            if (loopEnum.getText().equals(text)) {
                return loopEnum.getValue();
            }
        }
        return null;
    }

    /**
     * 注： 只能作用于实现了 BaseEnum 的枚举类
     *
     * @param enums enums 传入枚举的 xxxEnum.values()
     * @param value 需要获取 Text 的 value
     * @return 对应的 text
     */
    public static String getTextByValue(BaseEnum[] enums, Object value) {
        for (BaseEnum loopEnum : enums) {
            if (loopEnum.getValue().equals(value)) {
                return loopEnum.getText();
            }
        }
        return null;
    }

    /**
     *
     * 注： 只能作用于实现了 BaseEnum 的枚举类
     *
     * @param enums 传入枚举的 xxxEnum.values()
     * @param value value
     * @return 对应的枚举对象
     */
    public static BaseEnum getEnumByValue(BaseEnum[] enums, Object value) {

        for (BaseEnum loopEnum : enums) {
            if (loopEnum.getValue().equals(value)) {
                return loopEnum;
            }
        }
        return null;
    }

    /**
     *
     * 注： 只能作用于实现了 BaseEnum 的枚举类
     *
     * @param enums 传入枚举的 xxxEnum.values()
     * @param text text
     * @return 对应的枚举对象
     */
    public static BaseEnum getEnumByText(BaseEnum[] enums, String text) {

        for (BaseEnum loopEnum : enums) {
            if (loopEnum.getText().equals(text)) {
                return loopEnum;
            }
        }
        return null;
    }

    /**
     * 注： 只能作用于实现了 BaseEnum 的枚举类
     * <p>
     * 获取传入枚举中的所有 value
     *
     * @param enums 传入枚举的 xxxEnum.values()
     * @return 该枚举的所有 value 的集合
     */
    public static List<Integer> getAllValues(BaseEnum[] enums) {
        List<Integer> valueList = new ArrayList<>();
        for (BaseEnum loopEnum : enums) {
            valueList.add(loopEnum.getValue());
        }
        return valueList;
    }


    public static <E> JSONArray toJSONArray(Class<E> enumClass) {
        return toJSONArray(enumClass, null, null);
    }

    public static <E> JSONArray toJSONArray(Class<E> enumClass, SimpleConvertCallback valueCallback, SimpleConvertCallback textCallback) {
        JSONArray dataArray = new JSONArray();

        try {
            Method valuesMethod = enumClass.getDeclaredMethod("values");
            Method getValueMethod = null;
            if (valueCallback == null) {
                getValueMethod = enumClass.getDeclaredMethod("getValue");
            }
            Method getTextMethod = null;
            if (textCallback == null) {
                getTextMethod = enumClass.getDeclaredMethod("getText");
            }

            E[] itemArray = (E[]) valuesMethod.invoke(null);

            for (E item : itemArray) {
                Object value = null;
                if (getValueMethod == null) {
                    value = valueCallback.to(item);
                } else {
                    value = getValueMethod.invoke(item);
                }

                Object text = null;
                if (getTextMethod == null) {
                    text = textCallback.to(item);
                } else {
                    text = getTextMethod.invoke(item);
                }

                JSONObject dataJson = new JSONObject();
                dataJson.put(VALUE, value);
                dataJson.put(TEXT, text);
                dataJson.put(DATA, item);

                dataArray.add(dataJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataArray;
    }

    /**
     * 获取Class中的所有value
     * <p>
     * 新方法请查看 getAllValues
     *
     * @param clazz class对象
     * @return
     */
    @Deprecated
    public static <E> List<Integer> getAllValues(Class<E> clazz) {
        List<Integer> valueList = null;
        try {
            valueList = new ArrayList<>();
            Method getValueMethod = clazz.getDeclaredMethod("getValue");
            E[] enumConstants = clazz.getEnumConstants();
            for (E enumConstant : enumConstants) {
                Integer value = (Integer) getValueMethod.invoke(enumConstant);
                if (value == null) {
                    continue;
                }
                valueList.add(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueList;
    }


    /**
     * 根据值直接获取文本
     * <p>
     * 新方法请查看 getTextByValue
     *
     * @param enumClass
     * @param value
     * @return
     */
    @Deprecated
    public static <E> String getTextByValue(Class<E> enumClass, Object value) {
        E e = getByValue(enumClass, value);
        return getText(e);
    }

    public static <E> String getKeyByValue(Class<E> enumClass, Object value) {
        E e = getByValue(enumClass, value);

        return getKey(e);
    }

    public static <E> Integer getValueByKey(Class<E> enumClass, String key) {
        E e = getByKey(enumClass, key);

        return (Integer) getValue(e);
    }

    /**
     * 根据文本直接获取值
     * <p>
     * 新方法请查看 getValueByText
     *
     * @param enumClass
     * @param text
     * @return
     */
    @Deprecated
    public static <E> Object getValueByText(Class<E> enumClass, String text) {
        E e = getByText(enumClass, text);

        return getValue(e);
    }

    /**
     * 获取枚举对象值
     * <p>
     * 新方法请查看 getValue
     *
     * @param e
     * @return
     */
    @Deprecated
    public static <E> Object getValue(E e) {
        Object valueObj = null;

        try {
            if (e != null) {
                Class<?> enumClass = e.getClass();

                Method getValueMethod = enumClass.getDeclaredMethod("getValue");
                valueObj = getValueMethod.invoke(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return valueObj;
    }

    /**
     * 获取枚举对象文本
     * <p>
     * 新方法请查看 getText
     *
     * @param e
     * @return
     */
    @Deprecated
    public static <E> String getText(E e) {
        String text = null;

        try {
            if (e != null) {
                Class<?> enumClass = e.getClass();

                Method getTextMethod = enumClass.getDeclaredMethod("getText");
                Object textObj = getTextMethod.invoke(e);

                if (textObj != null) {
                    text = textObj.toString();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return text;
    }

    public static <E> String getCode(E e) {
        String code = null;

        try {
            if (e != null) {
                Class<?> enumClass = e.getClass();

                Method getTextMethod = enumClass.getDeclaredMethod("getCode");
                Object textObj = getTextMethod.invoke(e);

                if (textObj != null) {
                    code = textObj.toString();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return code;
    }

    public static <E> String getKey(E e) {
        String code = null;

        try {
            if (e != null) {
                Class<?> enumClass = e.getClass();

                Method getKeyMethod = enumClass.getDeclaredMethod("getKey");
                Object textObj = getKeyMethod.invoke(e);

                if (textObj != null) {
                    code = textObj.toString();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return code;
    }


    /**
     * 根据值获取枚举对象
     * <p>
     * 新方法请查看 getEnumByValue
     *
     * @param enumClass
     * @param value
     * @return
     */
    @Deprecated
    public static <E> E getByValue(Class<E> enumClass, Object value) {
        return getByField(enumClass, FIELD_VALUE, value);
    }

    /**
     * 根据文本获取枚举对象
     *
     * 新方法请查看 getEnumByText
     *
     * @param enumClass
     * @param text
     * @return
     */
    @Deprecated
    public static <E> E getByText(Class<E> enumClass, String text) {
        return getByField(enumClass, FIELD_TEXT, text);
    }

    /**
     * 根据key获取枚举对象
     *
     * @param enumClass
     * @param keyName
     * @param <E>
     * @return
     */
    public static <E> E getByKey(Class<E> enumClass, String keyName) {
        E target = null;
        try {
            Method getKeyMethod = enumClass.getDeclaredMethod("getKey");
            E[] eArray = enumClass.getEnumConstants();
            for (E e : eArray) {
                Object keyObj = getKeyMethod.invoke(e);
                if (keyObj == null) {
                    continue;
                }

                if (Objects.equals(keyObj.toString(), keyName)) {
                    target = e;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return target;
    }

    public static <E> E getByCode(Class<E> enumClass, String text) {
        return getByField(enumClass, FIELD_CODE, text);
    }

    /**
     * 根据字段查询枚举对象
     *
     * @param enumClass
     * @param field
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <E> E getByField(Class<E> enumClass, int field, Object obj) {
        E target = null;

        if (obj != null) {
            try {
                Method valuesMethod = enumClass.getDeclaredMethod("values");

                E[] eArray = (E[]) valuesMethod.invoke(null);
                for (E e : eArray) {
                    switch (field) {
                        case FIELD_VALUE:
                            Object value = getValue(e);
                            if (value != null && value.equals(obj)) {
                                target = e;
                            }
                            break;
                        case FIELD_TEXT:
                            String text = getText(e);
                            if (text != null && text.equals(obj)) {
                                target = e;
                            }
                            break;
                        case FIELD_CODE:
                            String code = getCode(e);
                            if (code != null && code.equals(obj)) {
                                target = e;
                            }
                            break;
                        default:
                            break;
                    }

                    if (target != null) {
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return target;
    }

    private static final int FIELD_VALUE = 1;
    private static final int FIELD_TEXT = 2;
    private static final int FIELD_CODE = 3;
    private static String VALUE = "value";
    private static String TEXT = "text";
    private static String DATA = "data";
}
