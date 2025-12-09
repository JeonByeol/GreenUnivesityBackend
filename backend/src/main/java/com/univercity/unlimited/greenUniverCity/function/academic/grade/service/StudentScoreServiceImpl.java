package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.StudentScore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentScoreServiceImpl implements StudentScoreService{
    @Override
    public StudentScoreResponseDTO createStudentScore(StudentScoreCreateDTO dto) {
        return null;
    }

    @Override
    public StudentScoreResponseDTO getStudentScore(Long scoreId) {
        return null;
    }

    @Override
    public List<StudentScoreResponseDTO> getStudentScores(Long enrollmentId) {
        return List.of();
    }

    @Override
    public List<StudentScoreResponseDTO> getItemScores(Long itemId) {
        return List.of();
    }

    @Override
    public StudentScoreResponseDTO updateStudentScore(Long scoreId, StudentScoreUpdateDTO dto) {
        return null;
    }

    @Override
    public boolean checkAllScoreSubmitted(Long enrollmentId, Long offeringId) {
        return false;
    }

    @Override
    public Long countStudentScore(Long enrollmentId) {
        return 0L;
    }

    @Override
    public StudentScore getStudentScoreEntity(Long scoreId) {
        return null;
    }
}
