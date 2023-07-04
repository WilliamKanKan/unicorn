package com.sztus.unicorn.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztus.unicorn.user.object.domain.User;
import com.sztus.unicorn.user.service.LoginAndRegisterService;
import com.sztus.unicorn.user.service.ResetPasswordService;
import com.sztus.unicorn.user.service.SendCodeToEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/")
public class LoginAndRegisterController {
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private LoginAndRegisterService loginAndRegisterService;
    @Autowired
    private SendCodeToEmail sendCodeToEmail;
    @Autowired
    private ResetPasswordService resetPasswordService;
    @PostMapping("/login")
    public JSONObject userLogin(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        return loginAndRegisterService.login(email,password);

    }
    @PostMapping(value = "/logout")
    public void userLogout(HttpServletRequest request){
        String token = request.getHeader("Access-Token");
        loginAndRegisterService.logout(token);
    }
    @PostMapping("/register")
    public JSONObject userRegister(@RequestBody User user) {
        return loginAndRegisterService.register(user);
    }
    @PostMapping(value = "/send_code")
    private JSONObject sendCode(@RequestBody User user){
        String name = user.getName();
        String email = user.getEmail();
//        httpSession.setAttribute("resetEmail", email);
        return sendCodeToEmail.generateCodeAndSend(name, email);
    }
    @PostMapping(value = "/reset")
    private JSONObject resetPassword(@RequestBody JSONObject jsonObject) {
//        String email = (String) httpSession.getAttribute("resetEmail");
        String verifyCode = jsonObject.getString("verifyCode");
        String password = jsonObject.getString("password");
        String confirmPassword = jsonObject.getString("confirmPassword");
        // 此时email只为postman接口用，后面会使用httpSession由上一个方法传递进来
        String email = jsonObject.getString("email");
        return resetPasswordService.resetPassword(email,password,confirmPassword,verifyCode);
    }
    @PostMapping(value = "/profile")
    private JSONObject profileEdit(@RequestBody User user){
        return loginAndRegisterService.profileEdit(user);
    }
}

