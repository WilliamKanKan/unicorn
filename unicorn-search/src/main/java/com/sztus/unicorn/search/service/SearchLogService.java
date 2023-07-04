package com.sztus.unicorn.search.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.lib.core.type.NumberCode;
import com.sztus.unicorn.search.object.domain.SearchLog;
import com.sztus.unicorn.search.repository.mapper.SearchLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class SearchLogService {
    @Autowired
    SearchLogMapper searchLogMapper;
    // 通过user_id查一个人的search记录
    public List<SearchLog> queryQuestionByUserIdAndDate(Long userId, Long startDate, Long endDate) {
        LambdaQueryWrapper<SearchLog> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
        logLambdaQueryWrapper.eq(SearchLog::getUserId, userId);
        logLambdaQueryWrapper.between(SearchLog::getCreatedAt, startDate, endDate);
        logLambdaQueryWrapper.orderByDesc(SearchLog::getCreatedAt);
        return searchLogMapper.selectList(logLambdaQueryWrapper);
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
            JSONObject response = JSONObject.parseObject(AjaxResult.success(CodeEnum.SUCCESS.getText()));
            response.remove("data");
            return response;
        } else {
            return JSON.parseObject(AjaxResult.failure(CodeEnum.FAILURE.getText()));
        }
    }

}