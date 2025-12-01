package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;


import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.GradeDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.GradeProfessorDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.GradeStudentDTO;

import java.util.List;

public interface GradeService {
    //G-1) 성적 테이블에 존재하는 전체 데이터 조회하기 위한 service
    List<GradeDTO> findAllGrade();

    //G-2) 학생이 본인이 수강한 모든 과목의 성적과 과목명을 조회하기 위한 service
    List<GradeStudentDTO> myGrade(String email);

    //G-3) 교수가 특정 과목의 수업을 듣는 전체학생 조회하기 위한 service
    List<GradeProfessorDTO> offeringOfGrade(Long offeringId);

    //G-4) 교수가 학생에 대한 정보를 받아와서 성적의 대한 값을 수정하기 위한 service
    GradeDTO updateNewGrade(Long enrollmentId,String gradeValue);

}
