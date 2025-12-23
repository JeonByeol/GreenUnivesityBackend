package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.StudentScore;

import java.util.List;

    public interface StudentScoreService {
    //StudentScoreController-> SS

    //SS-1) 학생에 대한 점수를 생성하기 위한 service 선언부 (교수)
    StudentScoreResponseDTO createStudentScore(StudentScoreCreateDTO dto, String professorEmail);

    //SS-2) 학생의 점수 하나를 단건 조회하기 위한 service 선언부
    StudentScoreResponseDTO getStudentScore(Long scoreId);

    //SS-3) 학생별 모든 점수를 조회하기 위한 service 선언부
    List<StudentScoreResponseDTO> getStudentScores(Long enrollmentId);

    //SS-4) 특정 평가항목별로 모든 학생의 점수를 조회하기 위한 service 선언부 (교수)
    List<StudentScoreResponseDTO> getItemScores(Long itemId, String professorEmail);
    
    //SS-5) 학생의 점수를 수정하기 위한 service 선언부
    StudentScoreResponseDTO updateStudentScore(Long scoreId, StudentScoreUpdateDTO dto, String professorEmail);

    //SS-6) 모든 점수 입력을 확인하기 위한 체크 service 선언부
    boolean checkAllScoreSubmitted(Long enrollmentId, Long offeringId);

    //SS-7) 학생의 점수 개수를 조회한다 외부 다른 serviceImpl에서 사용하기 위한 service 선언부
    Long countStudentScore(Long enrollmentId);

    //SS-8) 외부 다른 serviceImpl에서 StudentScore에 대한 정보를 조회하기 위한 service 선언부
    StudentScore getStudentScoreEntity(Long scoreId);

}
