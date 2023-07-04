package com.sztus.unicorn.user.controller;

import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/search")
    public String showAccount(@RequestParam Long id) {
        return userService.getAccountInfoByUserId(id);
    }
}
