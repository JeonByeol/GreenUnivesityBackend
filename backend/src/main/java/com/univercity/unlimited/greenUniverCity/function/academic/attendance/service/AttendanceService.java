package com.univercity.unlimited.greenUniverCity.function.academic.attendance.service;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.LegacyAttendanceDTO;
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
    AttendanceResponseDTO createAttendanceForProfessor(AttendanceCreateDTO dto,String professorEmail);

    //A-5) A에 선언된 postmanUpdateAttendance의 요청을 받아서 교수가 학생에 대한 출결을 수정하기 위해 동작하는 서비스 선언
    AttendanceResponseDTO updateAttendanceForProfessor(AttendanceUpdateDTO dto,String professorEmail);

    //A-A) ** 필요한 기능 입력 부탁드립니다 | 사용 안하면 삭제 서비스구현 삭제 부탁드립니다 **
    ResponseEntity<String> addAttendance(LegacyAttendanceDTO legacyAttendanceDTO);
}
