package com.sztus.unicorn.lib.core.util;

import com.sztus.unicorn.lib.core.constant.GlobalConst;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机字符串工具类
 */
public class RandomUtil {

    /**
     * 获取纯数字随机字符串
     *
     * @return
     */
    public static String getNumeric() {
        return getNumeric(GlobalConst.DEFAULT_RANDOM_SIZE);
    }

    /**
     * 获取纯数字随机字符串
     *
     * @param length
     * @return
     */
    public static String getNumeric(Integer length) {
        String targetStr = "";

        while (targetStr.length() != length) {
            String randomStr = String.valueOf(Math.random());

            if (randomStr.length() - 2 > length) {
                // 去掉前两位后的数字序列
                targetStr = randomStr.substring(2, 2 + length);
            }
        }

        return targetStr;
    }

    /**
     * 获取数字字母随机字符串
     *
     * @return
     */
    public static String getAlphaNumeric() {
        long seed = Long.parseLong(String.format("%s%d%s",
                getNumeric(2),
                (new Date()).getTime(),
                getNumeric(3)));

        char[] buffer = new char[DIGIT_BUFFER_SIZE];
        int charPos = DIGIT_BUFFER_SIZE;
        int radix = 1 << DIGIT_OFFSET;
        long mask = radix - 1;

        do {
            buffer[--charPos] = DIGIT_ARRAY[(int) (seed & mask)];
            seed >>>= DIGIT_OFFSET;
        } while (seed != 0);

        return new String(buffer, charPos, (DIGIT_BUFFER_SIZE - charPos));
    }

    /**
     *
     * @param length
     * @return
     */

    public static String generateRandomAlpha(int length) {
        char[] buf = new char[length];
        Random random = ThreadLocalRandom.current();
        for (int i=0; i < length; i++) {
            buf[i] = DIGIT_ARRAY[random.nextInt(DIGIT_ARRAY.length)];
        }
        return new String(buf);
    }

    private static final int DIGIT_BUFFER_SIZE = 64;
    private static final int DIGIT_OFFSET = 6;
    private static final char[] DIGIT_ARRAY = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '-', '_'
    };
}
