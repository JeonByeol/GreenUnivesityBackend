package com.univercity.unlimited.greenUniverCity.function.academic.attendance.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.service.AttendanceService;
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

        AttendanceResponseDTO response=attendanceService.createAttendance(dto,professorEmail);

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

        AttendanceResponseDTO updateAttendance=attendanceService.updateAttendance(
                dto,
                professorEmail
        );

        return ResponseEntity.ok(updateAttendance);
    }

    // =================================================================================
    // 8. [삭제] 출결 삭제 (교수) (New!)
    // =================================================================================
    @DeleteMapping("/delete/{attendanceId}")
    public ResponseEntity<String> deleteAttendance(
            @PathVariable("attendanceId") Long attendanceId,
            @RequestHeader(value="X-User-Email", required = false) String professorEmail) {

        if (professorEmail == null || professorEmail.isEmpty()) {
            professorEmail = "hannah@aaa.com";
        }

        log.info("1) 출결 삭제 요청 - ID: {}, 교수: {}", attendanceId, professorEmail);
        attendanceService.deleteAttendance(attendanceId, professorEmail);

        return ResponseEntity.ok("출결 데이터가 삭제되었습니다.");
    }

    // =================================================================================
    // 1. [조회] 출결 단건 조회 (New!)
    // - 수정 버튼 누르기 전이나 상세 보기 시 사용
    // =================================================================================
    @GetMapping("one/{attendanceId}")
    public ResponseEntity<AttendanceResponseDTO> getAttendance(@PathVariable("attendanceId") Long attendanceId) {
        log.info("1) 출결 단건 조회 요청 - ID: {}", attendanceId);
        AttendanceResponseDTO response = attendanceService.getAttendance(attendanceId);
        return ResponseEntity.ok(response);
    }

    // =================================================================================
    // 3. [조회] 교수 - 특정 강의(Offering)의 전체 출결 현황 조회 (New!)
    // - 교수가 본인 수업의 출석부를 볼 때 사용
    // =================================================================================
    @GetMapping("/offering/{offeringId}")
    public List<AttendanceResponseDTO> getAttendanceByOffering(
            @PathVariable("offeringId") Long offeringId,
            @RequestHeader(value="X-User-Email", required = false) String professorEmail) {

        // Postman 테스트용 헤더 처리
        if (professorEmail == null || professorEmail.isEmpty()) {
            professorEmail = "hannah@aaa.com"; // 테스트용 기본값
        }

        log.info("1) 교수 과목별 출결 조회 요청 - OfferingId: {}, 교수: {}", offeringId, professorEmail);
        return attendanceService.getAttendanceByOffering(offeringId, professorEmail);
    }

}
