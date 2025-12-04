package com.univercity.unlimited.greenUniverCity.function.academic.section.service;

import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionUpdateDTO;

import java.util.List;

public interface ClassSectionService {
    // 주석-1) ClassSectionController=SE

    //SE-1) SE에 선언된 postmanTestClassSection의 요청을 받아서 세션 테이블에 존재하는 전체 데이터를 조회하기 위해 동작하는 서비스 선언
    List<ClassSectionResponseDTO> findAllSection();

    //SE-2)(공통) SE에 선언된 postmanTest의 요청을 받아서 강의별 분반에 대한 목록을 조회하기 위해 동작하는 서비스를 선언
    List<ClassSectionResponseDTO> findSectionsByOfferingId(Long offeringId);

    //SE-3)(관리자or교수) SE에 선언된 postmanTestCreateSection의 요청을 받아서 특정 강의에 새로운 분반을 생성하기 위해 동작하는 서비스를 선언
    ClassSectionResponseDTO createSection(ClassSectionCreateDTO dto,String email);

    //SE-4)(관리자or교수) SE에 선언된 postmanTestUpdateSection의 요청을 받아서 특정 강의
    ClassSectionResponseDTO updateSection(ClassSectionUpdateDTO dto,String email);

    //SE-5)(관리자or교수) SE에 선언된 postmanTestUpdateSection의 요청을 받아서 특정 강의를 잘못만들었을때 삭제하기 위한 서비스를 선언
    void deleteSection(Long sectionId, String email);
}
