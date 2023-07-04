package com.sztus.unicorn.lib.core.util;

import java.security.SecureRandom;
import java.util.Base64;

public class CreatePwUtil {
    // 生成salt
    public  static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[6];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
    // 密码加密
    public static String hashPassword(String password, String salt) {
        String input = password + salt;
        return CryptUtil.md5(input);

    }
}
