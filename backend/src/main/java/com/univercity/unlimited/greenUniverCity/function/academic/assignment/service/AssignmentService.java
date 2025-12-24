package com.univercity.unlimited.greenUniverCity.function.academic.assignment.service;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment.AssignmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment.AssignmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.dto.assignment.AssignmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Assignment;

import java.util.List;

public interface AssignmentService {
    //주석-1) AssignmentController=AS

    //AS-1)(공통) 특정 분반(Section)에 등록된 모든 과제 목록을 조회하는 서비스
    List<AssignmentResponseDTO> findAssignmentsBySectionId(Long sectionId);

    //AS-2)(관리자or교수) 특정 분반에 새로운 과제를 생성(출제)하기 위해 동작하는 서비스
    AssignmentResponseDTO createAssignment(AssignmentCreateDTO dto, String email);

    //AS-3)(관리자or교수) 기존에 출제한 과제의 내용이나 기한을 수정하기 위한 서비스
    AssignmentResponseDTO updateAssignment(AssignmentUpdateDTO dto, String email);

    //AS-4)(관리자or교수) 잘못 출제된 과제를 삭제하기 위한 서비스 (관련 제출물도 함께 삭제 고려)
    void deleteAssignment(Long assignmentId, String email);

    //AS-5)(공통) 과제 상세 내용을 확인하기 위한 단건 조회 서비스
    AssignmentResponseDTO getAssignment(Long assignmentId);

    //AS-ALL) 등록된 모든 과제 내용을 전체 조회하기 위한 서비스
    List<AssignmentResponseDTO> findAllAssignments();

    //AS-E) 외부 Service(예: 제출 서비스)에서 Assignment의 실체 정보를 사용하기 위한 엔티티 조회 서비스
    Assignment getAssignmentEntity(Long assignmentId);
}
