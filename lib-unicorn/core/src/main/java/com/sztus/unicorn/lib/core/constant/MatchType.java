package com.sztus.unicorn.lib.core.constant;


/**
 * @author ice
 * @description 字符串匹配类型常量定义
 */
public interface MatchType {

    int EQUALS = 1;
    int CONTAINS_TARGET = 2;
    int STARTS_WITH_TARGET = 3;
    int ENDS_WITH_TARGET = 4;
    int CONTAINS_SOURCE = 5;
    int STARTS_WITH_SOURCE = 6;
    int ENDS_WITH_SOURCE = 7;
    int IN = 8;
    int NOT_IN = 9;
    int NOT_EQUALS= 10;
    int NOT_CONTAINS= 11;
}
