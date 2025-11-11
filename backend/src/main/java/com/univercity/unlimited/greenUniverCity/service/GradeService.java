package com.univercity.unlimited.greenUniverCity.service;


import com.univercity.unlimited.greenUniverCity.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.dto.GradeDTO;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface GradeService {
    List<GradeDTO> findAllGrade();

    ResponseEntity<String> addGrade(GradeDTO gradeDTO);

    Optional<List<GradeDTO>> findAllGradeDTO();

    List<GradeDTO> findMyGrade(String email);
}
