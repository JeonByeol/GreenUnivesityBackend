package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.section.service.ClassSectionService;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

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
    private ClassSectionRepository sectionRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ModelMapper mapper;

    @Test
    @Tag("push")
    @Transactional
    @Rollback(false)
    public void insertInitData() {
        log.info("========== Enrollment 더미 데이터 생성 시작 ==========");

        // 데이터 세팅
        List<ClassSection> sections = sectionRepository.findAll();
        List<User> users = userRepository.findAll();

        // 데이터 체크
        if (sections.isEmpty()) {
            log.warn("ClassSection 데이터가 없습니다.");
            return;
        }

        if (users.isEmpty()) {
            log.warn("User 데이터가 없습니다.");
            return;
        }

        log.info("조회된 분반: {}개, 사용자: {}개", sections.size(), users.size());

        int successCount = 0;
        int skipCount = 0;
        int duplicateCount = 0;

        //각 분반에 랜덤으로 학생들 배정 (중복 방지)
        for (ClassSection section : sections) {
            // 각 분반의 정원 내에서만 학생 배정
            int maxEnrollment = Math.min(section.getMaxCapacity(), users.size());
            int targetCount = (int) (maxEnrollment * (0.5 + Math.random() * 0.5)); // 정원의 50~100%

            int enrolled = 0;
            int attempts = 0;
            int maxAttempts = users.size() * 2; // 무한루프 방지

            while (enrolled < targetCount && attempts < maxAttempts) {
                attempts++;
                User randomUser = users.get((int) (Math.random() * users.size()));

                // 중복 체크: 이미 이 분반에 신청했는지 확인
                boolean alreadyEnrolled = repository.existsByUserUserIdAndClassSectionSectionId(
                        randomUser.getUserId(),
                        section.getSectionId()
                );

                if (alreadyEnrolled) {
                    duplicateCount++;
                    continue; // 이미 신청한 학생이면 스킵
                }

                // 랜덤 확률로 신청 여부 결정 (40% 확률)
                if ((int) (Math.random() * 10) >= 4) {
                    skipCount++;
                    continue;
                }

                try {
                    Enrollment enrollment = Enrollment.builder()
                            .classSection(section)
                            .enrollDate(LocalDateTime.now())
                            .user(randomUser)
                            .build();

                    repository.save(enrollment);
                    enrolled++;
                    successCount++;

                    // 처음 5개만 로그 출력
                    if (successCount <= 5) {
                        log.info("생성됨: 학생 {} → 분반 {} ({})",
                                randomUser.getEmail(),
                                section.getSectionName(),
                                section.getCourseOffering().getCourseName());
                    }

                } catch (Exception e) {
                    log.error("Enrollment 생성 실패 - userId: {}, sectionId: {}, 에러: {}",
                            randomUser.getUserId(),
                            section.getSectionId(),
                            e.getMessage());
                }
            }

            if (attempts >= maxAttempts) {
                log.warn("분반 {} 배정 시도 횟수 초과 (enrolled: {}/{})",
                        section.getSectionName(), enrolled, targetCount);
            }
        }

        log.info("========== Enrollment 더미 데이터 생성 완료 ==========");
        log.info("성공: {}건, 중복 스킵: {}건, 랜덤 스킵: {}건", successCount, duplicateCount, skipCount);
    }
//    @Test
//    @Tag("push")
//    public void insertInitData(){
//        // 데이터 세팅
//        List<ClassSection> sections=sectionRepository.findAll();
//        List<CourseOffering> offerings = offeringRepository.findAll();
//        List<User> users = userRepository.findAll();
//
//        // 데이터 체크
//        if(offerings.isEmpty() == true)
//        {
//            log.info("Offering 데이터가 없습니다.");
//            return;
//        }
//
//        if(users.isEmpty() == true)
//        {
//            log.info("User 데이터가 없습니다.");
//            return;
//        }
//
//        // 데이터 저장
//        for(ClassSection section:sections){
//            for(User user:users){
//                if((int)(Math.random()*10)>=4){
//                    continue;
//                }
//                Enrollment enrollment = Enrollment.builder()
//                        .classSection(section)
//                        .enrollDate(LocalDateTime.now())
//                        .user(user)
//                        .build();
//
//                repository.save(enrollment);
//            }
//        }

//        for(CourseOffering offering : offerings) {
//            for(User user:users){
//                if((int)(Math.random()*10)>=4){
//                    continue;
//                }
//                Enrollment enrollment = Enrollment.builder()
//                        .courseOffering(offering)
//                        .enrollDate(LocalDateTime.now())
//                        .user(user)
//                        .build();
//
//                repository.save(enrollment);
//            }
//
//        }
//    }

//    @Transactional
//    @Commit
//    @Test
//    public void insertEnrollmentData() {
//        // 데이터 세팅
//        List<ClassSection> sections=sectionRepository.findAll();
//        List<CourseOffering> offerings = offeringRepository.findAll();
//        List<User> users = userRepository.findAll();
//
//        // 데이터 체크
//        if(offerings.isEmpty() == true)
//        {
//            log.info("Offering 데이터가 없습니다.");
//            return;
//        }
//
//        if(users.isEmpty() == true)
//        {
//            log.info("User 데이터가 없습니다.");
//            return;
//        }
//
//        CourseOffering offering = offerings.get((int)(Math.random()*offerings.size()));
//        ClassSection section=sections.get((int)(Math.random()*sections.size()));
////        LegacyCourseOfferingDTO offeringDTO = mapper.map(offering, LegacyCourseOfferingDTO.class);
//        ClassSectionResponseDTO sectionDTO=mapper.map(section,ClassSectionResponseDTO.class);
//        User user = users.get((int)(Math.random()*users.size()));
//        UserDTO userDTO = mapper.map(user,UserDTO.class);
//
//        EnrollmentResponseDTO responseDTO = EnrollmentResponseDTO.builder()
//                .enrollDate(LocalDateTime.now())
//                .user(userDTO)
//                .build();
//
//        enrollmentService.addEnrollment(responseDTO);
//
//
//    }
}
