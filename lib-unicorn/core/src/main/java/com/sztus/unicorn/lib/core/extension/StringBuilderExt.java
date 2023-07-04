package com.sztus.unicorn.lib.core.extension;

public class StringBuilderExt {
    private final StringBuilder stringBuilder;

    public StringBuilderExt() {
        this.stringBuilder = new StringBuilder();
    }

    public <T> StringBuilderExt append(T t) {
        stringBuilder.append(t);
        return this;
    }

    public <T> StringBuilderExt appendLine(T t) {
        stringBuilder.append(t).append(System.lineSeparator());
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }
}
