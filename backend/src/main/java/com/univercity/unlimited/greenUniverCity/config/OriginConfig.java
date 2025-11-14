package com.univercity.unlimited.greenUniverCity.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;


@Configuration
@Slf4j
@RequiredArgsConstructor
//public class OriginConfig {
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // 1. "http://localhost:3000" (React)의 요청을 허용
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//
//        // 2. Preflight 요청을 위해 "OPTIONS" 메서드 포함
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//
//        // 3. 허용할 헤더 지정
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
//
//        // 4. (중요) 쿠키/인증 정보를 주고받기 위해 true로 설정
//        configuration.setAllowCredentials(true);
//
//        // 5. Preflight 캐시 시간
//        configuration.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        // "/api/**" 경로에 대해 위 설정을 적용
//        source.registerCorsConfiguration("/api/**", configuration);
//        return source;
//    }
//}
public class OriginConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With")
                .allowCredentials(true)
                .maxAge(3600);
    }
}


