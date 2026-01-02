package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.entity.AcademicTerm;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.respository.AcademicTermRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.repository.CourseRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class CourseOfferingRepositoryTests {
    @Autowired
    private CourseOfferingRepository repository;

    @Autowired
    private CourseOfferingService service;

    @Autowired
    private AcademicTermRepository academicTermRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;



    @Test
    @Transactional
    @Commit
    @Tag("push")
    public void insertInitData() {
        List<Course> courseList = courseRepository.findAll();
        List<AcademicTerm> academicTermList = academicTermRepository.findAll();

        if (courseList.isEmpty()) {
            log.info("코스 데이터가 없습니다.");
            return;
        }

        if (academicTermList.isEmpty()) {
            log.info("학기 데이터가 없습니다.");
            return;
        }

        // 교수 또는 ADMIN만 필터링
        List<User> professorList = userRepository.findAll().stream()
                .filter(user -> (user.getUserRole().equals(UserRole.PROFESSOR) || user.getUserRole().equals(UserRole.ADMIN)))
                .collect(Collectors.toList());

        if (professorList.isEmpty()) {
            log.info("교수 데이터가 없습니다.");
            return;
        }

        for (Course course : courseList) {
            int count = (int) (Math.random() * 6); // 0~5개 랜덤 생성

            for (int i = 0; i < count; i++) {
                char alphabet = (char) ('A' + i); // 0~5개면 충분
                User professor = professorList.get((int)(Math.random() * professorList.size()));
                AcademicTerm academicTerm = academicTermList.get((int)(Math.random() * academicTermList.size()));

                CourseOffering courseOffering = CourseOffering.builder()
                        .courseName(course.getCourseName() + alphabet)
                        .course(course)
                        .professor(professor)
                        .academicTerm(academicTerm)
                        .build();

                repository.save(courseOffering);
            }
        }
    }
}
