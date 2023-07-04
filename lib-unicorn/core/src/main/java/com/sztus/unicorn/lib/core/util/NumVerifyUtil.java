package com.sztus.unicorn.lib.core.util;

import com.sztus.unicorn.lib.core.type.AjaxResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumVerifyUtil {
    // 验证用户名格式方法 包含一个字母和一个数字，并且长度至少为6位 (?=.* 正向预查,表式任意位置上必须包含一个字母和数字
    public static String usernameMatcher(String name) {
        String usernameRegex = MatchFormat.USERNAME;
        Pattern usernamePattern = Pattern.compile(usernameRegex);
        Matcher usernameMatcher = usernamePattern.matcher(name);
        if (!usernameMatcher.matches()) {
            return AjaxResult.failure("Incorrect name format");
        } else {
            return "success";
        }
    }

    // 验证邮箱格式方法
    // 判断邮箱地址是否合法，[A-Za-z0-9+_.-] 可以是大小写字母或者数字或者特殊字符+_.- @ 邮箱地址标准配
    //  @后面也必须要跟至少两个数字或者字母，大小写不限，最后再解一个. 和至少两个字母，因为这是通常的域名格式
    public static String emailMatcher(String email) {
        String emailRegex = MatchFormat.EMAIL;
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()) {
            return AjaxResult.failure("Incorrect email format");
        } else {
            return "success";
        }
    }
    // 验证密码格式方法
    // 验证密码是否符合要求 必须包含字母大小写、数字和特殊符号【@#$%^&+=】)
    public static String passwordMatcher(String password) {
        String passwordRegex = MatchFormat.PASSWORD;
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        if (!passwordMatcher.matches()) {
            return AjaxResult.failure("Incorrect password format");
        } else {
            return "success";
        }
    }
    public static final class MatchFormat{
        public static final String USERNAME = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
        public static final String EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]{2,}\\.[A-Za-z]{2,}$";
        public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";

    }
}
