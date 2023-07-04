package com.sztus.unicorn.lib.core.base;

import java.util.Objects;

/**
 * 枚举对象基类
 *
 * @author wolf
 */
public interface BaseEnum {

    /**
     * 获取值信息
     *
     * @return
     */
    Integer getValue();

    /**
     * 获取文本信息
     *
     * @return
     */
    String getText();

    /**
     * 根据值获取枚举对象
     *
     * @param enumClass
     * @param value
     * @return
     */
    static <T extends BaseEnum> T fromValue(Class<T> enumClass, Integer value) {
        if (value == null) {
            return null;
        }

        for (T e : enumClass.getEnumConstants()) {
            if (Objects.equals(value, e.getValue())) {
                return e;
            }
        }

        throw new IllegalArgumentException(String.format("No enum value %d of %s found.", value, enumClass.getCanonicalName()));
    }

    /**
     * 根据文本获取枚举对象
     *
     * @param enumClass
     * @param text
     * @return
     */
    static <T extends BaseEnum> T fromText(Class<T> enumClass, String text) {
        for (T e : enumClass.getEnumConstants()) {
            if (Objects.equals(text, e.getText())) {
                return e;
            }
        }

        throw new IllegalArgumentException(String.format("No enum text %s of %s found.", text, enumClass.getCanonicalName()));
    }

}
