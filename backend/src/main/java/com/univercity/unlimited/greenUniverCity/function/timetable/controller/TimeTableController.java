package com.univercity.unlimited.greenUniverCity.function.timetable.controller;

import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableUpdateDTO;
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

    /**
     * [전체 조회] 시간표 테이블의 모든 데이터를 조회합니다.
     * (T-1)
     */
    //T-GET) 시간표 테이블에 존재하는 모든 시간표를 조회하기 위해 컨트롤러 내에 선언된 crud(get)
    @GetMapping("/all")
    public List<TimeTableResponseDTO> postmanTestTimeTable(){
        log.info("1) 여기는 시간표 전체조회Controller 입니다");
        return timeTableService.findAllTimeTable();
    }

    //T-2) 특정 과목에 대한 시간표를 조회하기 위해 컨트롤러 내에 선언된 crud(get)
    @GetMapping("/one/{offeringId}")
    public List<TimeTableResponseDTO> postmanTestCourseTimeTable(@PathVariable("offeringId") Long offeringId){
        log.info("1)왜안됨?:{}",offeringId);
        return timeTableService.offeringOfTimeTable(offeringId);
    }

    //T-3) 특정 학생이 수강신청한 모든 시간표를 조회하기 위해 컨트롤러 내에 선언된 crud(get)
    @GetMapping("/my/{email}")
    public List<TimeTableResponseDTO> postmanTestMyTimeTable(@PathVariable("email") String email){
        return timeTableService.studentOfTimeTable(email);
    }

    //T-4) 교수 or 관리자가 개설된 강의에 대한 시간표를 생성하기 위해 컨트롤러 내에 선언된 crud(post)
    @PostMapping("/create")
    public ResponseEntity<TimeTableResponseDTO> postmanCreateTime(
            @RequestBody TimeTableCreateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail){

        log.info("1) 시간표 생성 요청 -교수:{},offeringId:{}",requesterEmail,dto.getOfferingId());

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (requesterEmail == null || requesterEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            requesterEmail = "hannah@aaa.com"; // 테스트용 기본값
        }

        TimeTableResponseDTO response=timeTableService.createTimeTableForProfessor(dto,requesterEmail);

        return ResponseEntity.ok(response);
    }

    //T-5) 교수가 본인이 담당하고 있는 수업에 존재하는 시간표를 수정하기 위한 컨트롤러 내에 선언된 crud(put)
    @PutMapping("/update")
    public ResponseEntity<TimeTableResponseDTO> postmanUpdateTimeTable(
//          @PathVariable("timetableId") Integer timetableId,
            @RequestBody TimeTableUpdateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail){

        log.info("1) 시간표 수정 요청 timetableId-:{},강의실-:{},요일-:{},시작시간-:{},종료시간-:{}",
                dto.getTimetableId(),
                dto.getLocation(),
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (requesterEmail == null || requesterEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            requesterEmail = "julia@aaa.com"; // 테스트용 기본값
        }

        TimeTableResponseDTO updateTimeTable=timeTableService.updateTimeTableForProfessor(
                dto,
                requesterEmail
        );

        return ResponseEntity.ok(updateTimeTable);
    }

    //T-6) 교수 or 관리자가 개설된 강의에 대한 시간표를 삭제하기 위한 컨트롤러 내에 선언된 crud(delete)
    @DeleteMapping("/delete/{timetableId}")
    public void postmanDeleteTimeTable(
            @PathVariable("timetableId") Integer timetableId,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail){

        log.info("1) 시간표 삭제 요청 timetableId-:{}, requesterEmail:{}",
                timetableId,requesterEmail);

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (requesterEmail == null || requesterEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            requesterEmail = "julia@aaa.com"; // 테스트용 기본값
        }

        timeTableService.deleteByTimeTable(timetableId,requesterEmail);
    }

}
