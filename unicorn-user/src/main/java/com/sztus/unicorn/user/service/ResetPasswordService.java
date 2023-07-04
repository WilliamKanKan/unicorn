package com.sztus.unicorn.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.unicorn.lib.cache.core.SimpleRedisRepository;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.lib.core.type.RedisKeyType;
import com.sztus.unicorn.lib.core.util.CreatePwUtil;
import com.sztus.unicorn.lib.core.util.NumVerifyUtil;
import com.sztus.unicorn.user.object.domain.Account;
import com.sztus.unicorn.user.object.domain.User;
import com.sztus.unicorn.user.repository.mapper.AccountMapper;
import com.sztus.unicorn.user.repository.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ResetPasswordService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    SimpleRedisRepository simpleRedisRepository;
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public JSONObject resetPassword(String email, String password, String confirmPassword, String verifyCode) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User existUser = userMapper.selectOne(queryWrapper);
        String passwordValidationResult = NumVerifyUtil.passwordMatcher(password);
        if (existUser != null) {
            if(existUser.getEmail().equals(email)){
                LambdaQueryWrapper<Account> queryWrapperAccount = new LambdaQueryWrapper<>();
                queryWrapperAccount.eq(Account::getOpenId, existUser.getOpenId());
                Account account = accountMapper.selectOne(queryWrapperAccount);
                if (account != null) {
                    if (!passwordValidationResult.equals("success")) {
                        return JSON.parseObject(passwordValidationResult);
                    }  if (!password.equals(confirmPassword)){
                        return JSON.parseObject(AjaxResult.failure("InputPassword does not match with ConfirmPassword"));
                    }
                    // 这里的code要去redis中取，取到后和用户输入的作判断
                    String verifyCodeKey = simpleRedisRepository.generateKey(RedisKeyType.VERIFYCODE,email);
                    String code = simpleRedisRepository.get(verifyCodeKey);
                    if (code != null) {
                       if(verifyCode.equals(code)){
                           String salt = CreatePwUtil.generateSalt();
                           String newPassword = CreatePwUtil.hashPassword(password, salt);
                           account.setPassword(newPassword);
                           account.setSalt(salt);
                           accountMapper.updateById(account);
                           simpleRedisRepository.delete(verifyCodeKey);
                           return JSON.parseObject(AjaxResult.success("null",CodeEnum.SUCCESS.getText()));
                       }
                       else {
                           return JSON.parseObject(AjaxResult.failure("Code is incorrect"));
                       }
                    } else {
                        return JSON.parseObject(AjaxResult.failure("Code not exist"));
                    }
                } else {
                    return JSON.parseObject(AjaxResult.failure("Account not exist"));
                }
            }
            else {
                return JSON.parseObject(AjaxResult.failure("Email not exist"));
            }

        } else {
            return JSON.parseObject(AjaxResult.failure("User not exist"));
        }

    }
}
