package com.sztus.unicorn.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.user.object.domain.LimitLog;
import com.sztus.unicorn.user.object.domain.User;
import com.sztus.unicorn.user.object.domain.UserLimit;
import com.sztus.unicorn.user.repository.mapper.LimitLogMapper;
import com.sztus.unicorn.user.repository.mapper.UserLimitMapper;
import com.sztus.unicorn.user.repository.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class UserOperationService {
    @Autowired
    private UserLimitMapper userLimitMapper;
    @Autowired
    private LimitLogMapper limitLogMapper;
    @Autowired
    UpdateRedisForLimitValue updateRedisForLimitValue;
    @Autowired
    UserMapper userMapper;
    // 将充值和消费统一在一个方法里，通过传入不同的参数再定义两个不同的方法，然后将得到的数据用主方法返回
    private String updateLimit(Long userId, Integer amount) {
        LambdaQueryWrapper<LimitLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LimitLog::getUserId, userId);
        LambdaQueryWrapper<UserLimit> queryWrapperUserLimit = new LambdaQueryWrapper<>();
        queryWrapperUserLimit.eq(UserLimit::getUserId, userId);
        LimitLog limitLog = limitLogMapper.selectOne(queryWrapper);
        UserLimit userLimit = userLimitMapper.selectOne(queryWrapperUserLimit);
        LambdaQueryWrapper<User> queryWrapperForUser = new LambdaQueryWrapper<>();
        queryWrapperForUser.eq(User::getId,userId);
        User user = userMapper.selectOne(queryWrapperForUser);
        if (limitLog != null && userLimit != null) {
            if (amount > 0) {
                limitLog.setAmount(limitLog.getAmount() + amount);
                limitLog.setCreatedAt(System.currentTimeMillis());
                userLimit.setLimitValue(userLimit.getLimitValue() + amount);
                return getString(limitLog, userLimit,user);
                // 当amount为0，limit_log表里的amount大于0时，执行两张表的减1操作，目的不能使amount减成负数，
                // 而user_limit的减1操作前会判断它是否大于0，如果等于0，不会执行search操作，也就是会执行减1操作。
            } else if (amount == 0 && limitLog.getAmount() > 0) {
                limitLog.setAmount(limitLog.getAmount() -1);
                userLimit.setLimitValue(userLimit.getLimitValue() -1);
                return getString(limitLog, userLimit,user);
            }
            // 当amount和limit_log表里的amount都为0时，只执行user_limit表减1的操作
            else if (amount == 0 && limitLog.getAmount() == 0) {
                userLimit.setLimitValue(userLimit.getLimitValue() -1);
                LambdaUpdateWrapper<UserLimit> updateWrapperUserLimit = new LambdaUpdateWrapper<>();
                updateWrapperUserLimit.eq(UserLimit::getId, userLimit.getId());
                int rowsAffectedValue = userLimitMapper.update(userLimit, updateWrapperUserLimit);
                if (rowsAffectedValue > 0) {
                    updateRedisForLimitValue.updateRedisForLimitValue(user);
                    return AjaxResult.success(userLimit,CodeEnum.SUCCESS.getText());
                } else {
                    return AjaxResult.failure(CodeEnum.FAILURE.getText());
                }
            }
        }

        {
            return AjaxResult.failure("No account found");
        }

    }
// 将更新操作封装在一个方法里
    @NotNull
    private String getString(LimitLog limitLog, UserLimit userLimit,User user) {
        LambdaUpdateWrapper<LimitLog> updateWrapper = new LambdaUpdateWrapper<>();
        LambdaUpdateWrapper<UserLimit> updateWrapperUserLimit = new LambdaUpdateWrapper<>();
        updateWrapper.eq(LimitLog::getId, limitLog.getId());
        updateWrapperUserLimit.eq(UserLimit::getId, userLimit.getId());
        int rowsAffectedAmount = limitLogMapper.update(limitLog, updateWrapper);
        int rowsAffectedValue = userLimitMapper.update(userLimit, updateWrapperUserLimit);
        if (rowsAffectedAmount > 0 && rowsAffectedValue > 0) {
            updateRedisForLimitValue.updateRedisForLimitValue(user);
            return AjaxResult.success(userLimit,CodeEnum.SUCCESS.getText());
        } else {
            return AjaxResult.failure(CodeEnum.FAILURE.getText());
        }
    }

    public String limitLogRecharge(Long userId, Integer amount) {
        return updateLimit(userId, amount);
    }
    public String userLimitCut(Long userId) {
        return updateLimit(userId, 0);
    }
    public String queryLimitValueByUserId(Long userId){
        LambdaQueryWrapper<UserLimit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLimit::getUserId, userId);
        UserLimit userLimit = userLimitMapper.selectOne(queryWrapper);
        log.info(userLimit.toString());
        return AjaxResult.success(userLimit,CodeEnum.SUCCESS.getText());
    }
}
