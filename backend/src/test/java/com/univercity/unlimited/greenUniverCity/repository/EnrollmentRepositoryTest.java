package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class EnrollmentRepositoryTest {
    @Autowired
    private EnrollmentRepository repository;

    @Test
    public void insertInitData(){
        // 데이터 세팅
        // 넣을게 없당

        // 데이터 저장
        for(int i=0; i<10; i++) {
            Enrollment enrollment = Enrollment.builder()
                    .enrollDate(LocalDateTime.now())
                    .build();

            repository.save(enrollment);
        }
    }
}
