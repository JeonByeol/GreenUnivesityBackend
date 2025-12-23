package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;


import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.Grade;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItemType;

import java.util.List;

public interface GradeService {

    //New-G-1) 성적 테이블에 존재하는 전체 데이터를 조회하기 위한 service 선언부 (교수만 가능해야함)
    List<GradeResponseDTO> findAllGrades();

    //New-G-2) 성적의 단건 조회를 위한 service 선언부
    GradeResponseDTO getGrade(Long gradeId);

    //NEW-G-3) 학생별 모든 성적 조회를 위한 service 선언부 (학생 본인만 가능)
    List<GradeResponseDTO> getStudentGrades(String studentEmail, String requesterEmail);

    //NEW-G-4) 강의별 모든 학생에 대한 성적을 조회하기 위한 service 선언부 (담당 교수만 가능)
    List<GradeResponseDTO> getOfferingGrades(Long offeringId, String professorEmail);
    
    //NEW-G-5) 성적을 수정하기 위한 service 선언부 (교수만 가능)
    GradeResponseDTO updateGrade(Long gradeId, GradeUpdateDTO dto, String professorEmail);

    //New-G-6) 최종 성적 자동 계산 및 저장 (교수만 가능)
    GradeResponseDTO calculateAndSaveGrade(Long enrollmentId, String professorEmail);

    //NEW-G-7) 외부 Entity(GradeItem,StudentScore,...)등 다른 ServiceImpl에서 사용하기 위해 선언된 service 선언
    Grade getGradeEntity(Long gradeId);

//    //New-G-5) 성적을 생성하기 위한 service 선언부 (교수만 가능)
//    GradeResponseDTO createGrade(GradeCreateDTO dto, String professorEmail); 성적 생성은 StudentScore에서 하는 일 삭제예정


}
