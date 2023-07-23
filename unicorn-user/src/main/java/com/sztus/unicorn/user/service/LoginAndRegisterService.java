package com.sztus.unicorn.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sztus.unicorn.lib.cache.core.SimpleRedisRepository;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.lib.core.type.NumberCode;
import com.sztus.unicorn.lib.core.type.RedisKeyType;
import com.sztus.unicorn.lib.core.util.CreatePwUtil;
import com.sztus.unicorn.lib.core.util.NumVerifyUtil;
import com.sztus.unicorn.lib.core.util.UuidUtil;
import com.sztus.unicorn.user.object.domain.Account;
import com.sztus.unicorn.user.object.domain.LimitLog;
import com.sztus.unicorn.user.object.domain.User;
import com.sztus.unicorn.user.object.domain.UserLimit;
import com.sztus.unicorn.user.repository.mapper.AccountMapper;
import com.sztus.unicorn.user.repository.mapper.LimitLogMapper;
import com.sztus.unicorn.user.repository.mapper.UserLimitMapper;
import com.sztus.unicorn.user.repository.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class LoginAndRegisterService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserLimitMapper userLimitMapper;
    @Autowired
    private LimitLogMapper limitLogMapper;
    @Autowired
    private SimpleRedisRepository simpleRedisRepository;

    @Options(useGeneratedKeys = true, keyProperty = "id")
    public JSONObject login(String email, String password) {
        // 先通过email查询来判断用户是否存在
        log.info(email.getClass().getSimpleName());
        log.info(password);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            // user表存在数据再去判断account表数据是否存在
            LambdaQueryWrapper<Account> queryWrapperAccount = new LambdaQueryWrapper<>();
            queryWrapperAccount.eq(Account::getOpenId, user.getOpenId());
            Account account = accountMapper.selectOne(queryWrapperAccount);
            LambdaQueryWrapper<UserLimit> queryWrapperLimit = new LambdaQueryWrapper<>();
            queryWrapperLimit.eq(UserLimit::getUserId, user.getId());
            UserLimit userLimit = userLimitMapper.selectOne(queryWrapperLimit);
            if (account != null) {
                // 两个数据都存在，下面进行密码判断，首先去account表里将salt拿到，然后和用户输入的password进行拼接再加密
                // 加密后的密码与数据中存的密码进行比较
                String codePassword = CreatePwUtil.hashPassword(password, account.getSalt());
                //account.getPassword() 数据库取到的密码，codePassword 加密后的密码
                if (!account.getPassword().equals(codePassword)) {
                    return JSON.parseObject(AjaxResult.failure("Password is not matched"));
                } else {
                    // 密码验证成功后用token去redis中查对应的数据,并将过期时间重置
                    String accountKey = simpleRedisRepository.generateKey(RedisKeyType.ACCOUNT, account.getToken());
                    String userLimitKey = simpleRedisRepository.generateKey(RedisKeyType.USERLIMIT, account.getToken());
                    String userKey = simpleRedisRepository.generateKey(RedisKeyType.USER, account.getToken());
                    simpleRedisRepository.set(userKey, JSON.toJSONString(user), NumberCode.EXPIRED_TIME);
                    simpleRedisRepository.set(accountKey, JSON.toJSONString(account), NumberCode.EXPIRED_TIME);
                    simpleRedisRepository.set(userLimitKey, JSON.toJSONString(userLimit), NumberCode.EXPIRED_TIME);
                    // 获得redis中最新的数据
                    String limitStrKey = simpleRedisRepository.get(userLimitKey);
                    String accStrKey = simpleRedisRepository.get(accountKey);
                    String userStrKey = simpleRedisRepository.get(userKey);
                    // 判断设置的数据是否生效，生效的话就把token和过期时间存到account表中，并返回给前端所有数据
                    if (limitStrKey != null && accStrKey != null && userStrKey != null) {
                        account.setExpiredAt(System.currentTimeMillis() + NumberCode.EXPIRED_TIME * 1000);
                        LambdaUpdateWrapper<Account> updateWrapperAccount = new LambdaUpdateWrapper<>();
                        updateWrapperAccount.eq(Account::getOpenId, account.getOpenId());
                        accountMapper.update(account, updateWrapperAccount);
                        // 用于去掉当从redis中取数据时带的斜杆 /
                        JSONObject jsonObjectAcc = (JSONObject) JSON.parse(accStrKey);
                        JSONObject jsonObjectLimit = (JSONObject) JSON.parse(limitStrKey);
                        JSONObject jsonObjectUser = (JSONObject) JSON.parse(userStrKey);
                        List<Object> list = new ArrayList<>();
                        list.add(jsonObjectAcc);
                        list.add(jsonObjectLimit);
                        list.add(jsonObjectUser);
                        return JSONObject.parseObject(AjaxResult.success(list, CodeEnum.SUCCESS.getText()));
                    } else {
                        return JSON.parseObject(AjaxResult.failure("Token not found"));
                    }
                }
            } else {
                return JSON.parseObject(AjaxResult.failure("Account not found"));
            }
        } else {
            return JSON.parseObject(AjaxResult.failure("User not exist"));
        }

    }

    // Options注解的作用是主键返回，因为注册时需要在user以外其他的表存入user_id,而这个user_id就是user表里的id
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public JSONObject register(User user) {
        Account account = new Account();
        UserLimit userLimit = new UserLimit();
        LimitLog limitLog = new LimitLog();
        // 通过email查询数据库是否有相同名字或者相同email的用户，如果有则提示已存在，没有再进行下面验证
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, user.getName()).or().eq(User::getEmail, user.getEmail());
        User existingUser = userMapper.selectOne(queryWrapper);
        String emailValidationResult = NumVerifyUtil.emailMatcher(user.getEmail());
        String passwordValidationResult = NumVerifyUtil.passwordMatcher(user.getPassword());
        String usernameValidationResult = NumVerifyUtil.usernameMatcher(user.getName());
        if (existingUser != null) {
            if (existingUser.getName().equals(user.getName())) {
                return JSON.parseObject(AjaxResult.failure("name already exists"));
            } else {
                return JSON.parseObject(AjaxResult.failure("Email already exists"));
            }
        }
        // 验证用户名格式
        else if (!usernameValidationResult.equals("success")) {
            return JSON.parseObject(usernameValidationResult);
        }
        // 验证邮箱格式
        else if (!emailValidationResult.equals("success")) {
            return JSON.parseObject(emailValidationResult);
        }
        // 验证密码格式
        else if (!passwordValidationResult.equals("success")) {
            return JSON.parseObject(passwordValidationResult);
        }
        // 验证密码和确认密码是否匹配
        else if (!user.getPassword().equals(user.getConfirmPassword())) {
            return JSON.parseObject(AjaxResult.failure("InputPassword does not match with ConfirmPassword"));
        } else {
            // 全部验证通过后，将需要的值全部set到对应的对象上，最后再存入到数据库，注意：因为其他关联表需要user_id,所以要先存user表的数据
            String salt = CreatePwUtil.generateSalt();
            account.setPassword(CreatePwUtil.hashPassword(user.getPassword(), salt));
            account.setSalt(salt);
            String uuid = UuidUtil.getUuid();
            String token = UuidUtil.getUuid();
            user.setOpenId(uuid);
            account.setOpenId(uuid);
            account.setToken(token);
            userMapper.insert(user);
            userLimit.setUserId(user.getId());
            userLimit.setLimitValue(NumberCode.FIRST_VALUE);
            limitLog.setUserId(user.getId());
            limitLog.setAmount(NumberCode.ZERO_VALUE);
            limitLog.setCreatedAt(System.currentTimeMillis());
            limitLogMapper.insert(limitLog);
            accountMapper.insert(account);
            userLimitMapper.insert(userLimit);
            return JSONObject.parseObject(AjaxResult.success());
        }
    }

    public void logout(String token) {
        String accountKey = simpleRedisRepository.generateKey(RedisKeyType.ACCOUNT, token);
        String userLimitKey = simpleRedisRepository.generateKey(RedisKeyType.USERLIMIT, token);
        String userKey = simpleRedisRepository.generateKey(RedisKeyType.USER, token);
        simpleRedisRepository.delete(accountKey);
        simpleRedisRepository.delete(userLimitKey);
        simpleRedisRepository.delete(userKey);


    }

    public JSONObject profileEdit(User user) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId, user.getId());
        User newUser = userMapper.selectOne(userLambdaQueryWrapper);
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("open_id", newUser.getOpenId());
        Account newAccount = accountMapper.selectOne(accountQueryWrapper);
        String emailValidationResult = NumVerifyUtil.emailMatcher(user.getEmail());
        String passwordValidationResult = NumVerifyUtil.passwordMatcher(user.getPassword());
        String usernameValidationResult = NumVerifyUtil.usernameMatcher(user.getName());
        if (!usernameValidationResult.equals("success")) {
            return JSON.parseObject(usernameValidationResult);
        } else {
            newUser.setName(user.getName());
        }

        if (!emailValidationResult.equals("success")) {
            return JSON.parseObject(emailValidationResult);
        } else {
            newUser.setEmail(user.getEmail());
        }
        if (!passwordValidationResult.equals("success")) {
            return JSON.parseObject(passwordValidationResult);
        } else if (!user.getPassword().equals(user.getConfirmPassword())) {
            return JSON.parseObject(AjaxResult.failure("InputPassword does not match with ConfirmPassword"));
        } else {
            String salt = CreatePwUtil.generateSalt();
            newAccount.setPassword(CreatePwUtil.hashPassword(user.getPassword(), salt));
            newAccount.setSalt(salt);
            userMapper.updateById(newUser);
            accountMapper.updateById(newAccount);
            return JSONObject.parseObject(AjaxResult.success());

        }

    }
}
