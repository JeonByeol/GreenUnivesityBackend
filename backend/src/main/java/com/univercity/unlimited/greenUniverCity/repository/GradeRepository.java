package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.dto.CourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade,Integer> {
//    @Query("SELECT g FROM Grade g " +
//            "JOIN g.user u " +
//            "WHERE u.email = :email")
//    List<Grade> findByStudent(@Param("email") String email); ***없앨예정***

    @Query("SELECT g FROM Grade g " +
            "JOIN FETCH g.enrollment e " +
            "JOIN FETCH e.courseOffering co " +
            "JOIN e.user u " +
            "WHERE u.email = :email")
    List<Grade> findByMyGrade(@Param("email")String email);

    Optional<Grade> findByEnrollment_enrollmentId(Long enrollmentId);

    @Query("SELECT g FROM Grade g J" +
            "OIN g.enrollment e " +
            "WHERE e.courseOffering.offeringId =:offeringId")
    List<Grade> findByOfferingGrade(@Param("offeringId") Long offeringId);


}
