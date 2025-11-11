package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class CourseOfferingRepositoryTests {
    @Autowired
    private CourseOfferingRepository repository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @Tag("push")
    public void insertInitData() {
        // 데이터 세팅
        // Course가 있다는 가정하에 진행합니다.
        List<Course> coureList = courseRepository.findAll();
        if(coureList.isEmpty() == true) {
            log.info("코스 데이터가 없습니다.");
            return;
        }

        for(Course course : coureList) {
            int max = (int) (Math.random() * 3) + 1;
            int semester = 1;
            char alphabat = 'A';
            for (int i = 0; i < max; i++) {
                CourseOffering courseOffering = CourseOffering.builder()
                        .professorName("EMPTY")
                        .courseName(course.getCourseName()+alphabat++)
                        .year(2025)
                        .semester(semester)
                        .course(course)
                        .build();

                repository.save(courseOffering);

                // 데이터 처리이후 초기화
                if((int) (Math.random() * 2) == 1) {
                    alphabat = 'A';
                    semester++;
                }
            }
        }
    }
}
