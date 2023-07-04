package com.sztus.unicorn.search.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.search.api.client.UserApi;
import com.sztus.unicorn.search.object.domain.SearchLog;
import com.sztus.unicorn.search.repository.mapper.SearchLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SearchChatGPTService {
    @Autowired
    UserApi userApi;
    @Autowired
    SearchLogMapper searchLogMapper;

    public JSONObject talkToChatGTP(Long id, Long userId, String question) {
        LambdaQueryWrapper<SearchLog> searchLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        searchLogLambdaQueryWrapper.eq(SearchLog::getUserId, userId).and(wrapper -> wrapper.eq(SearchLog::getId, id));
        SearchLog searchLogExist = searchLogMapper.selectOne(searchLogLambdaQueryWrapper);

        if (searchLogExist == null) {
            Long keyId = 1L;
            Map<Long, String> conversation = new HashMap<>();
            conversation.put(keyId, question);
            String questionJson = JSON.toJSONString(conversation);
            SearchLog searchLog = new SearchLog();
            searchLog.setUserId(userId);
            searchLog.setQuestion(questionJson);
            searchLog.setCreatedAt(System.currentTimeMillis());
            searchLogMapper.insert(searchLog);
        } else {
            String questionJson = searchLogExist.getQuestion();
            Map<Long, String> conversation = JSON.parseObject(questionJson, new TypeReference<Map<Long, String>>() {
            });
            conversation.put((long) conversation.size() + 1, question);
            String updatedQuestionJson = JSON.toJSONString(conversation);
            searchLogExist.setQuestion(updatedQuestionJson);
            searchLogMapper.updateById(searchLogExist);
        }
        userApi.cutLimitValueByUserId(userId);
        String answer = "你好，我是chatGTP!";
        return JSON.parseObject(AjaxResult.success(answer, CodeEnum.SUCCESS.getText()));

    }
}

