package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
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

    @Test
    @Tag("push")
    public void testGradeData(){
        // 데이터 세팅
        String vv[]={"A+","A","B+","B","C+","C","D+","D","F"};

        List<User> users = userRepository.findAll();
        List<CourseOffering> courseOfferings = courseOfferingRepository.findAll();

        if(users.isEmpty() == true) {
            log.info("User가 비어있습니다.");
            return;
        }
        for(User user : users) {
            for(CourseOffering offering : courseOfferings) {
                if((int)(Math.random()*2) == 1)
                    continue;

                Grade grade=Grade.builder()
                        .offeringId(offering.getOfferingId())
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
