package com.univercity.unlimited.greenUniverCity.function.academic.attendance.service;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceUpdateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AttendanceService {
    //주석-1) AttendanceController=A

    //A-1) A에 선언된 postmanTestAttendance의 요청을 받아서 출결 테이블에 존재하는 전체 데이터를 조회하기 위해 동작하는 서비스 선언
    List<AttendanceResponseDTO> findAllAttendance();

    //A-2) A에 선언된 postmanTestEnrollForAttendance의 요청을 받아서 "학생의 특정 과목"에 대한 출결을 조회하기 위해 동작하는 서비스 선언
    List<AttendanceResponseDTO> findForEnrollmentOfAttendance(Long enrollmentId);

    //A-3) A에 선언된 postmanTestMyAttendance의 요청을 받아서 특정 학생이 자신의 수강과목에 대한 모든 출결을 조회하기 위해 동작하는 서비스 선언
    List<AttendanceResponseDTO> findForStudentOfAttendance(String email);

    //A-4) A에 선언된 postmanTestCreateAttendance의 요청을 받아서 교수가 특정 학생에 대한 출결을 작성하기 위해 동작하는 서비스 선언
    AttendanceResponseDTO createAttendance(AttendanceCreateDTO dto,String professorEmail);

    //A-5) A에 선언된 postmanUpdateAttendance의 요청을 받아서 교수가 학생에 대한 출결을 수정하기 위해 동작하는 서비스 선언
    AttendanceResponseDTO updateAttendance(AttendanceUpdateDTO dto,String professorEmail);

    // =================================================================================
    // A-6). [조회] 출결 단건 조회 (기본 CRUD 보강)
    // - 수정 버튼 누르기 전이나 상세 보기 시 사용
    // =================================================================================
    AttendanceResponseDTO getAttendance(Long attendanceId);

    // =================================================================================
    // A-7). [조회] 교수가 특정 과목(Offering)의 전체 출결 현황 조회
    // =================================================================================
    List<AttendanceResponseDTO> getAttendanceByOffering(Long offeringId, String professorEmail);


    // =================================================================================
    // A-8). [삭제] 출결 삭제 (교수)
    // =================================================================================
    void deleteAttendance(Long attendanceId, String professorEmail);
}
