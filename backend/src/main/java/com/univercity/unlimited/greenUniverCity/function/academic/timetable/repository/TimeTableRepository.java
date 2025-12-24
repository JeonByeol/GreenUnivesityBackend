package com.univercity.unlimited.greenUniverCity.function.academic.timetable.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable,Long> {

    //전체 시간표 조회 (DTO 변환 시 N+1 방지)
    @Query("SELECT t FROM TimeTable t " +
            "JOIN FETCH t.classSection cs " +
            "JOIN FETCH cs.courseOffering co " +
            "JOIN FETCH co.course c " +
            "LEFT JOIN FETCH t.classroom cr " +
            "ORDER BY t.timetableId ASC")
    List<TimeTable> findAllWithDetails();

    //T-2)에 선언된
    @Query("SELECT t FROM TimeTable t " +
            "JOIN FETCH t.classSection cs " +
            "JOIN FETCH cs.courseOffering co " +
            "WHERE co.offeringId =:offeringId")
    List<TimeTable> findTimeTableByOfferingId(@Param("offeringId") Long offeringId);


    @Query("SELECT t FROM TimeTable t " +
            "JOIN t.classSection cs " +
            "JOIN cs.courseOffering co " +
            "JOIN Enrollment e ON e.classSection = cs " +
            "JOIN e.user u " +
            "WHERE u.email = :email")
    List<TimeTable> findTimetableByStudentEmail(@Param("email") String email);

    // Case A: 생성 시 (나 자신은 없으므로 그냥 검사)
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

    // Case B: 수정 시 (나 자신(excludeId)은 제외하고 검사해야 함)
    @Query("SELECT COUNT(t) > 0 FROM TimeTable t " +
            "WHERE t.classroom.classroomId = :classroomId " +
            "AND t.dayOfWeek = :day " +
            "AND t.timetableId != :excludeId " + // ✅ 내 ID는 제외
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


}
