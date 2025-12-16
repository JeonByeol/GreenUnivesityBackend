package com.univercity.unlimited.greenUniverCity.config;


import com.univercity.unlimited.greenUniverCity.security.filter.JWTCheckFIlter;
import com.univercity.unlimited.greenUniverCity.security.handler.APILoginFailHandler;
import com.univercity.unlimited.greenUniverCity.security.handler.APILoginSuccessHandler;
import com.univercity.unlimited.greenUniverCity.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class CustomSecurityConfig {

    private final JWTCheckFIlter jwtCheckFilter;   // ⭐ 필수 추가
    private final APILoginSuccessHandler apiLoginSuccessHandler;
    private final APILoginFailHandler apiLoginFailHandler;
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","HEAD"));
        config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","Cache-Control"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("=== Custom Security Config Loaded ===");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 🔥 로그인 엔드포인트를 Security 필터가 처리하도록 활성화
                .formLogin(form -> form
                        .loginProcessingUrl("/api/user/login") // 프론트 로그인 URL
                        .successHandler(apiLoginSuccessHandler) // ★ JWT 생성 포인트
                        .failureHandler(apiLoginFailHandler)
                        .permitAll()
                )

                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/register").permitAll()
                        .requestMatchers("/api/user/login").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )

                // 🔥 이제 UsernamePasswordAuthenticationFilter가 살아 있으니 정상 위치로 들어감
                .addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
