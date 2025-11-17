package com.univercity.unlimited.greenUniverCity.function.timetable.controller;

import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
}
