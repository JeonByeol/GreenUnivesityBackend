package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.entity.TimeTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

@SpringBootTest
@Slf4j
public class TimeTableRepositoryTests {
    @Autowired TimeTableRepository repository;

    @Autowired CourseOfferingRepository courseOfferingRepository;

    @Test

    public void testTimeTableData(){
        for (int i= 1; i <4 ; i++) {
        final long offeringId=i;
        CourseOffering courseOffering=courseOfferingRepository.findById(offeringId)
                .orElseThrow(() ->
                        new RuntimeException("Test Error: Enrollment " + offeringId + " not found")
                );
        TimeTable timeTable=TimeTable.builder()
                .courseOffering(courseOffering)
                .dayOfWeek("1"+i)
                .startTime(LocalTime.of(2,4,7,0))
                .endTime(LocalTime.of(7,3,5,1))
                .location("부산"+i)
                .build();
        repository.save(timeTable);


        }

    }
}
