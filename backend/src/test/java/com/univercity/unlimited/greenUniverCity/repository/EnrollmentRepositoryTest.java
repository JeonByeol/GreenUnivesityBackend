package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
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
    private EnrollmentRepository repository;

    @Autowired
    private CourseOfferingRepository offeringRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Tag("push")
    public void insertInitData(){
        // 데이터 세팅
        List<CourseOffering> offerings = offeringRepository.findAll();
        List<UserVo> users = userRepository.findAll();

        // 데이터 체크
        if(offerings.isEmpty() == true)
        {
            log.info("Offering 데이터가 없습니다.");
            return;
        }

        if(users.isEmpty() == true)
        {
            log.info("User 데이터가 없습니다.");
            return;
        }

        // 데이터 저장
        for(CourseOffering offering : offerings) {
            Enrollment enrollment = Enrollment.builder()
                    .courseOffering(offering)
                    .enrollDate(LocalDateTime.now())
                    .user(users.get((int)(Math.random()*users.size())))
                    .build();

            repository.save(enrollment);
        }
    }
}
