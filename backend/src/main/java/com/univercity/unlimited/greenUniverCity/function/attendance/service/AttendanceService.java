package com.univercity.unlimited.greenUniverCity.function.attendance.service;

import com.univercity.unlimited.greenUniverCity.function.attendance.dto.AttendanceCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.attendance.dto.LegacyAttendanceDTO;
import com.univercity.unlimited.greenUniverCity.function.attendance.dto.AttendanceResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.attendance.dto.AttendanceUpdateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AttendanceService {
    //주석-1) AttendanceController=A

    //A-1) A에 선언된 postmanTestAttendance의 요청을 받아서 출결 테이블에 존재하는 전체 데이터를 조회하기 위해 동작하는 서비스 선언
    List<AttendanceResponseDTO> findAllAttendance();

    //A-2) A에 선언된 postmanTestMyAttendance의 요청을 받아서 특정 학생이 자신의 수강과목에 대한 모든 출결을 조회하기 위해 동작하는 서비스 선언
    List<AttendanceResponseDTO> studentOfAttendance(Long enrollmentId,String studentEmail);

    //A-3) A에 선언된 postmanTestCreateAttendance의 요청을 받아서 교수가 특정 학생에 대한 출결을 작성하기 위해 동작하는 서비스 선언
    AttendanceResponseDTO createAttendanceForProfessor(AttendanceCreateDTO dto,String studentEmail,String professorEmail);

    //A-4) A에 선언된 postmanUpdateAttendance의 요청을 받아서 교수가 학생에 대한 출결을 수정하기 위해 동작하는 서비스 선언
    AttendanceResponseDTO updateAttendanceForProfessor(AttendanceUpdateDTO dto,String studentEmail,String professorEmail);


    //A-A) ** 필요한 기능 입력 부탁드립니다 | 사용 안하면 삭제 서비스구현 삭제 부탁드립니다 **
    ResponseEntity<String> addAttendance(LegacyAttendanceDTO legacyAttendanceDTO);
}
