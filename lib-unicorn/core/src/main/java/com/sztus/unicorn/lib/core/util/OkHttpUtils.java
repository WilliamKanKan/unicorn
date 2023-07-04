package com.sztus.unicorn.lib.core.util;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public final class OkHttpUtils {
    // 线程工作内存值变化,通知主内存
    private static volatile OkHttpClient okHttpClient = null;
    private static volatile Semaphore semaphore = null;
    private Map<String, String> headerMap;
    private Map<String, String> paramMap;
    private String url;
    private Request.Builder request;

    /**
     * 初始化okHttpClient，并且允许https访问
     */
    private OkHttpUtils() {
        if (okHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    TrustManager[] trustManagers = buildTrustManagers();
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
                            .hostnameVerifier((hostName, session) -> true)
                            .retryOnConnectionFailure(false)
                            .build();
                }
            }
        }
    }

    /**
     * 用于异步请求时，控制访问线程数，返回结果
     *
     * @return semaphore实例
     */
    private static Semaphore getSemaphoreInstance() {
        // 只能1个线程同时访问
        if (semaphore == null) {
            synchronized (OkHttpUtils.class) {
                if (semaphore == null) {
                    semaphore = new Semaphore(0);
                }
            }
        }

        return semaphore;
    }

    /**
     * 创建OkHttpUtils
     *
     * @return 创建OkHttpUtils实例
     */
    public static OkHttpUtils builder() {
        return new OkHttpUtils();
    }

    /**
     * 添加url
     *
     * @param url 请求路径
     * @return OkHttpUtils实例
     */
    public OkHttpUtils url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return OkHttpUtils实例
     */
    public OkHttpUtils addParam(String key, String value) {
        if (paramMap == null) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.put(key, value);
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key   参数名
     * @param value 参数值
     * @return OkHttpUtils实例
     */
    public OkHttpUtils addHeader(String key, String value) {
        if (headerMap == null) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }

    /**
     * 初始化get方法
     *
     * @return OkHttpUtils实例
     */
    public OkHttpUtils get() {
        request = new Request.Builder().get();
        StringBuilder urlBuilder = generateParameters(paramMap);
        request.url(urlBuilder.toString());
        return this;
    }

    private StringBuilder generateParameters(Map<String, String> paramMap) {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
                            append("=").
                            append(URLEncoder.encode(entry.getValue(), "utf-8")).
                            append("&");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        return urlBuilder;
    }

    /**
     * 初始化get方法
     *
     * @return OkHttpUtils实例
     */
    public OkHttpUtils get(Map<String, String> paramMap) {
        StringBuilder urlBuilder = generateParameters(paramMap);
        request = new Request.Builder()
                .get()
                .url(urlBuilder.toString());
        return this;
    }

    /**
     * 初始化post方法
     *
     * @param isJsonPost true等于json的方式提交数据，类似postman里post方法的raw
     *                   false等于普通的表单提交
     * @return OkHttpUtils实例
     */
    public OkHttpUtils post(boolean isJsonPost) {
        RequestBody requestBody;
        if (isJsonPost) {
            String json = "";
            if (paramMap != null) {
                json = JSON.toJSONString(paramMap);
            }
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder formBody = new FormBody.Builder();
            if (paramMap != null) {
                paramMap.forEach(formBody::add);
            }
            requestBody = formBody.build();
        }
        request = new Request.Builder().post(requestBody).url(url);
        return this;
    }

    /**
     * 初始化 post 方法
     *
     * @param jsonString 参数转为 json 字符串
     * @return OkHttpUtils实例
     */
    public OkHttpUtils postByJson(String jsonString) {
        request = new Request.Builder()
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString))
                .url(url);
        return this;
    }

    /**
     * 初始化 post 方法
     *
     * @param paramMap 参数转为 map
     * @return OkHttpUtils实例
     */
    public OkHttpUtils postByForm(Map<String, String> paramMap) {

        FormBody.Builder formBody = new FormBody.Builder();
        if (paramMap != null) {
            paramMap.forEach(formBody::add);
        }

        request = new Request.Builder()
                .post(formBody.build())
                .url(url);
        return this;
    }

    /**
     * 同步请求
     *
     * @return 响应结果
     */
    public String sync() {
        setHeader(request);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            AssertUtils.notNullObj(response.body(), "responseBody");

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "Request Error：" + e.getMessage();
        }
    }

    /**
     * 异步请求，有返回值
     */
    public String async() {
        StringBuilder buffer = new StringBuilder();
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                buffer.append("Request Error：").append(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    buffer.append(response.body().string());
                }

                // 释放线程等待,拼接结果
                getSemaphoreInstance().release();
            }
        });

        try {
            // 线程等待异步结果返回
            getSemaphoreInstance().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * 异步请求，带有接口回调
     *
     * @param callBack 回调方法
     */
    public void async(ICallBack callBack) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    callBack.onSuccessful(call, response.body().string());
                }
            }
        });
    }

    /**
     * 为request添加请求头
     *
     * @param request 请求实例
     */
    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            try {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return 安全套接字工厂实例
     */
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    /**
     * 自定义一个接口回调
     */
    public interface ICallBack {

        void onSuccessful(Call call, String data);

        void onFailure(Call call, String errorMsg);

    }
}
