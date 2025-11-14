package com.univercity.unlimited.greenUniverCity.service;


import com.univercity.unlimited.greenUniverCity.dto.grade.GradeDTO;
import com.univercity.unlimited.greenUniverCity.dto.grade.GradeProfessorDTO;
import com.univercity.unlimited.greenUniverCity.dto.grade.GradeStudentDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface GradeService {
    List<GradeDTO> findAllGrade();

    List<GradeStudentDTO> myGrade(String email);

    List<GradeProfessorDTO> courseOfGrade(Long offeringId);

    GradeDTO postNewGrade(Long enrollmentId,String gradeValue);

    //    List<GradeDTO> myGrade2(String email,String courseName);
    //    GradeDTO postNewGrade(String professorEmail,Long enrollmentId,String gradeValue);
}
