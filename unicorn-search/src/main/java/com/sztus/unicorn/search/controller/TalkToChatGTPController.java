package com.sztus.unicorn.search.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztus.unicorn.lib.cache.core.SimpleRedisRepository;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.lib.core.type.NumberCode;
import com.sztus.unicorn.lib.core.type.RedisKeyType;
import com.sztus.unicorn.search.object.domain.SearchLog;
import com.sztus.unicorn.search.service.SearchChatGPTService;
import com.sztus.unicorn.search.service.SearchLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@CrossOrigin
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
    @GetMapping(value = "/query")
    public JSONObject queryQuestion(HttpServletRequest request) {
        String userInfoJson = (String) request.getAttribute("userInfoJson");
            try {
                JsonNode jsonNode = newObjectMapper.readTree(userInfoJson);
                long userId = jsonNode.get("userId").asLong();
                List<SearchLog> searchLogList = searchLogService.queryQuestionByUserId(userId);
                if (searchLogList != null && !searchLogList.isEmpty()) {
                    return JSON.parseObject(AjaxResult.success(searchLogList, CodeEnum.SUCCESS.getText()));
                } else {
                    return JSON.parseObject(AjaxResult.failure("No data found"));
                }
            }
            catch (JsonProcessingException e) {
                return JSON.parseObject(AjaxResult.failure("Invalid JSON response"));
            }

    }
    @GetMapping(value = "/query_by_id")
    public JSONObject queryQuestionById(@RequestParam Long id){
        return  searchLogService.queryQuestionById(id);
    }

    @PostMapping(value = "/delete")
    public JSONObject deleteQuestion(@RequestBody SearchLog searchLog) {
        Long id = searchLog.getId();
        Long userId = searchLog.getUserId();
            return searchLogService.deleteLogs(id,userId);
        }
    @PostMapping(value = "/chat")
    public JSONObject chatMethod(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        // 从请求属性中获取userInfoJson
        String userInfoJson = (String) request.getAttribute("userInfoJson");
        String question = jsonObject.getString("question");
        try {
            JsonNode jsonNode = newObjectMapper.readTree(userInfoJson);
            long userId = jsonNode.get("userId").asLong();
            int limitValue = jsonNode.get("limitValue").asInt();
            Long id = jsonObject.getLong("id");
            if (limitValue > 0) {
                if (question != null && !question.trim().isEmpty()) {
                    return searchChatGPTService.talkToChatGTP(id,userId, question);
                } else {
                    return JSON.parseObject(AjaxResult.failure("You did not talk"));
                }
            } else {
                return JSON.parseObject(AjaxResult.failure("You need to recharge"));
            }
        } catch (JsonProcessingException e) {
            return JSON.parseObject(AjaxResult.failure("Invalid JSON response"));
        }
    }
}


