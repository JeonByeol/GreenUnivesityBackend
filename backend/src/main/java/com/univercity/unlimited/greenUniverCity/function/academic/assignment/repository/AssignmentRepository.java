package com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    /**
     * AS-1) 특정 분반의 과제 목록 조회
     * - ClassSection 정보를 Fetch Join하여 N+1 문제 방지 (DTO 변환 시 sectionName 필요함)
     * - 마감 기한(dueDate) 오름차순 정렬 (빨리 마감되는 것부터 표시)
     */
    @Query("SELECT a FROM Assignment a " +
            "JOIN FETCH a.classSection cs " +
            "WHERE cs.sectionId = :sectionId " +
            "ORDER BY a.dueDate ASC")
    List<Assignment> findBySectionId(@Param("sectionId") Long sectionId);

    @Query("SELECT a FROM Assignment a " +
            "JOIN FETCH a.classSection cs " +
            "ORDER BY a.assignmentId ASC")
    List<Assignment> findAllWithFetchJoin();

    // (선택 사항) 특정 분반 삭제 시, 해당 분반의 모든 과제 삭제 (Service에서 사용 시)
    // @Modifying
    // @Query("DELETE FROM Assignment a WHERE a.classSection.sectionId = :sectionId")
    // void deleteBySectionId(@Param("sectionId") Long sectionId);
}
