package com.univercity.unlimited.greenUniverCity.function.grade.service;


import com.univercity.unlimited.greenUniverCity.function.grade.dto.GradeDTO;
import com.univercity.unlimited.greenUniverCity.function.grade.dto.GradeProfessorDTO;
import com.univercity.unlimited.greenUniverCity.function.grade.dto.GradeStudentDTO;

import java.util.List;

public interface GradeService {
    List<GradeDTO> findAllGrade();

    List<GradeStudentDTO> myGrade(String email);

    List<GradeProfessorDTO> courseOfGrade(Long offeringId);

    GradeDTO postNewGrade(Long enrollmentId,String gradeValue);

    //    List<GradeDTO> myGrade2(String email,String courseName);
    //    GradeDTO postNewGrade(String professorEmail,Long enrollmentId,String gradeValue);
}
