package com.univercity.unlimited.greenUniverCity.function.academic.section.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassSectionRepository extends JpaRepository<ClassSection,Long> {

    //SE-2)에 선언된 서비스 구현부에서 offeringId를 전달하기 위해 작성된 쿼리
    @Query("SELECT c FROM ClassSection c " +
            "JOIN FETCH c.courseOffering co " +
            "WHERE co.offeringId =:offeringId")
    List<ClassSection> findSectionByOfferingId(@Param("offeringId") Long offeringId);



}
