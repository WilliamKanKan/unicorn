package com.sztus.unicorn.user.api.client.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "openapi-pre-origination")
public interface CustomerApi {

    @PostMapping("/application/create")
    String creatApplication(@RequestBody String request);

}
