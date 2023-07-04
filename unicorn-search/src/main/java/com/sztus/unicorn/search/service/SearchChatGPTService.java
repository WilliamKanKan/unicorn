package com.sztus.unicorn.search.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.search.api.client.UserApi;
import com.sztus.unicorn.search.object.domain.SearchLog;
import com.sztus.unicorn.search.repository.mapper.SearchLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchChatGPTService {
    @Autowired
    UserApi userApi;
    @Autowired
    SearchLogMapper searchLogMapper;

    public JSONObject talkToChatGTP(Long userId, String question) {
            SearchLog searchLog = new SearchLog();
            searchLog.setUserId(userId);
            searchLog.setQuestion(question);
            searchLog.setCreatedAt(System.currentTimeMillis());
            searchLogMapper.insert(searchLog);
            userApi.cutLimitValueByUserId(userId);
            String answer = "你好，我是chatGTP!";
            return JSON.parseObject(AjaxResult.success(answer,CodeEnum.SUCCESS.getText()));

        }
    }

