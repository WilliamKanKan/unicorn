package com.sztus.unicorn.search.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value ="unicorn-user")
public interface UserApi {

    @GetMapping("/user/query")
    String queryLimitByUserId(@RequestParam("userId") Long userId);
    @PostMapping(value = "/user/limit_cut")
    String cutLimitValueByUserId(@RequestParam("userId") Long userId);
}

