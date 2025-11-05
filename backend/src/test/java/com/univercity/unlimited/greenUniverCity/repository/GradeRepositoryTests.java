package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class GradeRepositoryTests {
    @Autowired
    private GradeRepository repository;

    @Autowired
    private CourseRepository re;

    @Autowired
    private EnrollmentRepository enrollmentRepository;


    @Test
    public void testGradeData(){
        for(int i = 1; i < 3; i++){
            final long enrollmentId = i;
            Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                    .orElseThrow(() ->
                            new RuntimeException("Test Error: Enrollment " + enrollmentId + " not found")
                    );
            Grade grade=Grade.builder()
                    .gradeValue("A+")
                    .enrollment(enrollment)
                    .build();

            repository.save(grade);
        }
    }
    @Test
    public void gradefind(){
        repository.findAll();
    }
}
