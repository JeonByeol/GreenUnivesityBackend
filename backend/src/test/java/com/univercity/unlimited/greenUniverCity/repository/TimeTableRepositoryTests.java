package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.entity.TimeTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@Slf4j
public class TimeTableRepositoryTests {
    @Autowired TimeTableRepository repository;

    @Autowired CourseOfferingRepository courseOfferingRepository;

    @Test

    public void testTimeTableData(){
        // 데이터 세팅
        String[] dummyLocations = {
                "공학관 101호",
                "인문관 305호",
                "자연과학관 208호",
                "경영관 502호",
                "학생회관 201호 (세미나실 A)",
                "법학관 110호 (대강의실)",
                "예술관 404호 (실습실)",
                "IT융합관 707호 (컴퓨터실 B)",
                "공학관 310호",
                "인문관 102호",
                "자연과학관 B101호 (실험실)",
                "경영관 1205호 (MBA 강의실)",
                "중앙도서관 4층 (정보교육실)",
                "법학관 303호 (모의법정)",
                "예술관 101호 (소극장)",
                "IT융합관 901호 (대강당)",
                "공학관 215호",
                "인문관 412호",
                "자연과학관 309호",
                "경영관 301호 (세미나실 B)"
        };
        String [] week={"월","화","수","목","금"};

        List<CourseOffering> courseOfferings = courseOfferingRepository.findAll();

        // 데이터 체크
        if(courseOfferings.isEmpty() == true) {
            log.info("CourseOfferings 데이터가 없습니다.");
            return;
        }

        // 데이터 추가
        for(CourseOffering courseOffering : courseOfferings) {
            TimeTable timeTable = TimeTable.builder()
                    .dayOfWeek(week[(int)(Math.random()*week.length)])
                    .startTime(LocalDateTime.now())
                    .endTime(LocalDateTime.now())
                    .location(dummyLocations[(int)(Math.random()*dummyLocations.length)])
                    .courseOffering(courseOffering)
                    .build();

            repository.save(timeTable);
        }
    }
}
