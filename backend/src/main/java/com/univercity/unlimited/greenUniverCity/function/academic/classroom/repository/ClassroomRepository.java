package com.univercity.unlimited.greenUniverCity.function.academic.classroom.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
    //CR-2 특정 강의실에 대한 정보를 조회하는 서비스 구현부에서 location의 값을 전달하기 위해 쿼리문 작성
    @Query("SELECT c FROM Classroom c WHERE c.location LIKE %:keyword%")
    List<Classroom> findByLocationContain(@Param("keyword") String keyword);
}
