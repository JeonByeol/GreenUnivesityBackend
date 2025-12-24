package com.univercity.unlimited.greenUniverCity.function.academic.assignment.service;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission.SubmissionCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission.SubmissionResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.submission.SubmissionUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Submission;

import java.util.List;

public interface SubmissionService {
    // 주석-1) SubmissionController=SB

    //SB-1)(학생) 특정 과제에 대해 파일을 업로드하여 과제를 제출하는 서비스
    SubmissionResponseDTO submitAssignment(SubmissionCreateDTO dto, String email);

    //SB-2)(학생) 이미 제출한 과제 파일을 수정(재제출)하기 위한 서비스
    SubmissionResponseDTO updateSubmissionFile(SubmissionUpdateDTO dto, String email);

    //SB-3)(교수) 학생이 제출한 과제에 대해 점수를 부여(채점)하는 서비스
    SubmissionResponseDTO gradeSubmission(Long submissionId, Float score, String email);

    //SB-4)(교수) 특정 과제에 대한 모든 학생들의 제출 현황을 조회하는 서비스
    List<SubmissionResponseDTO> findAllSubmissionsByAssignment(Long assignmentId);

    //SB-5)(학생) 내가 제출한 과제 내역을 확인하기 위한 단건 조회 서비스
    SubmissionResponseDTO findMySubmission(Long assignmentId, String email);

    //SB-6) 전체조회 서비스
    List<SubmissionResponseDTO> findAllSubmissions();

    //SB-E) 외부 Service에서 Submission의 정보를 활용하기 위한 엔티티 조회 서비스
    Submission getSubmissionEntity(Long submissionId);
}
