package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
public class EnrollmentRepositoryTest {
    @Autowired
    private EnrollmentRepository repository;

    @Autowired
    private CourseOfferingRepository offeringRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ModelMapper mapper;

    @Test
    @Tag("push")
    public void insertInitData(){
        // 데이터 세팅
        List<CourseOffering> offerings = offeringRepository.findAll();
        List<User> users = userRepository.findAll();

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
                    .user(offering.getUser())
                    .build();

            repository.save(enrollment);
        }
    }

    @Transactional
    @Commit
    @Test
    public void insertEnrollmentData() {
        // 데이터 세팅
        List<CourseOffering> offerings = offeringRepository.findAll();
        List<User> users = userRepository.findAll();

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

        CourseOffering offering = offerings.get((int)(Math.random()*offerings.size()));
        CourseOfferingDTO offeringDTO = mapper.map(offering,CourseOfferingDTO.class);
        User user = users.get((int)(Math.random()*users.size()));
        UserDTO userDTO = mapper.map(user,UserDTO.class);

        EnrollmentDTO enrollmentDTO = EnrollmentDTO.builder()
                .courseOffering(offeringDTO)
                .enrollDate(LocalDateTime.now())
                .user(userDTO)
                .build();

        enrollmentService.addEnrollment(enrollmentDTO);


    }
}
