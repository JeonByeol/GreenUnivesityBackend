package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.dto.GradeDTO;
import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService{
    private final GradeRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<GradeDTO> findAllGrade() {
        log.info("전체 성적 조회");
        List<GradeDTO> dto=new ArrayList<>();
        for(Grade i:repository.findAll()){
            GradeDTO r=mapper.map(i,GradeDTO.class);
            dto.add(r);
        }
        return dto;
    }

    @Override
    public ResponseEntity<String> addGrade(GradeDTO gradeDTO) {
        return null;
    }

    @Override
    public Optional<List<GradeDTO>> findAllGradeDTO() {
        List<Grade> grades = repository.findAll();
        List<GradeDTO> gradeDTOS = grades.stream().map(grade ->
                mapper.map(grade, GradeDTO.class)).toList();

        Optional<List<GradeDTO>> optionalGradeDTOS = Optional.of(gradeDTOS);
        return optionalGradeDTOS;
    }
}
