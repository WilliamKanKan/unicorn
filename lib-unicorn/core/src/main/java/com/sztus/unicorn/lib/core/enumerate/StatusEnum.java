package com.sztus.unicorn.lib.core.enumerate;

import com.sztus.unicorn.lib.core.base.BaseEnum;

/**
 * State enumeration definition
 * 
 * 2018-09-01 Wolf
 *      Create an enumeration definition
 */
public enum StatusEnum implements BaseEnum {

    /**
     * The unknown
     */
    UNKNOWN(0, "Unknown"),
    
    /**
     * To enable the
     */
    ENABLE(1, "Enable"),
    
    /**
     * disable
     */
    DISABLE(-1, "Disable");
    
    StatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }
    
    @Override
    public Integer getValue() {
        return this.value;
    }
    @Override
    public String getText() {
        return this.text;
    }
    
    private Integer value;
    private String text;
    
}
