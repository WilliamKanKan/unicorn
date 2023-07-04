package com.sztus.unicorn.user.service;

import com.alibaba.fastjson.JSON;
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
        QueryWrapper<Account> queryWrapperAccount = new QueryWrapper<>();
        queryWrapperAccount.eq("open_id", user.getOpenId()).select("open_id","password","token","salt","expired_at");
        Account account = accountMapper.selectOne(queryWrapperAccount);
        String userLimitKey = simpleRedisRepository.generateKey(RedisKeyType.USERLIMIT,account.getToken());
        String limitStrKey = simpleRedisRepository.get(userLimitKey);
        if(limitStrKey!=null){
            QueryWrapper<UserLimit> queryWrapperLimit = new QueryWrapper<>();
            queryWrapperLimit.eq("user_id", user.getId()).select("user_id","limit_value");
            UserLimit userLimit  = userLimitMapper.selectOne(queryWrapperLimit);
            Long expiredTime = simpleRedisRepository.getExpire(userLimitKey);
            log.info("过期时间" + expiredTime.toString());
            simpleRedisRepository.set(userLimitKey,JSON.toJSONString(userLimit),(account.getExpiredAt()-System.currentTimeMillis())/1000);
        }else {
            log.info("Token not found or expired");
        }

    }

}
