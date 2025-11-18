package com.univercity.unlimited.greenUniverCity.function.grade.repository;


import com.univercity.unlimited.greenUniverCity.function.grade.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade,Integer> {
    @Query("SELECT g FROM Grade g " +
            "JOIN FETCH g.enrollment e " +
            "JOIN FETCH e.courseOffering co " +
            "JOIN e.user u " +
            "WHERE u.email = :email")
    List<Grade> findByMyGrade(@Param("email")String email); //G-2)

    @Query("SELECT g FROM Grade g " +
            "JOIN g.enrollment e " +
            "WHERE e.courseOffering.offeringId =:offeringId")
    List<Grade> findByOfferingGrade(@Param("offeringId") Long offeringId); //G-3)

    Optional<Grade> findByEnrollment_enrollmentId(Long enrollmentId);//G-4)




}
