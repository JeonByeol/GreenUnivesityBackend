package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import jakarta.transaction.Transactional;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
@ToString
public class CourseOfferingRepositoryTests {
    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    @Test
    @Transactional
    public void findAll() { // courseRepository
        List<CourseOffering> allOfferings = courseOfferingRepository.findAll();
        if (allOfferings.isEmpty()) {
            log.warn("⚠️ CourseOffering 데이터가 없습니다.");
            return;
        }

        for(CourseOffering courseOffering: allOfferings){
            log.info( "{}가 보유한 Course 정보 : {}",courseOffering.getOfferingId(), courseOffering.getCourse());
        }
    }
}
