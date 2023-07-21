package com.sztus.unicorn.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
@ConfigurationProperties(prefix = "openai")
public class OpenAiModel {
    /**
     * 代理地址
     */
    private static String proxyHost;
    /**
     * 代理端口
     */
    private static Integer proxyPort;
    /**
     * openai apikey
     */
    private static List<String> keys;

    public static String getProxyHost() {
        return proxyHost;
    }

    public static void setProxyHost(String proxyHost) {
        OpenAiModel.proxyHost = proxyHost;
    }

    public static Integer getProxyPort() {
        return proxyPort;
    }

    public static void setProxyPort(Integer proxyPort) {
        OpenAiModel.proxyPort = proxyPort;
    }

    public static List<String> getKeys() {
        return keys;
    }

    public static void setKeys(List<String> keys) {
        OpenAiModel.keys = keys;
    }
}
