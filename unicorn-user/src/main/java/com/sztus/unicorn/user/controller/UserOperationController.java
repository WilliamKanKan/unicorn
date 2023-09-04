package com.sztus.unicorn.user.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztus.unicorn.lib.cache.core.SimpleRedisRepository;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.user.object.domain.UserLimit;
import com.sztus.unicorn.user.service.UserOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserOperationController {
    @Autowired
    private UserOperationService userOperationService;
    @Autowired
    private SimpleRedisRepository simpleRedisRepository;
    @Autowired
    ObjectMapper newObjectMapper;
    @GetMapping(value = "/query")
    public JSONObject queryLimitValue(HttpServletRequest request){
        String userInfoJson = (String) request.getAttribute("userInfoJson");
        return JSONObject.parseObject(AjaxResult.success(JSON.parseObject(userInfoJson,UserLimit.class), CodeEnum.SUCCESS.getText()));

    }
    @GetMapping(value = "/token_verify")
    public JSONObject tokenVerify(){
        return JSONObject.parseObject(AjaxResult.success());
    }
     @GetMapping(value = "/token_verify")
    public JSONObject tokenVerify(){
        return JSONObject.parseObject(AjaxResult.success());
    }
    @PostMapping(value = "/recharge")
    public JSONObject limitLogRecharge(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        String userInfoJson = (String) request.getAttribute("userInfoJson");
        String amount = jsonObject.getString("amount");
        if (userInfoJson != null) {
            try {
                JsonNode jsonNode = newObjectMapper.readTree(userInfoJson);
                long userId = jsonNode.get("id").asLong();
                // 判断前端传入的amount否为Integer类型，如果不是就抛异常并返回输入有误
                try {
                    int amountValue = Integer.parseInt(amount);
                    if (amountValue > 0) {
                        return JSONObject.parseObject(userOperationService.limitLogRecharge(userId, amountValue));
                    } else {
                        return JSONObject.parseObject(AjaxResult.failure("Input incorrect"));
                    }
                } catch (NumberFormatException e) {
                    return JSONObject.parseObject(AjaxResult.failure("Input incorrect"));
                }


            } catch (JsonProcessingException e) {
                return JSON.parseObject(AjaxResult.failure("Invalid JSON response: ") + e.getMessage());
            }
        } else {
            // 返回token不存在或已过期的错误信息
            return JSON.parseObject(AjaxResult.failure("Token not found or expired"));

        }
    }
    @PostMapping(value = "/limit_cut")
    public JSONObject limitLogCut(@RequestParam Long userId){
        return JSONObject.parseObject(userOperationService.userLimitCut(userId));
    }


}
