package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
public class CCPRepositoryTests {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    @Test
    public void testInsert(){
        var departmentArray = List.of("컴퓨터","정보","전기");

        for(int i = 0; i < 10; i++){
            Department department = Department.builder()
                    .deptName(departmentArray.get(i%3))
                    .build();
            
            Course course = Course.builder()
                    .courseId("cour"+i)
                    .courseName("cou"+i)
                    .description("dec")
                    .credits(i)
                    .department(department)
                    .build();

            CourseOffering courseOffering = CourseOffering.builder()
                    .professorId("pro"+i)
                    .year(LocalDateTime.now().getYear())
                    .semester(i%2)
                    .course(course)
                    .build();

            departmentRepository.save(department);
            courseRepository.save(course);
            courseOfferingRepository.save(courseOffering);


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


}
