package com.sztus.unicorn.lib.core.enumerate;

import com.sztus.unicorn.lib.core.base.BaseEnum;

public enum CodeEnum implements BaseEnum {

    SUCCESS(1, "Success"),
    DEFAULT(0, "Default"),
    FAILURE(-1, "Failure"),

    AUTH_ERROR_CREDENTIAL_IS_EMPTY(-1003, "Authorization Failure: The Credential is empty"),
    AUTH_ERROR_CREDENTIAL_IS_WRONG_FORMAT(-1004, "Authorization Failure: The Credential is wrong format"),

    AUTH_ERROR_EMPLOYEE_NOT_MATCH(-1005, "Authorization Failure: Incorrect Username or Password"),

    AUTH_ERROR_EMPLOYEE_NOT_EXIST(-1010, "Authorization Failure: The Employee is NOT existed"),

    AUTH_ERROR_CREDENTIAL_IS_INCOMPLETE(-1015, "Authorization Failure: The Credential is incomplete");

    CodeEnum(Integer value, String text) {
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
