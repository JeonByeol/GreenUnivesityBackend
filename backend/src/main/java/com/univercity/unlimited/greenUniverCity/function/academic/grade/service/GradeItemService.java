package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;

import java.util.List;

public interface GradeItemService {
    //GradeItemController-> GI

    //GI-1) 평가항목을 생성하기 위한 service 선언부 (교수)
    GradeItemResponseDTO createGradeItem(GradeItemCreateDTO dto, String professorEmail);

    //GI-2) 평가항목 테이블에 존재하는 데이터 하나 단건 조회를 위한 service 선언부
    GradeItemResponseDTO getGradeItem(Long itemId);

    //GI-3) 특정 강의에 존재하는 평가항목 목록을 조회하기 위한 service 선언부
    List<GradeItemResponseDTO> getOfferingGradeItem(Long offeringId);

    //GI-4) 기존에 존재하는 평가항목의 정보를 수정하기 위한 service 선언부 (교수)
    GradeItemResponseDTO updateGradeItem(Long itemId, GradeItemUpdateDTO dto, String professorEmail);

    //GI-5) 평가항목의 비율의 합계를 검증하기 위한 service 선언부
    boolean validateWeightSum(Long offeringId);

    //GI-6) 평가항목의 개수를 조회하는 다른 serviceImpl 구현부에서 사용하기 위한 service 선언부
    Long countOfferingGradeItems(Long offeringId);
    
    //GI-7) Entity 조회를 하여 외부 다른 serviceImpl 구현부에서 사용하기 위한 service 선언부
    GradeItem getGradeItemEntity(Long itemId);

}
