package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import lombok.extern.slf4j.Slf4j;
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
    private EnrollmentRepository enrollmentRepository;

    @Test
    public void testGradeData(){
        // 데이터 세팅
        String vv[]={"A+","A","B+","B","C+","C","D+","D","F"};

        List<UserVo> users = userRepository.findAll();
        List<Enrollment> enrollments = enrollmentRepository.findAll();

        if(users.isEmpty() == true) {
            log.info("User가 비어있습니다.");
            return;
        }

        for(UserVo user : users) {
            for(Enrollment enrollment : enrollments) {
                if((int)(Math.random()*2) == 1)
                    continue;

                Grade grade=Grade.builder()
                        .enrollmentId(enrollment.getEnrollmentId())
                        .gradeValue(vv[(int)(Math.random()*vv.length)])
                        .user(user)
                        .build();

                repository.save(grade);
            }
        }
    }
    @Test
    public void gradefind(){
        repository.findAll();
    }
}
