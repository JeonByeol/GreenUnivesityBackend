package com.univercity.unlimited.greenUniverCity.function.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.entity.TimeTable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TimeTableService {
    //주석-1) TimeTableController=T

    //T-1) T에 선언된 postmanTestTimeTable의 요청을 받아서 시간표 테이블에 존재하는 전체 데이터 조회에 필요한 데이터를 조회하기 위해 동작하는 서비스 선언
    List<TimeTableDTO> findAllTimeTable();

    //학생
    //List<TimeTableDTO> get2(Long offeringId);
    //T-2) T에 선언된 postmanTestCourseTimeTable의 요청을 받아서 특정 과목에 존재하는 모든 시간표를 조회하는 위해 동작하는 서비스 선언
    List<TimeTableResponseDTO> get2(String dayOfWeek);

    //T-3) T에 선언된 postmanTestMyTimeTable의 요청을 받아서 특정 학생이 수강신청한 모든 시간표를 조회하기 위해 동작하는 서비스 선언
    List<TimeTableResponseDTO> get(String email);

    //(관리자or교수)
    //T-4) T에 선언된 postmanTestNewTimeTable의 요청을 받아서 특정 강의에 대한 새로운 시간표를 생성하기 위해 동작하는 서비스 선언
    TimeTableResponseDTO post(TimeTableCreateDTO dto, String requesterEmail);

    //T-5) T에 선언된 postmanUpdateTimeTable의 요청을 받아서 기존에 존재하는 강의 시간표를 수정하기 위해 동작하는 서비스 선언
    TimeTableResponseDTO put(Integer timetableId,TimeTableCreateDTO dto,String requesterEmail);

    //T-6) T에 선언된 postmanDeleteTimeTable의 요청을 받아서 기존에 존재하는 강의에 대한 시간표를 삭제하기 위해 동작하는 서비스 선언
    TimeTable delete();

    ResponseEntity<String> addTimeTable(TimeTableDTO timeTableDTO);
}
