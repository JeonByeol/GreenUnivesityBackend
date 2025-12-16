package com.univercity.unlimited.greenUniverCity.function.academic.timetable.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable,Long> {

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



}
