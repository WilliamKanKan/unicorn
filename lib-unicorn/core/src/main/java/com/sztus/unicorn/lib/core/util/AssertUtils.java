package com.sztus.unicorn.lib.core.util;

import com.sztus.unicorn.lib.core.exception.IllegalObjectException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author Ernest Gou
 */
public final class AssertUtils {

    private static final String ILLEGAL_OBJ_PREFIX = "[Illegal Object] - ";
    private static final String ILLEGAL_ARG_PREFIX = "[Illegal Argument] - ";
    private static final String IS_NULL_MESSAGE = "%s must not be null";
    private static final String IS_BLANK_MESSAGE = "%s must not be blank";
    private static final String IS_ZERO_MESSAGE = "%s has to be non-zero";

    public static void notNullObj(Object obj, String name) {
        if (obj == null) {
            throw new IllegalObjectException(String.format(ILLEGAL_OBJ_PREFIX + IS_NULL_MESSAGE, name));
        }
    }

    public static void notBlankObj(String str, String name) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalObjectException(String.format(ILLEGAL_OBJ_PREFIX + IS_BLANK_MESSAGE, name));
        }
    }

    public static void notNullArg(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format(ILLEGAL_ARG_PREFIX + IS_NULL_MESSAGE, name));
        }
    }

    public static void notNullArg(String names, Object... objs) {
        Arrays.stream(objs).forEach(obj -> {
            if (obj == null) {
                throw new IllegalArgumentException(String.format(ILLEGAL_ARG_PREFIX + IS_NULL_MESSAGE, names));
            }
        });
    }

    public static void notZeroArg(Number number, String name) {
        notNullArg(number, name);

        if (number instanceof Integer) {
            if ((Integer) number == 0) {
                throw new IllegalArgumentException(String.format(ILLEGAL_ARG_PREFIX + IS_ZERO_MESSAGE, name));
            }
        }
    }

    public static void notBlankArg(String str, String name) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(String.format(ILLEGAL_ARG_PREFIX + IS_BLANK_MESSAGE, name));
        }
    }

}
