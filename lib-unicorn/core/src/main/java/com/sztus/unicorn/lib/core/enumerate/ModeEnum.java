package com.sztus.unicorn.lib.core.enumerate;

import com.sztus.unicorn.lib.core.base.BaseEnum;

public enum ModeEnum implements BaseEnum {
    TEST(0, "Test"),
    PRODUCE(1, "Produce"),
    AUDIT(2, "Audit");

    ModeEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    private Integer value;
    private String text;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }

}
