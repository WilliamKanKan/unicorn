package com.sztus.unicorn.lib.core.enumerate;

import com.sztus.unicorn.lib.core.base.BaseEnum;

public enum GenderEnum implements BaseEnum {

    MALE(201, "Male","M"),
    FEMALE(202, "Female","F"),
    UNKNOW(209, "Unknow","U");

    GenderEnum(Integer value, String text, String code) {
        this.value = value;
        this.text = text;
        this.code = code;
    }
    
    private Integer value;
    private String text;
    private String code;
    
    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getText() {
        return this.text;
    }

    public String getCode() {
        return code;
    }}
