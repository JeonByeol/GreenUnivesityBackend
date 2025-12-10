package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.StudentScore;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.StudentScoreRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentScoreServiceImpl implements StudentScoreService{
    
    private final StudentScoreRepository repository;
    
    private final EnrollmentService enrollmentService;
    
    private final GradeItemService itemService;

    private final AcademicSecurityValidator validator;

    /**
     * SS-A) StudentScore 엔티티를 Response DTO로 변환
     */

    private StudentScoreResponseDTO toResponseDTO(StudentScore studentScore){
        GradeItem item=studentScore.getGradeItem();
        Enrollment enrollment=studentScore.getEnrollment();

        return StudentScoreResponseDTO.builder()
                .scoreId(studentScore.getScoreId())
                .enrollmentId(enrollment.getEnrollmentId())
                .itemId(item.getItemId())
                .scoreObtained(studentScore.getScoreObtained())
                .createdAt(studentScore.getCreatedAt())
                .updatedAt(studentScore.getUpdatedAt())
                .build();
    }
    
    //SS-1) 학생 점수 생성
    @Override
    public StudentScoreResponseDTO createStudentScore(StudentScoreCreateDTO dto, String professorEmail) {
        log.info("2) 학생 점수 생성 시작 - enrollmentId-:{}, itemId-:{}, 교수-:{}",
                dto.getEnrollmentId(), dto.getItemId(), professorEmail);

        Enrollment enrollment= enrollmentService.getEnrollmentEntity(dto.getEnrollmentId());

        CourseOffering offering= enrollment.getCourseOffering();
        validator.validateProfessorOwnership(offering, professorEmail, "점수입력");

        GradeItem gradeItem = itemService.getGradeItemEntity(dto.getItemId());

        //만점 초과 검증
        if(dto.getScoreObtained() > gradeItem.getMaxScore()){
            throw new IllegalArgumentException(
                    "흭득 점수가 만점을 초과할 수 없습니다. 만점: " + gradeItem.getMaxScore());
        }

        //중복 점수 등록 확인
        if(repository.existByEnrollment_EnrollmentIdAndGradeItem_ItemId(
                dto.getEnrollmentId(),dto.getItemId())){
            throw new IllegalArgumentException("이미 해당 평가항목의 점수갇 등록되어 있습니다,");
        }

        StudentScore studentScore=StudentScore.builder()
                .enrollment(enrollment)
                .gradeItem(gradeItem)
                .scoreObtained(dto.getScoreObtained())
                .build();

        StudentScore saveScore =repository.save(studentScore);
        log.info("4) 학생 점수 생성 완료 -scoreId-:{}, 교수-:{}", saveScore.getScoreId(), professorEmail);

        return toResponseDTO(saveScore);
    }

    //SS-2) 학생 점수 단건 조회
    @Override
    public StudentScoreResponseDTO getStudentScore(Long scoreId) {
        log.info("2)학생 점수 조회 - scoreId-:{}", scoreId);

        StudentScore studentScore=repository.findById(scoreId)
                .orElseThrow(()->new IllegalArgumentException("점수 정보를 찾을 수 없습니다"));

        return toResponseDTO(studentScore);
    }
    
    //SS-3) 학생별 모든 점수 조회
    @Override
    public List<StudentScoreResponseDTO> getStudentScores(Long enrollmentId) {
        log.info("2) 학생 점수 목록 조회 시작 - enrollmentId-:{}", enrollmentId);

        List<StudentScore> scores= repository.findByEnrollmentId(enrollmentId);

        log.info("4) 학생 점수 목록 조회 성공 - enrollmentId-:{}, 점수개수-:{}", enrollmentId, scores.size());

        return scores.stream()
                .map(this::toResponseDTO)
                .toList();
    }
    
    //SS-4) 평가항목별 모든 학생 점수 조회
    @Override
    public List<StudentScoreResponseDTO> getItemScores(Long itemId, String professorEmail) {
        log.info("2) 평가 항목별 점수 조회 시작 - itemId-:{}, 교수-:{}", itemId, professorEmail);
        
        GradeItem gradeItem= itemService.getGradeItemEntity(itemId);
        CourseOffering offering= gradeItem.getCourseOffering();
        validator.validateProfessorOwnership(offering, professorEmail, "점수조회");

        List<StudentScore> scores = repository.findByItemId(itemId);

        log.info("4) 평가 항목별 점수 조회 성공 - itemId-:{}, 점수개수-:{}", itemId, scores.size());

        return scores.stream()
                .map(this::toResponseDTO)
                .toList();
    }
    
    //SS-5) 학생 점수 수정
    @Override
    public StudentScoreResponseDTO updateStudentScore(Long scoreId, StudentScoreUpdateDTO dto, String professorEmail) {
        log.info("2) 학생 점수 수정 시작 - scoreId-:{}, 교수-:{}", scoreId, professorEmail);

        StudentScore studentScore=repository.findById(scoreId)
                .orElseThrow(()->new IllegalArgumentException("점수 정보를 찾을 수 없습니다"));
        
        CourseOffering offering= studentScore.getEnrollment().getCourseOffering();
        validator.validateProfessorOwnership(offering, professorEmail, "점수수정");

        //만점 초과 검증
        if(dto.getScoreObtained() > studentScore.getGradeItem().getMaxScore()){
            throw new IllegalArgumentException("흭득 점수가 만전을 초과할 수 없습니다.");
        }

        studentScore.setScoreObtained(dto.getScoreObtained());

        StudentScore updateScore=repository.save(studentScore);

        log.info("5) 학생 점수 수정 성공 -scoreId-:{}, 교수-:{}", updateScore.getScoreId(), professorEmail);

        return toResponseDTO(updateScore);
    }

    //SS-6) 모든 점수 입력 확인(GradeItemService를 통해 평가항목 개수 조회)
    @Override

    public boolean checkAllScoreSubmitted(Long enrollmentId, Long offeringId) {
        log.info("2) 점수 입력 완료 확인 - enrollmentId-:{}, offeringId-:{}", enrollmentId, offeringId);

        //GradeItem을 통해 평가항목 개수 가져옴
        Long totalItems = itemService.countOfferingGradeItems(offeringId);

        //학생이 받은 점수 게수 가져오기
        Long submittedScores=repository.countByEnrollment_EnrollmentId(enrollmentId);

        boolean isComplete= totalItems == submittedScores;

        log.info("평가항목 개수-:{}, 입력된 점수 개수-:{}, 완료여부-:{}",
                totalItems, submittedScores, isComplete);

        return isComplete;
    }

    //SS-7) 학생 점수 개수 조회( 외부 service에서 사용)
    @Override
    public Long countStudentScore(Long enrollmentId) {
        return repository.countByEnrollment_EnrollmentId(enrollmentId);
    }

    //SS-8) StudentScore에 대한 정보를 조회(외부Service에서 사용)
    @Override
    public StudentScore getStudentScoreEntity(Long scoreId) {
        return repository.findById(scoreId)
                .orElseThrow(()->new IllegalArgumentException("점수 정보를 찾을 수 없습니다."));
    }
}
