package com.sztus.unicorn.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.lib.core.type.ApiResponse;
import com.sztus.unicorn.lib.core.util.AjaxResultUtil;
import com.sztus.unicorn.user.object.domain.Account;
import com.sztus.unicorn.user.object.domain.User;
import com.sztus.unicorn.user.repository.mapper.AccountMapper;
import com.sztus.unicorn.user.repository.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;

    public String getAccountInfoById(Long id) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).select("id", "open_id", "password", "salt", "token", "expired_at");
        Account account = accountMapper.selectOne(queryWrapper);

        if (account != null) {
            // 构建成功的响应
            return AjaxResult.success(account, CodeEnum.SUCCESS.getText());
        } else {
            // 构建失败的响应
            return AjaxResult.failure(null,CodeEnum.FAILURE.getText());
        }
    }

    public String getAccountInfoByUserId(Long id) {

        Account account = userMapper.selectJoinOne(Account.class, new MPJLambdaWrapper<User>()
                .select(Account::getId)
                .select(Account::getOpenId)
                .select(Account::getPassword)
                .select(Account::getSalt)
                .select(Account::getExpiredAt)
                .leftJoin(Account.class,Account::getOpenId,User::getOpenId).eq(User::getId, id));
        if (account != null) {
            // 构建成功的响应
            return AjaxResult.success(account, CodeEnum.SUCCESS.getText());
        } else {
            // 构建失败的响应
            return AjaxResult.failure(null,CodeEnum.FAILURE.getText());
        }
    }
}
