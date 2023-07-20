package com.sztus.unicorn.search.service;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztus.unicorn.search.config.OpenAiModel;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.Optional;
@Service
public class AiServiceFactory {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10L);

    public static OpenAiService createService() {
        String token = "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";

        Assert.isTrue(StringUtils.isNotBlank(token), () -> String.valueOf(new RuntimeException("ApiKey不能为空，请检查参数配置")));

        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        // 设置代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(OpenAiModel.getProxyHost(), OpenAiModel.getProxyPort()));
        OkHttpClient client = OpenAiService.defaultClient(token, DEFAULT_TIMEOUT).newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = OpenAiService.defaultRetrofit(client, mapper);

        return new OpenAiService(retrofit.create(OpenAiApi.class), client.dispatcher().executorService());

    }
}
