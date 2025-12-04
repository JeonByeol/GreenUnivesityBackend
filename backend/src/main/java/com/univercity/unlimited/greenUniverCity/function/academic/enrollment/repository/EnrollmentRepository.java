package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    // -- 전체 Entity --

    //E-2/E-4)Enrollment에 대한 id를 찾음/다른 service에서 enroll에대한 id값을 알기 위한 쿼리 선언
    Enrollment findByEnrollmentId(Long id);

    // -- ClassSection --

    //분반별 수강 인원 집계 Projection
    interface SectionCountSummary {
        Long getSectionId();//분반 id
        Long getCount();//수강인원수
    }

    //E.SE-1)ClassSection에  존재하는 단일분반의 maxCapacity(정원)을 실시간으로 조회하기 위해 선언된 쿼리
    Integer countByClassSection_SectionId(Long sectionId);

    //E.SE-2)ClassSection의 존재하는 복수분반의 maxCapacity(정원)을 실시간으로 조회하기 위해 선언된 쿼리
    @Query("SELECT e.classSection.sectionId AS sectionId, COUNT(e) AS count " +
            "FROM Enrollment e " +
            "WHERE e.classSection.sectionId IN :sectionIds " +
            "GROUP BY e.classSection.sectionId")
    List<SectionCountSummary> countBySectionIds(@Param("sectionIds") List<Long> sectionIds);

}
