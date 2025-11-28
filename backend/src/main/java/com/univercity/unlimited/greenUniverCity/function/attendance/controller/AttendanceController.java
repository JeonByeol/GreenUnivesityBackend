package com.univercity.unlimited.greenUniverCity.function.attendance.controller;

import com.univercity.unlimited.greenUniverCity.function.attendance.dto.AttendanceCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.attendance.dto.AttendanceResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.attendance.dto.AttendanceUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    
    //A-1) 출결 컨트롤러 클래스에서 테이블내에 존재하는 모든 데이터를 조회하는 crud(get방식)선언
    @GetMapping("/all")
    public List<AttendanceResponseDTO> postmanTestAttendance(){
        log.info("1) 출결 전체조회 요청");
        return attendanceService.findAllAttendance();
    }

    //A-2) 출결 컨트롤러 클래스에서 "학생의 특정 과목" 에 대한 출결을 조회하기 위해 CRUD(get방식)선언
    @GetMapping("/enrollment/{enrollmentId}")
    public List<AttendanceResponseDTO>postmanTestMyAttendance(
            @PathVariable("enrollmentId") Long enrollmentId){
        log.info("1) 학생의 출결 조회 요청 enrollmentId-:{}",enrollmentId);
        return attendanceService.findForEnrollmentOfAttendance(enrollmentId);
    }

    //A-3) 출결 컨트롤러 클래스에서 "학생의 전체 출결"을 조회하기 위해 CRUD(get방식)선언
    @GetMapping("/my/{email}")
    public List<AttendanceResponseDTO>postmanTestMyAttendance(
            @PathVariable("email") String email){
        log.info("1) 학생의 출결 조회 요청 email-:{}",email);
        return attendanceService.findForStudentOfAttendance(email);
    }
    
    //A-4)출결 컨트롤러 클래스에서 학생의 출결을 작성하기 위해 CRUD(post방식) 선언
    @PostMapping("/create")
    public ResponseEntity<AttendanceResponseDTO> postmanCreateAttendance(
            @RequestBody AttendanceCreateDTO dto,
            @RequestHeader(value="X-User-Email",required = false) String professorEmail){

        log.info("1) 출결 생성 요청 교수-:{}, enrollmentId-:{}",professorEmail,dto.getEnrollmentId());

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "hannah@aaa.com"; // 테스트용 기본값
        }

        AttendanceResponseDTO response=attendanceService.createAttendanceForProfessor(dto,professorEmail);

        return ResponseEntity.ok(response);
    }

    //A-5) 출결 컨트롤러 클래스내에서 학생의 출결을 수정하기 위해 CRUD(put)방식 선언
    @PutMapping("/update")
    public ResponseEntity<AttendanceResponseDTO> postmanUpdateAttendance(
            @RequestBody AttendanceUpdateDTO dto,
            @RequestHeader (value="X-User-Email",required = false) String professorEmail){

        log.info("1) 출결 수정 요청 attendanceId-:{}, 교수-:{}",dto.getAttendanceId(),professorEmail);

        // Postman 테스트용: Header가 없으면 기본값 사용 (개발 환경에서만)
        if (professorEmail == null || professorEmail.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            professorEmail = "hannah@aaa.com"; // 테스트용 기본값
        }

        AttendanceResponseDTO updateAttendance=attendanceService.updateAttendanceForProfessor(
                dto,
                professorEmail
        );

        return ResponseEntity.ok(updateAttendance);
    }



    //교수
    //학생출석등록/수정
    //학생들 전체 출석부


    //관리자
    //학생들 전체 출석부
    //학생 출석 등록/수정

}
