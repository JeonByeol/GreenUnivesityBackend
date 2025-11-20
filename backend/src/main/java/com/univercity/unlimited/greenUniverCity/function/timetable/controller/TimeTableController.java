package com.univercity.unlimited.greenUniverCity.function.timetable.controller;

import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/time")
@RequiredArgsConstructor
@Slf4j
public class TimeTableController {
    private final TimeTableService timeTableService;

    //T-1) 시간표 테이블에 존재하는 모든 시간표를 조회하기 위해 컨트롤러 내에 선언된 crud(get)
    @GetMapping("/all")
    public List<TimeTableDTO> postmanTestTimeTable(){
        log.info("여기는 시간표 전체조회Controller 입니다");
        return timeTableService.findAllTimeTable();
    }

    //T-2) 특정 과목에 대한 시간표를 조회하기 위해 컨트롤러 내에 선언된 crud(get)
    @GetMapping("/one/{dayOfWeek}")
    public List<TimeTableResponseDTO> postmanTestCourseTimeTable(@PathVariable("dayOfWeek") String dayOfWeek){
        log.info("1)왜 안됨?:{}",dayOfWeek);
        return timeTableService.get2(dayOfWeek);
    }

    //T-3) 특정 학생이 수강신청한 모든 시간표를 조회하기 위해 컨트롤러 내에 선언된 crud(get)
    @GetMapping("/my/{email}")
    public List<TimeTableResponseDTO> postmanTestMyTimeTable(@PathVariable("email") String email){
        return timeTableService.get(email);
    }

    //T-4) 교수 or 관리자가 개설된 강의에 대한 시간표를 생성하기 위해 컨트롤러 내에 선언된 crud(post)
    @PostMapping("/create/{email}")
    public ResponseEntity<TimeTableResponseDTO> postmanCreateTime(
            @RequestBody TimeTableCreateDTO dto,
            @PathVariable("email") String requesterEmail){

        log.info("1) 시간표 생성 요청 -교수:{},offeringId:{}",requesterEmail,dto.getOfferingId());

        TimeTableResponseDTO response=timeTableService.post(dto,requesterEmail);

        return ResponseEntity.ok(response);
    }


}
