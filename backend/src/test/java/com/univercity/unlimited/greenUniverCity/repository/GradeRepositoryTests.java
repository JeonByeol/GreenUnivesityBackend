package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class GradeRepositoryTests {
    @Autowired
    private GradeRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    @Tag("push")
    public void testGradeData(){
        // 데이터 세팅
        String vv[]={"A+","A","B+","B","C+","C","D+","D","F"};
//        for(int i = 1; i <= 10; i++){
//            final long enrollmentId = i;
//            Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
//                    .orElseThrow(() ->
//                            new RuntimeException("Test Error: Enrollment " + enrollmentId + " not found")
//                    );
//            Grade grade=Grade.builder()
//                    .enrollment(enrollment)
//                    .gradeValue(vv[(int)(Math.random()*vv.length)])
//                    .build();
//
//            repository.save(grade);
//            }
        List<User> users = userRepository.findAll();
        List<Enrollment> enrollments = enrollmentRepository.findAll();

        if(users.isEmpty() == true) {
            log.info("User가 비어있습니다.");
            return;
        }
            for(Enrollment enrollment : enrollments) {
//                enrollment=enrollments.get((int)(Math.random())*enrollments.size());
                if((int)(Math.random()*2) == 1) {
                    continue;
                }
                Grade grade=Grade.builder()
                        .enrollment(enrollment)
                        .gradeValue(vv[(int)(Math.random()*vv.length)])
                        .build();

                repository.save(grade);
            }

    }
}
