package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class EnrollmentRepositoryTest {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void testGradeData(){
        for(int i = 0; i < 10; i++){
            List<CourseOffering> courseOfferingList = courseOfferingRepository.findAll();
            List<UserVo> userList = userRepository.findAll();

            CourseOffering courseOffering = Optional.of(courseOfferingList.get(i)).orElseGet(() -> new CourseOffering());
            UserVo userVo = Optional.of(userList.get(i)).orElseGet(() -> new UserVo());

            Enrollment enrollment = Enrollment.builder()
                    .enrollDate(LocalDateTime.now())
                    .courseOffering(courseOffering)
                    .user(userVo)
                    .build();

            enrollmentRepository.save(enrollment);
        }
    }
    @Test
    public void gradefind()
    {
        List<Enrollment> enrollmentList = enrollmentRepository.findAll();
        log.info("enrollment test : {}",enrollmentList);
    }
}
