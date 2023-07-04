package com.sztus.unicorn.lib.core.type;

import com.sztus.unicorn.lib.core.base.BaseError;

public enum ErrorCode implements BaseError {

    SUCCESS(1, "Success"),
    UNKNOWN(0, "Unknown"),
    FAILURE(-1, "Failure"),
    AJAX_RESULT_FAILURE(-2, "Ajax result is null."),
    RESULT_CODE_FAILURE(-3, "Result code is null."),
    RESULT_DATA_FAILURE(-4, "Result data is null."),

    QUERY_FAILURE_NO_CONDITION(-1001, "Query failure, at least set one condition."),
    QUERY_FAILURE_NOT_EXISTED(-1002, "Query failure, assigned record is not existed."),
    ;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
