package com.univercity.unlimited.greenUniverCity.function.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableStudentDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.entity.TimeTable;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.util.List;

public interface TimeTableService {
    //T-1) 리뷰 테이블에 존재하는 모든 데이터 조회
    List<TimeTableDTO> findAllTimeTable();

    //학생
//    List<TimeTableDTO> get2(Long offeringId);//T-2) 특정 과목에 대한 시간표를 조회하는 기능
    List<TimeTableStudentDTO> get2(String dayOfWeek);//T-2) 특정 과목에 대한 시간표를 조회하는 기능

    //T-3) 특정 학생이 수강신청한 모든 시간표를 조회
    List<TimeTableStudentDTO> get(String email);

    //T-4) 특정 강의에 대한 시간표를 생성 (관리자or교수)
    TimeTable post();

    //T-5) 기존에 존재하는 강의 시간표를 수정
    TimeTable put();

    //T-6) 강의에 대한 시간표를 삭제
    TimeTable delete();

    ResponseEntity<String> addTimeTable(TimeTableDTO timeTableDTO);
}
