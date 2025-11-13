package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade,Integer> {
//    @Query("select m from Grade m where m.gradeValue = :gradeValue" )
//    Grade findByGrade(@Param("gradeValue") String gardeValue);
//    Optional<Grade> findByEnroll (Enrollment enrollment);

//    @Query("SELECT g FROM Grade g " +
//            "JOIN g.user u " +
//            "WHERE u.email = :email")
//    List<Grade> findByStudent(@Param("email") String email); ***없앨예정***

//    @Query("SELECT u FROM UserVo u"+
//            "JOIN u.grades g" +
//            "JOIN .")
    @Query("SELECT g FROM Grade g " +
            "JOIN FETCH g.enrollment e " +
            "JOIN FETCH e.courseOffering co " +
            "JOIN e.user u " +
            "WHERE u.email = :email")
    List<Grade> findByMyGrade(@Param("email")String email);

//    @Query("SEELCT g FROM Grade g " +
//            "JOIN FETCH g.enrollment e " +
//            "JOIN FETCH e.courseOffering co " +
//            "JOIN e.courseOffering of " +
//            "WHERE of.courseName= :courseName")
//    List<Grade> postStudentGrade(@Param("courseName")String courseName);

}
