package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.AttendanceDTO;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AttendanceService {
    //수강신청한 전체 강의 불러오기
    List<Enrollment> findAllEnrollment();
    //수강하고 있는 학생의 이름일치확인,이름과 같을 경우 신청한 과목코드 불러오기
    List<Enrollment> findPartEnrollment(User user, Long enrollmentId);

//    Optional<List<AttendanceDTO>> findAllAttendance();

    List<AttendanceDTO> findAllat();
    ResponseEntity<String> addAttendance(AttendanceDTO attendanceDTO);
}
