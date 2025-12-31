package com.univercity.unlimited.greenUniverCity.function.academic.offering.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseOfferingRepository extends JpaRepository<CourseOffering,Long> {
    //CO-3)CourseOffering에 대한 id를 찾음/다른 service에서 enroll에대한 id값을 알기 위한 쿼리 선언
    CourseOffering findByOfferingId(Long id);

    @Query("""
        select co
        from CourseOffering co
        join co.professor p
        where p.email = :email
    """)
    List<CourseOffering> findByUserEmail(@Param("email") String email);

    //이메일로 검색하기 위한 쿼리
    @Query("SELECT c FROM CourseOffering c JOIN FETCH c.professor p WHERE p.email = :email")
    List<CourseOffering> findAllByProfessorEmail(@Param("email") String email);

}