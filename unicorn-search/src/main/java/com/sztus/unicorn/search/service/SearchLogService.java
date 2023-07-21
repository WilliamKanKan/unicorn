package com.sztus.unicorn.search.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.search.object.domain.SearchAnswer;
import com.sztus.unicorn.search.object.domain.SearchLog;
import com.sztus.unicorn.search.object.domain.SearchQuestion;
import com.sztus.unicorn.search.repository.mapper.SearchLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SearchLogService {
    @Autowired
    SearchLogMapper searchLogMapper;
    // 通过user_id查一个人的search记录
    public JSONObject queryQuestionByUserId(Long userId) {
        LambdaQueryWrapper<SearchLog> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
        logLambdaQueryWrapper.eq(SearchLog::getUserId, userId);
        logLambdaQueryWrapper.orderByDesc(SearchLog::getCreatedAt);
        List<SearchLog> logs = searchLogMapper.selectList(logLambdaQueryWrapper);

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (SearchLog log : logs) {
            String searchQuestionsJson = log.getSearchQuestions();
            String searchAnswersJson = log.getSearchAnswers();
            List<SearchQuestion> searchQuestions = JSON.parseArray(searchQuestionsJson, SearchQuestion.class);
            List<SearchAnswer> searchAnswers = JSON.parseArray(searchAnswersJson, SearchAnswer.class);

            Map<String, Object> entryMap = new HashMap<>();
            entryMap.put("searchQuestions", searchQuestions);
            entryMap.put("searchAnswers", searchAnswers);
            entryMap.put("userId", log.getUserId());
            entryMap.put("logId",log.getId());
            resultList.add(entryMap);
        }

        return JSONObject.parseObject(AjaxResult.success(resultList, CodeEnum.SUCCESS.getText()));
    }

    public JSONObject queryQuestionById(Long id) {
        LambdaQueryWrapper<SearchLog> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
        logLambdaQueryWrapper.eq(SearchLog::getId, id);
        logLambdaQueryWrapper.orderByDesc(SearchLog::getCreatedAt);
        SearchLog log = searchLogMapper.selectOne(logLambdaQueryWrapper);

        if (log != null) {
            String searchQuestionsJson = log.getSearchQuestions();
            String searchAnswersJson = log.getSearchAnswers();

            List<SearchQuestion> searchQuestions = JSON.parseArray(searchQuestionsJson, SearchQuestion.class);
            List<SearchAnswer> searchAnswers = JSON.parseArray(searchAnswersJson, SearchAnswer.class);
            List<Object> list = new ArrayList<>();
            list.add(searchQuestions);
            list.add(searchAnswers);
            return JSONObject.parseObject(AjaxResult.success(list, CodeEnum.SUCCESS.getText()));
        }else {
            return JSONObject.parseObject(AjaxResult.failure());
        }


    }


    public JSONObject deleteLogs(Long id, Long userId) {
        LambdaQueryWrapper<SearchLog> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (id != null) {
            logLambdaQueryWrapper.eq(SearchLog::getId, id);
        } else if (userId != null) {
            logLambdaQueryWrapper.eq(SearchLog::getUserId, userId);
        }
        int row = searchLogMapper.delete(logLambdaQueryWrapper);
        if (row > 0) {
            return JSONObject.parseObject(AjaxResult.success());
        } else {
            return JSON.parseObject(AjaxResult.failure(CodeEnum.FAILURE.getText()));
        }
    }

}