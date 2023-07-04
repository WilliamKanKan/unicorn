package com.sztus.unicorn.user.controller;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.lib.core.type.ApiResponse;
import com.sztus.unicorn.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Resource
    private UserService userService;

    @PostMapping("/search")
    public String showAccount(@RequestParam Long id) {
        return userService.getAccountInfoById(id);
    }
}
