package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.dto.AttendanceDTO;
import com.univercity.unlimited.greenUniverCity.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.entity.Board;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import com.univercity.unlimited.greenUniverCity.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @GetMapping("/all")
    public ResponseEntity<List<AttendanceDTO>> postAllSearch(){
        log.info("모든 게시판들의 이름 출력");

        Optional<List<AttendanceDTO>> optionalAttendanceDTOS = attendanceService.findAllAttendance();
        if(optionalAttendanceDTOS.isEmpty() == true){
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(optionalAttendanceDTOS.get());
    }

    //학생
    //강의별 본인출석조회(전체 출석률체크포함)
    //출석체크
//    @GetMapping("/student/checkclass")//수강한 전체 강의 불러오기
//    public List<Enrollment> postmanTestEnroll(){
//        log.info("수강한 전체강의 호출");
//        return attendanceService.findAllEnrollment();
//    }
//    @GetMapping("/student/partclass")//수강한 일부과목 불러오기
//    public List<Enrollment> postmanTestPartEnroll(@PathVariable("partclass")UserVo userVo,Long enrollmentId){
//        log.info("학생이 수강하는 일부과목 호출");
//        return attendanceService.findPartEnrollment(userVo,enrollmentId);
//    }

    //교수
    //학생출석등록/수정
    //학생들 전체 출석부


    //관리자
    //학생들 전체 출석부
    //학생 출석 등록/수정

}
