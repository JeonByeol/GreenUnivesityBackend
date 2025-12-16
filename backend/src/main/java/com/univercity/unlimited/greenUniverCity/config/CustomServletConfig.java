package com.univercity.unlimited.greenUniverCity.config;


import com.univercity.unlimited.greenUniverCity.util.LocalDateFormater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormater());
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("*")
//                .allowedMethods("HEAD","GET","POST","PUT","DELETE","OPTIONS")
//                .maxAge(300)
//                .allowedHeaders("Authorization","Cache-Control","Content-Type");
//    }
//@Bean
//public PasswordEncoder passwordEncoder(){
//    return  new BCryptPasswordEncoder();//비밀번호 암호화 ,단방향 암호화
//}

}
