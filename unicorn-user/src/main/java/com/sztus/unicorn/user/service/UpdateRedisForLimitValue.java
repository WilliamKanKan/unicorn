package com.sztus.unicorn.user.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sztus.unicorn.lib.cache.core.SimpleRedisRepository;
import com.sztus.unicorn.lib.core.type.RedisKeyType;
import com.sztus.unicorn.user.object.domain.Account;
import com.sztus.unicorn.user.object.domain.User;
import com.sztus.unicorn.user.object.domain.UserLimit;
import com.sztus.unicorn.user.repository.mapper.AccountMapper;
import com.sztus.unicorn.user.repository.mapper.UserLimitMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UpdateRedisForLimitValue {
    @Autowired
    UserOperationService userOperationService;
    @Autowired
    UserLimitMapper userLimitMapper;
    @Autowired
    SimpleRedisRepository simpleRedisRepository;
    @Autowired
    AccountMapper accountMapper;
    public void updateRedisForLimitValue(User user){
        LambdaQueryWrapper<Account> queryWrapperAccount = new LambdaQueryWrapper<>();
        queryWrapperAccount.eq(Account::getOpenId, user.getOpenId());
        Account account = accountMapper.selectOne(queryWrapperAccount);
        String userLimitKey = simpleRedisRepository.generateKey(RedisKeyType.USERLIMIT,account.getToken());
        String limitStrKey = simpleRedisRepository.get(userLimitKey);
        if(limitStrKey!=null){
            LambdaQueryWrapper<UserLimit> queryWrapperLimit = new LambdaQueryWrapper<>();
            queryWrapperLimit.eq(UserLimit::getUserId, user.getId());
            UserLimit userLimit  = userLimitMapper.selectOne(queryWrapperLimit);
            Long expiredTime = simpleRedisRepository.getExpire(userLimitKey);
            log.info("过期时间" + expiredTime.toString());
            simpleRedisRepository.set(userLimitKey,JSON.toJSONString(userLimit),(account.getExpiredAt()-System.currentTimeMillis())/1000);
        }else {
            log.info("Token not found or expired");
        }

    }

}
