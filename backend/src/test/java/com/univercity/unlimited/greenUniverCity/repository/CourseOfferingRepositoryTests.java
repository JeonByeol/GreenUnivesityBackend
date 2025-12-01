package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.LegacyCourseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.repository.CourseRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.LegacyCourseOfferingDTO;
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

        if (courseList.isEmpty()) {
            log.info("코스 데이터가 없습니다.");
            return;
        }

        // ⭐ 교수 또는 ADMIN만 필터링
        List<User> professorList = userRepository.findAll().stream()
                .filter(user -> (user.getUserRole().equals(UserRole.PROFESSOR) || user.getUserRole().equals(UserRole.ADMIN)))
                .collect(Collectors.toList());

        if (professorList.isEmpty()) {
            log.info("교수 데이터가 없습니다.");
            return;
        }

        for (Course course : courseList) {
            int semester = 1;
            char alphabet = 'A';

            // ⭐ 교수 리스트에서 랜덤 선택
            User professor = professorList.get((int)(Math.random() * professorList.size()));

            CourseOffering courseOffering = CourseOffering.builder()
                    .courseName(course.getCourseName() + alphabet++)
                    .year(2025)
                    .semester(semester)
                    .course(course)
                    .professor(professor) // 이미 교수임이 보장됨
                    .build();

            repository.save(courseOffering);
        }
    }

    
    // 데이터 한 개 추가
    @Transactional
    @Commit
    @Test
    public void insertCourseOfferingData() {
        // 데이터 세팅
        List<CourseOffering> courseOfferings = repository.findAll();
        List<User> userList = userRepository.findAll();
        List<Course> courseList = courseRepository.findAll();

        if(courseOfferings.isEmpty() == true) {
            log.info("코스 데이터가 없습니다.");
            return;
        }

        if(userList.isEmpty() == true) {
            log.info("유저 데이터가 없습니다.");
            return;
        }

        if(courseList.isEmpty() == true) {
            log.info("코스 데이터가 없습니다.");
            return;
        }

        // 교수 이름 세팅
        List<String> professorNames = new ArrayList<>();
        for(User user : userList) {
            if((user.getUserRole().equals(UserRole.PROFESSOR) || user.getUserRole().equals(UserRole.ADMIN))){
                professorNames.add(user.getNickname());
            }
        }

        // 기본 데이터 세팅
        String professorName = professorNames.get((int)(Math.random()*professorNames.size()));
        int semester = (int) (Math.random() * 2) + 1;
        Course course = courseList.get((int)(Math.random()*courseList.size()));
        LegacyCourseDTO legacyCourseDTO = mapper.map(course, LegacyCourseDTO.class);
        LegacyCourseOfferingDTO legacyCourseOfferingDTO = LegacyCourseOfferingDTO.builder()
                .professorName(professorName)
                .year(2025)
                .semester(semester)
                .course(legacyCourseDTO)
                .build();

        char alphabat = 'A';
        int cnt = 0;
        int maxCnt = courseOfferings.size();
        String courseName = "EMPTY";
        boolean isContain = false;
        while (alphabat != 'Z'){
            // 체크 하면서 중복이면 break로 탈출
            for(CourseOffering courseOffering : courseOfferings){
                if(courseOffering.getCourseName().equals(course.getCourseName() + alphabat)) {
                    isContain = true;
                    break;
                }
            }

            // 중복이 있을경우 처리
            if(isContain == true) {
                isContain = false;
                alphabat++;

                if(alphabat == 'Z') {
                    log.info("조합을 찾을 수 없습니다.");
                    break;
                }
            } else {
                // 중복이 없으면 처리 완료
                legacyCourseOfferingDTO.setCourseName(course.getCourseName() + alphabat);
                service.addCourseOffering(legacyCourseOfferingDTO);
                break;
            }
        }
    }

    // 데이터 여러개 추가
    // 즉 중복 확인
    @Transactional
    @Commit
    @Test
    public void insertManyCourseOfferingData() {
        for(int i=0; i<5; i++) {
            // 데이터 세팅
            List<CourseOffering> courseOfferings = repository.findAll();
            List<User> userList = userRepository.findAll();
            List<Course> courseList = courseRepository.findAll();

            if(courseOfferings.isEmpty() == true) {
                log.info("코스 데이터가 없습니다.");
                return;
            }

            if(userList.isEmpty() == true) {
                log.info("유저 데이터가 없습니다.");
                return;
            }

            if(courseList.isEmpty() == true) {
                log.info("코스 데이터가 없습니다.");
                return;
            }

            // 교수 이름 세팅
            List<String> professorNames = new ArrayList<>();
            for(User user : userList) {
                if((user.getUserRole().equals(UserRole.PROFESSOR) || user.getUserRole().equals(UserRole.ADMIN))){
                    professorNames.add(user.getNickname());
                }
            }

            // 기본 데이터 세팅
            String professorName = professorNames.get((int)(Math.random()*professorNames.size()));
            int semester = (int) (Math.random() * 2) + 1;
            Course course = courseList.get((int)(Math.random()*courseList.size()));
            LegacyCourseDTO legacyCourseDTO = mapper.map(course, LegacyCourseDTO.class);
            LegacyCourseOfferingDTO legacyCourseOfferingDTO = LegacyCourseOfferingDTO.builder()
                    .professorName(professorName)
                    .year(2025)
                    .semester(semester)
                    .course(legacyCourseDTO)
                    .build();

            char alphabat = 'A';
            int cnt = 0;
            int maxCnt = courseOfferings.size();
            String courseName = "EMPTY";
            boolean isContain = false;
            while (alphabat != 'Z'){
                // 체크 하면서 중복이면 break로 탈출
                for(CourseOffering courseOffering : courseOfferings){
                    if(courseOffering.getCourseName().equals(course.getCourseName() + alphabat)) {
                        isContain = true;
                        break;
                    }
                }

                // 중복이 있을경우 처리
                if(isContain == true) {
                    isContain = false;
                    alphabat++;

                    if(alphabat == 'Z') {
                        log.info("조합을 찾을 수 없습니다.");
                        break;
                    }
                } else {
                    // 중복이 없으면 처리 완료
                    legacyCourseOfferingDTO.setCourseName(course.getCourseName() + alphabat);
                    UserDTO userDTO = mapper.map(userList.get((int)(Math.random()*userList.size())),UserDTO.class);
                    legacyCourseOfferingDTO.setUser(userDTO);
                    service.addCourseOffering(legacyCourseOfferingDTO);
                    break;
                }
            }
        }
    }
}
