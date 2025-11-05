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
        final long offeringId=1;
//        final long enrollmentId=1;
        //CourseOffering courseOffering=courseOfferingRepository.findById(offeringId)

        TimeTable timeTable=TimeTable.builder()
                .courseOffering(new CourseOffering(1l,null,"교수님",1,1))
                .dayOfWeek("1")
                .startTime(LocalTime.of(2,4,7,0))
                .endTime(LocalTime.of(7,3,5,1))
                .location("부산")
                .build();
        repository.save(timeTable);
//        for (int i= 1; i <4 ; i++) {
//
//        }

    }
}
