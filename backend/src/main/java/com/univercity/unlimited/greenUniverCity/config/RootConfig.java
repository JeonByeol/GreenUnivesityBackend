package com.univercity.unlimited.greenUniverCity.config;

// [추가] DTO와 Entity import
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.Grade;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// [추가] AccessLevel import
import org.modelmapper.config.Configuration.AccessLevel;

import java.time.LocalDateTime;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper(){
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setFieldMatchingEnabled(true)
                .setDeepCopyEnabled(false)
                .setFieldAccessLevel(AccessLevel.PRIVATE) // (주석 해제 및 org.modelmapper.config.Configuration 정리)
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                // 모호성 오류(ConfigurationException) 해결
                // 복잡한 관계(예: Post -> Comment, User -> Comment)에서
                // 매핑 경로가 여러 개 발견되면, 모호한 필드는 매핑을 무시(ignore)합니다.
                .setAmbiguityIgnored(true);


        // String -> List 오류(register) 해결
        // UserDTO의 'role'(String) 필드가 UserVo의 'userRoleList'(List)에
        // 자동 매핑되는 것을 방지합니다. (register 메서드용)
//        mapper.typeMap(UserDTO.class, User.class)
//                .addMappings(m -> m.skip(User::setUserRoleList));

        //Grad→ Enrollment→ CourseOffering → Course   → courseName
        mapper.typeMap(Grade.class, GradeResponseDTO.class).addMappings(m ->
                m.map(src -> src.getEnrollment().getCourseOffering().getCourse().getCourseName(),
                        GradeResponseDTO::setCourseName)
        );
//        mapper.typeMap(Grade.class, GradeDTO.class)
//                .addMappings(g->g.skip(GradeDTO::setCourseName));


        Converter<LocalDateTime, LocalDateTime> localDateTimeConverter =
                new AbstractConverter<LocalDateTime, LocalDateTime>() {
                    @Override
                    protected LocalDateTime convert(LocalDateTime source) {
                        return source;
                    }
                };

        mapper.addConverter(localDateTimeConverter);
        return mapper;
    }
}