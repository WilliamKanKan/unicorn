package com.sztus.unicorn.search.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztus.unicorn.lib.cache.core.SimpleRedisRepository;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.lib.core.type.RedisKeyType;
import com.sztus.unicorn.search.object.domain.SearchLog;
import com.sztus.unicorn.search.service.SearchChatGPTService;
import com.sztus.unicorn.search.service.SearchLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("search")
@Slf4j
public class TalkToChatGTPController {
    @Autowired
    SearchLogService searchLogService;
    @Autowired
    SimpleRedisRepository simpleRedisRepository;
    @Autowired
    SearchChatGPTService searchChatGPTService;
    @Autowired
    ObjectMapper newObjectMapper;

    // 查询search记录
    @PostMapping(value = "/query")
    public JSONObject queryQuestion(HttpServletRequest request, @RequestBody JSONObject date) {
        // 从请求头中获取token
        String userTokenKey = request.getHeader("tokenKey");
        // 根据token从Redis中获取用户信息
        String userKey = simpleRedisRepository.generateKey(RedisKeyType.USER, userTokenKey);
        String userInfoJson = simpleRedisRepository.get(userKey);
        Long startDate = date.getLongValue("startDate");
        Long endDate = date.getLongValue("endDate");
        if (userInfoJson != null) {
            // 用户信息存在于Redis中
            try {
                JsonNode jsonNode = newObjectMapper.readTree(userInfoJson);
                long userId = jsonNode.get("id").asLong();
                log.info(String.valueOf(userId));
                List<SearchLog> searchLogList = searchLogService.queryQuestionByUserIdAndDate(userId, startDate, endDate);
                if (searchLogList != null) {
                    return JSON.parseObject(AjaxResult.success(searchLogList, CodeEnum.SUCCESS.getText()));
                } else {
                    return JSON.parseObject(AjaxResult.failure("No data found"));
                }

            } catch (JsonProcessingException e) {
                return JSON.parseObject(AjaxResult.failure("Invalid JSON response: ")+ e.getMessage());
            }
        }else {
            // 返回token不存在或已过期的错误信息
            return JSON.parseObject(AjaxResult.failure("Token not found or expired"));

        }
    }
    @PostMapping (value = "/delete")
    public JSONObject deleteQuestion(HttpServletRequest request, @RequestBody SearchLog searchLog){
        String userTokenKey = request.getHeader("tokenKey");
        // 根据token从Redis中获取用户信息
        String userKey = simpleRedisRepository.generateKey(RedisKeyType.USER, userTokenKey);
        String userInfoJson = simpleRedisRepository.get(userKey);
        Long id = searchLog.getId();
        if(userInfoJson!=null){
            return searchLogService.deleteQuestionById(id);
        }else {
            return JSON.parseObject(AjaxResult.failure(CodeEnum.SUCCESS.getText()));
        }

    }
    @PostMapping(value = "/chat")
    public JSONObject chatMethod(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        // 从请求头中获取token
        String tokenKey = request.getHeader("tokenKey");
        // 根据token从Redis中获取用户信息
        String userLimitKey = simpleRedisRepository.generateKey(RedisKeyType.USERLIMIT, tokenKey);
        String userInfoJson = simpleRedisRepository.get(userLimitKey);
        String question = jsonObject.getString("question");
        if (userInfoJson != null) {
            // 用户信息存在于Redis中
            try {
                JsonNode jsonNode = newObjectMapper.readTree(userInfoJson);
                log.info(userInfoJson);
                long userId = jsonNode.get("userId").asLong();
                log.info(String.valueOf(userId));
                int limitValue = jsonNode.get("limitValue").asInt();
                if (limitValue > 0) {
                    if (question != null && !question.trim().isEmpty()) {
                        return searchChatGPTService.talkToChatGTP(userId, question);
                    } else {
                        return JSON.parseObject(AjaxResult.failure("You did not talk"));
                    }
                } else {
                    return JSON.parseObject(AjaxResult.failure("You need to recharge"));
                }
            } catch (JsonProcessingException e) {
                return JSON.parseObject(AjaxResult.failure("Invalid JSON response"));
            }
        } else {
            return JSON.parseObject(AjaxResult.failure("Token not found or expired"));
        }
    }
}

