package com.univercity.unlimited.greenUniverCity.function.timetable.controller;

import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableStudentDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/time")
@RequiredArgsConstructor
@Slf4j
public class TimeTableController {
    private final TimeTableService timeTableService;

    @GetMapping("/all")
    public List<TimeTableDTO> postmanTestTimeTable(){
        log.info("여기는 시간표 전체조회Controller 입니다");
        return timeTableService.findAllTimeTable();
    }

    //T-2) 특정 과목에 대한 시간표를 조회하는 기능
    @GetMapping("/one/{dayOfWeek}")
    public List<TimeTableStudentDTO> postmanTestCourseTimeTable(@PathVariable("dayOfWeek") String dayOfWeek){
        log.info("1)왜 안됨?:{}",dayOfWeek);
        return timeTableService.get2(dayOfWeek);
    }

    //T-3) 특정 학생이 수강신청한 모든 시간표를 조회
    @GetMapping("/my/{email}")
    public List<TimeTableStudentDTO> postmanTestMyTimeTable(@PathVariable("email") String email){
        return timeTableService.get(email);
    }

}
