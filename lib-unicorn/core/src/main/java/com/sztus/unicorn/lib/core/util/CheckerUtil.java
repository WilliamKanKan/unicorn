package com.sztus.unicorn.lib.core.util;

import java.util.Objects;

/**
 * @author Ernest Gou
 * @date 2020-12-09
 */
public class CheckerUtil {

    public static Boolean allNull(Object... args) {
        Boolean result = true;

        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (Objects.nonNull(arg)) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    public static Boolean anyNull(Object... args) {
        Boolean result = true;

        if (args != null && args.length > 0) {
            result = false;

            for (Object arg : args) {
                if (Objects.isNull(arg)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
}
