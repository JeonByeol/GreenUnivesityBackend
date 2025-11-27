package com.univercity.unlimited.greenUniverCity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
    //Password를 Encoder를 활용하여 암호화 시키기 위해 사용하는 Config입니다
    // -> 현재 테스트/공부중으로 진행중인 상태 사용은x 단방향 암호화

    //비밀번호 String password를 받으면 passwordEncoder를 통하여
    //매번 토큰 형식으로 자동변환 시켜줌
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
