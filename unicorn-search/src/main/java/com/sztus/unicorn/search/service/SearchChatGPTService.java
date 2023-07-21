package com.sztus.unicorn.search.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.search.api.client.UserApi;
import com.sztus.unicorn.search.object.domain.SearchAnswer;
import com.sztus.unicorn.search.object.domain.SearchLog;
import com.sztus.unicorn.search.object.domain.SearchQuestion;
import com.sztus.unicorn.search.repository.mapper.SearchLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SearchChatGPTService {
    @Autowired
    UserApi userApi;
    @Autowired
    SearchLogMapper searchLogMapper;

    @Options(useGeneratedKeys = true, keyProperty = "id")
    public JSONObject talkToChatGTP(Long id, Long userId, String question) {
        LambdaQueryWrapper<SearchLog> searchLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        searchLogLambdaQueryWrapper.eq(SearchLog::getUserId, userId).and(wrapper -> wrapper.eq(SearchLog::getId, id));
        SearchLog searchLogExist = searchLogMapper.selectOne(searchLogLambdaQueryWrapper);

        String answer = "你好，我是chatGPT!";
        SearchQuestion searchQuestion = new SearchQuestion();
        SearchAnswer searchAnswer = new SearchAnswer();

        if (searchLogExist == null) {
            Long keyId = 1L;
            searchAnswer.setId(keyId);
            searchAnswer.setAnswer(answer);
            searchQuestion.setId(keyId);
            searchQuestion.setQuestion(question);
            List<SearchAnswer> searchAnswers = new ArrayList<>();
            List<SearchQuestion> searchQuestions = new ArrayList<>();
            searchAnswers.add(searchAnswer);
            searchQuestions.add(searchQuestion);
            SearchLog searchLog = new SearchLog();
            searchLog.setUserId(userId);
            searchLog.setSearchQuestions(JSON.toJSONString(searchQuestions));
            searchLog.setSearchAnswers(JSON.toJSONString(searchAnswers));
            searchLog.setCreatedAt(System.currentTimeMillis());
            searchLogMapper.insert(searchLog);
        } else {
            searchQuestion.setQuestion(question);
            searchAnswer.setAnswer(answer);
            List<SearchQuestion> existSearchQuestions = JSON.parseArray(searchLogExist.getSearchQuestions(), SearchQuestion.class);
            List<SearchAnswer> existSearchAnswers = JSON.parseArray(searchLogExist.getSearchAnswers(), SearchAnswer.class);
            existSearchQuestions.add(searchQuestion);
            existSearchAnswers.add(searchAnswer);
            searchQuestion.setId((long)existSearchQuestions.size());
            searchAnswer.setId((long)existSearchAnswers.size());
            searchLogExist.setSearchQuestions(JSON.toJSONString(existSearchQuestions));
            searchLogExist.setSearchAnswers(JSON.toJSONString(existSearchAnswers));
            searchLogMapper.updateById(searchLogExist);
        }

        userApi.cutLimitValueByUserId(userId);

        return JSON.parseObject(AjaxResult.success(searchAnswer, CodeEnum.SUCCESS.getText()));
    }
}

