package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class CCPRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Transactional
    @Commit
    @Test
    public void testInsert(){
        var departmentArray = List.of("컴퓨터","정보","전기");
        var findUser = userRepository.findAll();

        if(findUser.isEmpty()){
            log.info("유저가 없습니다. 유저를 추가해주세요.");
            return;
        }

        for(int i = 0; i < 10; i++){
            Department department = Department.builder()
                    .deptName(departmentArray.get(i%3))
                    .build();

            Course course = Course.builder()
                    .courseName("cou"+i)
                    .description("dec")
                    .credits(i)
                    .department(department)
                    .build();

            UserVo user = null;
            if(findUser.size() > i) {
                user = findUser.get(i);
            }

            List<CourseOffering> courseOfferingList = new ArrayList<>();
            for(int j=0; j<5; j++) {
                CourseOffering courseOffering = CourseOffering.builder()
                        .professorId("pro"+j)
                        .year(LocalDateTime.now().getYear())
                        .user(user)
                        .semester(j%2)

                        .course(course)
                        .build();

                courseOfferingList.add(courseOffering);
            }



            for(CourseOffering offering : courseOfferingList){
                course.addCourseOffering(offering);
                if(user == null)
                    continue;

                user.addOffering(offering);
            }

            departmentRepository.save(department);
            courseRepository.save(course);

            if(user != null)
                userRepository.save(user);

            for(CourseOffering offering : courseOfferingList){
                courseOfferingRepository.save(offering);
            }
        }
    }

    @Test
    public void findTest()
    {
        List<CourseOffering> courseOfferingList = courseOfferingRepository.findAll();
        log.info("courseOffering test : {}",courseOfferingList);

        List<Course> courseList = courseRepository.findAll();
        log.info("course test : {}",courseList);

        List<Department> departmentList = departmentRepository.findAll();
        log.info("departmentList test : {}",departmentList);
    }

    @Transactional
    @Test
    public void find1() { // userRepository
        List<CourseOffering> allOfferings = courseOfferingRepository.findAll();
        if (allOfferings.isEmpty()) {
            log.warn("⚠️ CourseOffering 데이터가 없습니다.");
            return;
        }

        Long offeringId = allOfferings.get(0).getOfferingId();
        Optional<UserVo> userVoOptional =  userRepository.findByOfferingId(offeringId);
        if(userVoOptional.isPresent()) {
            log.info("➡️ 개설 ID: {}, CourseOffering {}", userVoOptional.get(), userVoOptional.get().getOfferings());
        }
    }

    @Transactional
    @Test
    public void find1_2() { // userRepository
        List<Enrollment> allEnrollments = enrollmentRepository.findAll();
        if (allEnrollments.isEmpty()) {
            log.warn("⚠️ CourseOffering 데이터가 없습니다.");
            return;
        }

        Long enrollmentId = allEnrollments.get(0).getEnrollmentId();
        Optional<UserVo> userVoOptional =  userRepository.findByEnrollmentId(enrollmentId);
        if(userVoOptional.isPresent()) {
            log.info("➡️ 개설 ID: {}, Enrollment {}", userVoOptional.get(), userVoOptional.get().getEnrollments());
        }
    }

    @Test
    public void find2() { // courseRepository
        List<CourseOffering> allOfferings = courseOfferingRepository.findAll();
        if (allOfferings.isEmpty()) {
            log.warn("⚠️ CourseOffering 데이터가 없습니다.");
            return;
        }

        Long offeringId = allOfferings.get(0).getOfferingId();
        courseRepository.findByOfferingId(offeringId).ifPresent(course -> {
            log.info("📘 과목명: {}", course.getCourseName());
            course.getOfferings().forEach(o ->
                    log.info("➡️ 개설 ID: {}, CourseOffering {}", o.getOfferingId(), o)
            );
        });
    }

    @Test
    public void find3() { // courseOfferingRepository
        List<CourseOffering> allOfferings = courseOfferingRepository.findAll();
        if (allOfferings.isEmpty()) {
            log.warn("⚠️ CourseOffering 데이터가 없습니다.");
            return;
        }

        Long courseId = allOfferings.get(0).getCourse().getCourseId();
        courseOfferingRepository.findCourseByCourseId(courseId).ifPresent(course ->
                log.info("📗 개설된 과목: {} (Course: {})", course.getCourseName(), course.getOfferings())
        );
    }



}
