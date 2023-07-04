package com.sztus.unicorn.search;


import com.sztus.unicorn.lib.core.constant.GlobalConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

/**
 * @author Andy
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = GlobalConst.SCAN_BASE_PACKAGE)
@SpringBootApplication(scanBasePackages = GlobalConst.SCAN_BASE_PACKAGE)
public class SearchApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(SearchApplication.class, args);

    }

}
