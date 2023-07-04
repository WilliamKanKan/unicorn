package com.sztus.unicorn.lib.core.util;


import com.sztus.unicorn.lib.core.enumerate.DateConvertTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 日期格式工具类
 *
 * @author Jay
 * @date 2022/09/08
 */
public class DateConverterUtil {

    private static String DATE_PATTERN = null;
    private static String TIME_PATTERN = null;
    private static String DATE_TIME_PATTERN = null;

//    private static CommonRedisRepository commonRedisRepository;
//
//    public static void setCommonRedisRepository(CommonRedisRepository commonRedisRepository) {
//        DateConverterUtil.commonRedisRepository = commonRedisRepository;
//    }
//
//    @Autowired
//    public void setBean(CommonRedisRepository commonRedisRepository) {
//        setCommonRedisRepository(commonRedisRepository);
//    }

    /**
     * 初始化时间格式
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    public static void initPattern() {
//        DATE_PATTERN = commonRedisRepository.get(commonRedisRepository.generateKey(CacheKey.SYSTEM_PARAMETER, CacheKey.DATE));
//        TIME_PATTERN = commonRedisRepository.get(commonRedisRepository.generateKey(CacheKey.SYSTEM_PARAMETER, CacheKey.TIME));
//        DATE_TIME_PATTERN = commonRedisRepository.get(commonRedisRepository.generateKey(CacheKey.SYSTEM_PARAMETER, CacheKey.DATE_TIME));
    }

    /**
     * 时间戳转时间格式  格式为redis中的 DATE_PATTERN
     *
     * @param timeStamp 时间戳
     * @return {@link String}
     */
    public static String timeStampToDateStr(Long timeStamp) {
        if (StringUtils.isEmpty(DATE_PATTERN)) {
            initPattern();
        }
        return DateUtil.timeStampToStr(timeStamp, DATE_PATTERN);
    }

    /**
     * 时间戳转时间格式  格式为redis中的 TIME_PATTERN
     *
     * @param timeStamp 时间戳
     * @return {@link String}
     */
    public static String timeStampToTimeStr(Long timeStamp) {
        if (StringUtils.isEmpty(TIME_PATTERN)) {
            initPattern();
        }
        return DateUtil.timeStampToStr(timeStamp, TIME_PATTERN);
    }

    /**
     * 时间戳转时间格式  格式为redis中的 DATE_TIME_PATTERN
     *
     * @param timeStamp 时间戳
     * @return {@link String}
     */
    public static String timeStampToDateTimeStr(Long timeStamp) {
        if (StringUtils.isEmpty(DATE_TIME_PATTERN)) {
            initPattern();
        }
        return DateUtil.timeStampToStr(timeStamp, DATE_TIME_PATTERN);
    }


    /**
     * data转时间格式 格式为redis中的 DATE_PATTERN
     *
     * @param date 日期
     * @return {@link String}
     */
    public static String dateToDateStr(Date date) {
        if (StringUtils.isEmpty(DATE_PATTERN)) {
            initPattern();
        }
        return DateUtil.dateToStr(date, DATE_PATTERN);
    }

    /**
     * data转时间格式 格式为redis中的 TIME_PATTERN
     *
     * @param date 日期
     * @return {@link String}
     */
    public static String dateToTimeStr(Date date) {
        if (StringUtils.isEmpty(TIME_PATTERN)) {
            initPattern();
        }
        return DateUtil.dateToStr(date, TIME_PATTERN);
    }

    /**
     * data转时间格式 格式为redis中的 DATE_TIME_PATTERN
     *
     * @param date 日期
     * @return {@link String}
     */
    public static String dateToDateTimeStr(Date date) {
        if (StringUtils.isEmpty(DATE_TIME_PATTERN)) {
            initPattern();
        }
        return DateUtil.dateToStr(date, DATE_TIME_PATTERN);
    }

    /**
     * 批量转化时间格式
     * @param list 需要转化的对象集合
     * @param infos get,set方法,加日期格式
     * @param <T> 泛型
     */
    public static <T> void batchConvertDate(List<T> list, Info<T>... infos) {
        for (T t : list) {
            convertDate(t, infos);
        }
    }

    /**
     * 单条数据的日期转化格式
     *
     * @param object object
     * @param infos  infos
     */
    public static <T> void convertDate(T object, Info<T>... infos) {

        for (Info<T> info : infos) {

            String timeStr = info.getNeedConvertTime.apply(object);

            switch (info.convertType) {
                case DATE: {
                    String newDateValue = DateConverterUtil.timeStampToDateStr(Long.valueOf(timeStr));
                    Optional.ofNullable(newDateValue).orElseThrow(() -> new RuntimeException("Convert date parameter conversion exception"));
                    info.setConvertStr.accept(object, newDateValue);
                    break;
                }
                case TIME: {
                    String newDateValue = DateConverterUtil.timeStampToTimeStr(Long.valueOf(timeStr));
                    Optional.ofNullable(newDateValue).orElseThrow(() -> new RuntimeException("Convert date parameter conversion exception"));
                    info.setConvertStr.accept(object, newDateValue);
                    break;
                }
                case DATE_TIME: {
                    String newDateValue = DateConverterUtil.timeStampToDateTimeStr(Long.valueOf(timeStr));
                    Optional.ofNullable(newDateValue).orElseThrow(() -> new RuntimeException("Convert date parameter conversion exception"));
                    info.setConvertStr.accept(object, newDateValue);
                    break;
                }
                default:
                    throw new IllegalStateException("Convert date convert type Illegal State Exception");
            }
        }
    }

    /**
     * get set 方法
     * 日期转化类型
     * @param <T> 泛型
     */
    public static class Info<T> {

        private Function<T, String> getNeedConvertTime;
        private BiConsumer<T, String> setConvertStr;
        private DateConvertTypeEnum convertType;

        public Info(DateConvertTypeEnum convertType, Function<T, String> getNeedConvertTime, BiConsumer<T, String> setConvertStr) {
            this.getNeedConvertTime = getNeedConvertTime;
            this.convertType = convertType;
            this.setConvertStr = setConvertStr;
        }
    }
}
