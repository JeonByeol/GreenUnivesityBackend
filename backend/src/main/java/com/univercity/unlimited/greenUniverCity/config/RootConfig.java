package com.univercity.unlimited.greenUniverCity.config;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true)
                .setDeepCopyEnabled(false)
//                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);



        Converter<LocalDateTime, LocalDateTime> localDateTimeConverter =
                new AbstractConverter<LocalDateTime, LocalDateTime>() {
                    @Override
                    protected LocalDateTime convert(LocalDateTime source) {
                        // 원본 객체를 그대로 반환
                        return source;
                    }
                };

        mapper.addConverter(localDateTimeConverter);
        return mapper;
    }
}
