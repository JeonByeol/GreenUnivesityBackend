package com.univercity.unlimited.greenUniverCity.function.academic.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableUpdateDTO;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface TimeTableService {
    //주석-1) TimeTableController=T

    //T-1) T에 선언된 postmanTestTimeTable의 요청을 받아서 시간표 테이블에 존재하는 전체 데이터를 조회하기 위해 동작하는 서비스 선언

    List<TimeTableResponseDTO> findAllTimeTable();

    //T-2) T에 선언된 postmanTestCourseTimeTable의 요청을 받아서 특정 과목에 존재하는 모든 시간표를 조회하는 위해 동작하는 서비스 선언
    List<TimeTableResponseDTO> offeringOfTimeTable(Long offeringId);

    //T-2-1) 본인 id를 활용하여 단건 조회를 할 수 있게 service 구현
    TimeTableResponseDTO getTimeTable(Long timetableId);

    //T-3) T에 선언된 postmanTestMyTimeTable의 요청을 받아서 특정 학생이 수강신청한 모든 시간표를 조회하기 위해 동작하는 서비스 선언
    List<TimeTableResponseDTO> studentOfTimeTable(String email);

    //(관리자or교수)
    //T-4) T에 선언된 postmanTestNewTimeTable의 요청을 받아서 특정 강의에 대한 새로운 시간표를 생성하기 위해 동작하는 서비스 선언
    TimeTableResponseDTO createTimeTableForProfessor(TimeTableCreateDTO dto, String requesterEmail);

    //T-5) T에 선언된 postmanUpdateTimeTable의 요청을 받아서 기존에 존재하는 강의 시간표를 수정하기 위해 동작하는 서비스 선언
    TimeTableResponseDTO updateTimeTableForProfessor(TimeTableUpdateDTO dto, String requesterEmail);

    //T-6) T에 선언된 postmanDeleteTimeTable의 요청을 받아서 기존에 존재하는 강의에 대한 시간표를 삭제하기 위해 동작하는 서비스 선언
    void deleteByTimeTable(Long timetableId,String requesterEmail);

}
