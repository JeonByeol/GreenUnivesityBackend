package com.univercity.unlimited.greenUniverCity.function.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableStudentDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.entity.TimeTable;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.util.List;

public interface TimeTableService {
    List<TimeTableDTO> findAllTimeTable();

    //학생
//    List<TimeTableDTO> get2(Long offeringId);//T-2) 특정 과목에 대한 시간표를 조회하는 기능
    List<TimeTableStudentDTO> get2(String dayOfWeek);//T-2) 특정 과목에 대한 시간표를 조회하는 기능

    List<TimeTableStudentDTO> get(String email);//T-3) 특정 학생이 수강신청한 모든 시간표를 조회

    //관리자or교수
    TimeTable post();//T-4) 특정 강의에 대한 시간표를 생성

    TimeTable put();//T-5) 기존에 존재하는 강의 시간표를 수정

    TimeTable delete();//T-6) 강의에 대한 시간표를 삭제



    ResponseEntity<String> addTimeTable(TimeTableDTO timeTableDTO);
}
