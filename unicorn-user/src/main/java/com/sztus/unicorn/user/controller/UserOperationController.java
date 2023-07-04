package com.sztus.unicorn.user.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztus.unicorn.lib.cache.core.SimpleRedisRepository;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
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
        String userLimitStr = (String) request.getAttribute("userInfoJson");
        if (userLimitStr != null) {
            JSONObject jsonObjectUser = (JSONObject) JSON.parse(userLimitStr);
            return JSONObject.parseObject(AjaxResult.success(jsonObjectUser,CodeEnum.SUCCESS.getText()));
        }
        else {
            return JSON.parseObject(AjaxResult.failure("Token not found or expired"));
        }
    }
    @GetMapping(value = "/queryable")
    public JSONObject queryValue(@RequestBody JSONObject jsonObject){
        Long userId = jsonObject.getLong("userId");
        return JSON.parseObject(userOperationService.queryLimitValueByUserId(userId));
    }

    @GetMapping(value = "/token_verify")
    public JSONObject tokenVerify(){
        JSONObject response = JSONObject.parseObject(AjaxResult.success(CodeEnum.SUCCESS.getText()));
        response.remove("data");
        return response;
    }
    @PostMapping(value = "/recharge")
    public JSONObject limitLogRecharge(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        String amount = jsonObject.getString("amount");
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
    @CrossOrigin
    @PostMapping(value = "/limit_cut")
    public JSONObject limitLogCut(@RequestParam Long userId){
        return JSONObject.parseObject(userOperationService.userLimitCut(userId));
    }


}
