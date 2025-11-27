package com.kmo.kome;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    @Test
    public void generatePassword() {
        // 使用和你配置类里一模一样的加密器
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 生成 "123456" 的密文
        String result = encoder.encode("123456");

        System.out.println("=========================================");
        System.out.println("下面是加密后的 password:");
        System.out.println(result);
        System.out.println("=========================================");
    }
}
