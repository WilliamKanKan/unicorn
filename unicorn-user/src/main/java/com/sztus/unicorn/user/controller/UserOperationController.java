package com.sztus.unicorn.user.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztus.unicorn.lib.cache.core.SimpleRedisRepository;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.lib.core.type.RedisKeyType;
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
    // redis中查询余额
    @GetMapping(value = "/query")
    public JSONObject queryLimitValue(HttpServletRequest request){
        String tokenKey = request.getHeader("tokenKey");
        // 根据token从Redis中获取用户信息
        String userLimitKey = simpleRedisRepository.generateKey(RedisKeyType.USERLIMIT, tokenKey);
        String userLimitStr = simpleRedisRepository.get(userLimitKey);
        if (userLimitStr != null) {
            JSONObject jsonObjectUser = (JSONObject) JSON.parse(userLimitStr);
            return JSONObject.parseObject(AjaxResult.success(jsonObjectUser,CodeEnum.SUCCESS.getText()));
        }
        else {
            return JSON.parseObject(AjaxResult.failure("Token not found or expired"));
        }
    }
    @GetMapping(value = "/queryable")
    public JSONObject queryValue(Long userId){
        return JSON.parseObject(userOperationService.queryLimitValueByUserId(userId));
    }
    @GetMapping(value = "/token_verify")
    public JSONObject tokenVerify(HttpServletRequest request){
        String tokenKey = request.getHeader("tokenKey");
        // 根据token从Redis中获取用户信息
        String userKey = simpleRedisRepository.generateKey(RedisKeyType.USER, tokenKey);
        String userInfo = simpleRedisRepository.get(userKey);
        if(userInfo != null){
            JSONObject jsonObjectUser = (JSONObject) JSON.parse(userInfo);
          return JSONObject.parseObject(AjaxResult.success(jsonObjectUser,CodeEnum.SUCCESS.getText()));
        }
        else {
            return JSON.parseObject(AjaxResult.failure("Token not found or expired"));
        }

    }
    @PostMapping(value = "/recharge")
    public JSONObject limitLogRecharge(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        String tokenKey = request.getHeader("tokenKey");
        // 根据token从Redis中获取用户信息
        String userLimitKey = simpleRedisRepository.generateKey(RedisKeyType.USERLIMIT, tokenKey);
        String userInfoJson = simpleRedisRepository.get(userLimitKey);
        String amount = jsonObject.getString("amount");
        if (userInfoJson != null) {
            // 用户信息存在于Redis中,将它取出
            try {
                JsonNode jsonNode = newObjectMapper.readTree(userInfoJson);
                Long userId = jsonNode.get("userId").asLong();
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


            }
              catch (JsonProcessingException e) {
                    return JSON.parseObject(AjaxResult.failure("Invalid JSON response: ")+ e.getMessage());
                }
            }else {
                // 返回token不存在或已过期的错误信息
                return JSON.parseObject(AjaxResult.failure("Token not found or expired"));

            }

    }
    @PostMapping(value = "/limit_cut")
    public JSONObject limitLogCut(@RequestParam Long userId){
        return JSONObject.parseObject(userOperationService.userLimitCut(userId));
    }


}
