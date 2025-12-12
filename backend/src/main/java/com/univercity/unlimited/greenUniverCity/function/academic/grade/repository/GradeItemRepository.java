package com.univercity.unlimited.greenUniverCity.function.academic.grade.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeItemRepository extends JpaRepository<GradeItem,Long> {
    
    //특정 강의의 모든 평가항목을 조회하기 위한 쿼리문 작성
    @Query("SELECT gi FROM GradeItem gi " +
            "WHERE gi.courseOffering.offeringId = :offeringId " +
            "ORDER BY gi.weightPercent DESC")
    List<GradeItem> findByOfferingId(@Param("offeringId") Long offeringId);

    //특정 강의의 평가항목의 개수를 조회하기 위한 쿼리문 작성
    Long countByCourseOffering_OfferingId(Long offeringId);

    //특정 강의에 같은 이름의 평가항목이 존재하는지 확인하기 위한 쿼리문(중복방지)
    boolean existsByCourseOffering_OfferingIdAndItemName(Long offeringId,String itemName);
}
