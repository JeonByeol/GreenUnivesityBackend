package com.univercity.unlimited.greenUniverCity.function.academic.attendance.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository <Attendance,Long>{

    // [New] N+1 문제 해결을 위한 Fetch Join 메서드
    // Attendance -> Enrollment -> User 까지 한 번에 가져옵니다.
    @Query("SELECT a FROM Attendance a " +
            "JOIN FETCH a.enrollment e " +
            "JOIN FETCH e.user u " +
            "ORDER BY a.attendanceId ASC")
    List<Attendance> findAllWithDetails();

    @Query("SELECT a FROM Attendance a " +
            "JOIN FETCH a.enrollment e " +
            "WHERE e.enrollmentId = :enrollmentId")
    List<Attendance> findByEnrollmentId(@Param("enrollmentId") Long enrollmentId);

    @Query("SELECT a FROM Attendance a " +
            "JOIN FETCH a.enrollment e " +
            "JOIN FETCH e.user u " +
            "WHERE u.email = :email")
    List<Attendance> findByEmail(@Param("email")String email);

    // 3. [교수용] 특정 강의(Offering)에 대한 모든 학생의 출결 조회
    // (Attendance -> Enrollment -> ClassSection -> CourseOffering -> OfferingId 경로)
    // * 복잡한 경로는 JPQL @Query로 푸는 게 가장 깔끔합니다!
    @Query("SELECT a FROM Attendance a " +
            "JOIN a.enrollment e " +
            "JOIN e.classSection s " +
            "JOIN s.courseOffering o " +
            "WHERE o.offeringId = :offeringId")
    List<Attendance> findByOfferingId(@Param("offeringId") Long offeringId);
}
