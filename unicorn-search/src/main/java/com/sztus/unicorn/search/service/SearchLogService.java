package com.sztus.unicorn.search.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
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
        QueryWrapper<SearchLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (startDate != 0 && endDate != 0) {
            queryWrapper.between("created_at", startDate, endDate);
        } else if (startDate != 0) {
            queryWrapper.ge("created_at", startDate);
        } else if (endDate != 0) {
            queryWrapper.le("created_at", endDate);
        }
        queryWrapper.orderByDesc("created_at");
        return searchLogMapper.selectList(queryWrapper);
    }

    public JSONObject deleteQuestionById(Long id){
        QueryWrapper<SearchLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        int row = searchLogMapper.delete(queryWrapper);
        if(row > 0){
            return JSON.parseObject(AjaxResult.success(CodeEnum.SUCCESS.getText()));
        }else {
            return JSON.parseObject(AjaxResult.failure(CodeEnum.FAILURE.getText()));
        }

    }
}