package com.univercity.unlimited.greenUniverCity.function.academic.section.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassSectionRepository extends JpaRepository<ClassSection,Long> {

    //분반 전체 목록 조회
    @Query("SELECT DISTINCT cs FROM ClassSection cs " +
            "JOIN FETCH cs.courseOffering co " +
            "JOIN FETCH co.course c " +
            "JOIN FETCH co.professor p " +
            "LEFT JOIN FETCH cs.timeTables tt " +   // 시간표 조인 (없을 수도 있으니 LEFT)
            "LEFT JOIN FETCH tt.classroom cr")      // 시간표 안의 강의실 조인
    List<ClassSection> findAllWithDetails();

    //SE-2)에 선언된 서비스 구현부에서 특정 강의(Offering)에 속한 분반 목록 조회하기 위한 쿼리
    @Query("SELECT DISTINCT cs FROM ClassSection cs " +
            "JOIN FETCH cs.courseOffering co " +
            "JOIN FETCH co.course c " +
            "JOIN FETCH co.professor p " +
            "LEFT JOIN FETCH cs.timeTables tt " +
            "LEFT JOIN FETCH tt.classroom cr " +
            "WHERE co.offeringId = :offeringId")
    List<ClassSection> findSectionByOfferingId(@Param("offeringId") Long offeringId);

}
