package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade,Integer> {
//    @Query("select m from Grade m where m.gradeValue = :gradeValue" )
//    Grade findByGrade(@Param("gradeValue") String gardeValue);
//    Optional<Grade> findByEnroll (Enrollment enrollment);

    @Query("SELECT g FROM Grade g " +
            "JOIN g.user u " +
            "WHERE u.email = :email")
    List<Grade> findByStudent(@Param("email") String email);

}
