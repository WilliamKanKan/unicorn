package com.sztus.unicorn.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sztus.unicorn.lib.cache.core.SimpleRedisRepository;
import com.sztus.unicorn.lib.core.enumerate.CodeEnum;
import com.sztus.unicorn.lib.core.type.AjaxResult;
import com.sztus.unicorn.lib.core.type.NumberCode;
import com.sztus.unicorn.lib.core.type.RedisKeyType;
import com.sztus.unicorn.user.object.domain.User;
import com.sztus.unicorn.user.repository.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class SendCodeToEmail {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SimpleRedisRepository simpleRedisRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    public JSONObject generateCodeAndSend(String name, String email){
        QueryWrapper<User> queryWrapperUser = new QueryWrapper<>();
        queryWrapperUser.eq("name", name).select("id","name","email");
        User user = userMapper.selectOne(queryWrapperUser);
        if(user != null){
            if(user.getEmail().equals(email)){
                // 暂时用email来做生成key的token
                String verifyCodeKey = simpleRedisRepository.generateKey(RedisKeyType.VERIFYCODE,email);
                String code = simpleRedisRepository.get(verifyCodeKey);
                if(code == null){
                 String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
                 simpleRedisRepository.set(verifyCodeKey, verifyCode,NumberCode.TIMEOUT);
                 // 发送email的方法
               sendEmail(email, "Verification Code", "Your verification code is: " + verifyCode + ",5 minutes expired!");
                    return JSON.parseObject(AjaxResult.success("null",CodeEnum.SUCCESS.getText()));
                }else {
                    return JSON.parseObject(AjaxResult.failure("Code exist"));
                }
            }else {
                return JSON.parseObject(AjaxResult.failure("Email is not matched"));
            }
        }else {
            return JSON.parseObject(AjaxResult.failure("User not exist"));
        }

    }
    public void sendEmail(String recipientEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(NumberCode.EMAIL_FROM);
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}
