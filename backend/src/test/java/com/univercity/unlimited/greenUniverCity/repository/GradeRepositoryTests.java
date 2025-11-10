package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
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
    private UserRepository userRepository;

    @Test
    public void testGradeData(){
        String vv[]={"A+","A","B+","B"};
        for(int i = 1; i < 4; i++){
            final long userId = i;
            UserVo userid = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("Test Error: Enrollment " + userId + " not found")
                    );



            Grade grade=Grade.builder()
                    .gradeValue(vv[i])
                    .user(userid)
                    .build();

            repository.save(grade);
        }
    }
    @Test
    public void gradefind(){
        repository.findAll();
    }
}
