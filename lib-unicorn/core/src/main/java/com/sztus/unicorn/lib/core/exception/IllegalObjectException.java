package com.sztus.unicorn.lib.core.exception;

public class IllegalObjectException extends RuntimeException {

    private static final long serialVersionUID = 7886320669774141616L;

    public IllegalObjectException() {
        super();
    }

    public IllegalObjectException(String msg) {
        super(msg);
    }
}
