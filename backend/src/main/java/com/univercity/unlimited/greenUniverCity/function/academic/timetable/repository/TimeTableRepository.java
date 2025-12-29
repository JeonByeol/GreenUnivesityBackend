package com.univercity.unlimited.greenUniverCity.function.academic.timetable.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable,Long> {

    //T-1) 시간표 테이블에 존재하는 모든 데이터를 조회하기 위한 쿼리 (N+1 문제 방지 Fetch Join 사용)
    @Query("SELECT t FROM TimeTable t " +
            "JOIN FETCH t.classSection cs " +
            "JOIN FETCH cs.courseOffering co " +
            "JOIN FETCH co.course c " +
            "LEFT JOIN FETCH t.classroom cr " +
            "ORDER BY t.timetableId ASC")
    List<TimeTable> findAllWithDetails();

    //T-2) 특정 개설 강의(Offering)에 포함된 시간표 목록을 조회하기 위한 쿼리
    @Query("SELECT t FROM TimeTable t " +
            "JOIN FETCH t.classSection cs " +
            "JOIN FETCH cs.courseOffering co " +
            "WHERE co.offeringId =:offeringId")
    List<TimeTable> findTimeTableByOfferingId(@Param("offeringId") Long offeringId);

    //T-3) 특정 학생이 수강신청한 과목들의 시간표를 조회하기 위한 쿼리 (Enrollment 조인)
    @Query("SELECT t FROM TimeTable t " +
            "JOIN t.classSection cs " +
            "JOIN cs.courseOffering co " +
            "JOIN Enrollment e ON e.classSection = cs " +
            "JOIN e.user u " +
            "WHERE u.email = :email")
    List<TimeTable> findTimetableByStudentEmail(@Param("email") String email);

    //T-4) 시간표 생성 시, 해당 강의실과 시간에 겹치는 수업이 있는지 확인하기 위한 검증 쿼리
    @Query("SELECT COUNT(t) > 0 FROM TimeTable t " +
            "WHERE t.classroom.classroomId = :classroomId " +
            "AND t.dayOfWeek = :day " +
            "AND (" +
            "   (t.startTime <= :start AND t.endTime > :start) OR " +
            "   (t.startTime < :end AND t.endTime >= :end) OR " +
            "   (t.startTime >= :start AND t.endTime <= :end)" +
            ")")
    boolean existsByTimeOverlap(@Param("classroomId") Long classroomId,
                                @Param("day") DayOfWeek day,
                                @Param("start") LocalTime start,
                                @Param("end") LocalTime end);

    //T-5) 시간표 수정 시, 자기 자신(excludeId)을 제외하고 해당 강의실/시간에 겹치는 수업이 있는지 확인하기 위한 검증 쿼리
    @Query("SELECT COUNT(t) > 0 FROM TimeTable t " +
            "WHERE t.classroom.classroomId = :classroomId " +
            "AND t.dayOfWeek = :day " +
            "AND t.timetableId != :excludeId " + // 내 ID는 제외
            "AND (" +
            "   (t.startTime <= :start AND t.endTime > :start) OR " +
            "   (t.startTime < :end AND t.endTime >= :end) OR " +
            "   (t.startTime >= :start AND t.endTime <= :end)" +
            ")")
    boolean existsByTimeOverlapExcludingId(@Param("classroomId") Long classroomId,
                                           @Param("day") DayOfWeek day,
                                           @Param("start") LocalTime start,
                                           @Param("end") LocalTime end,
                                           @Param("excludeId") Long excludeId);


    // 교수의 이메일로 시간표 조회 쿼리(인데 사용못하고 있음 추후 안하면 삭제)
    @Query("SELECT t FROM TimeTable t " +
            "JOIN t.classSection cs " +
            "JOIN cs.courseOffering co " +
            "JOIN co.professor p " +
            "WHERE p.email = :email")
    List<TimeTable> findByProfessorEmail(@Param("email") String email);
}