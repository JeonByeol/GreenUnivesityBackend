package com.univercity.unlimited.greenUniverCity.function.timetable.repository;

import com.univercity.unlimited.greenUniverCity.function.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable,Integer> {
//    @Query("SELECT t FROM TimeTable t " +
//            "JOIN t.courseOffering co " +
//            "WHERE co.offeringId =:offeringId")
//    List<TimeTable> findByOfferingTimeTable(@Param("offeringId") Long offeringId);//T-2)

    //T-2)본인 시간표 조회
    @Query("SELECT t FROM TimeTable t " +
            "JOIN FETCH t.courseOffering co " +
            "WHERE t.dayOfWeek = :dayOfWeek")
    List<TimeTable> findByDayTimeTable(@Param("dayOfWeek") String dayOfWeek);

    @Query("SELECT t FROM TimeTable t " +
            "JOIN t.courseOffering co " +
            "JOIN Enrollment e ON e.courseOffering = co " +
            "JOIN e.user u " +
            "WHERE u.email = :email")
    List<TimeTable> findTimetableByStudentEmail(String email);

}
