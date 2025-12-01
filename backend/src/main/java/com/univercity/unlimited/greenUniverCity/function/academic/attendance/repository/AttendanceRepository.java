package com.univercity.unlimited.greenUniverCity.function.academic.attendance.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository <Attendance,Long>{


    @Query("SELECT a FROM Attendance a " +
            "JOIN FETCH a.enrollment e " +
            "WHERE e.enrollmentId = :enrollmentId")
    List<Attendance> findByEnrollmentId(@Param("enrollmentId") Long enrollmentId);

    @Query("SELECT a FROM Attendance a " +
            "JOIN FETCH a.enrollment e " +
            "JOIN FETCH e.user u " +
            "WHERE u.email = :email")
    List<Attendance> findByEmail(@Param("email")String email);

}
