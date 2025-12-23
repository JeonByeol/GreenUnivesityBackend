package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItemType;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.Grade;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GradeServiceImpl implements GradeService{

    private final GradeRepository repository;
    private final EnrollmentService enrollmentService;
    private final StudentScoreService scoreService;
    private final GradeItemService itemService;
    private final CourseOfferingService offeringService;
    private final EntityMapper entityMapper;
    private final AcademicSecurityValidator validator;

    //G-1) 성적 테이블에 존재하는 모든 데이터를 조회하기 위한 service구현부 ** 교수or관리자 ** 권한만 가능해야함
    @Override
    @Transactional(readOnly = true)
    public List<GradeResponseDTO> findAllGrades() {
        log.info("2) 성적 테이블에 존재하는 전체 데이터 조회 시작");

        return repository.findAllWithDetails().stream()
                .map(entityMapper::toGradeResponseDTO)
                .toList();
    }

    //G-2) 성적 단건 조회
    @Override
    @Transactional(readOnly = true)
    public GradeResponseDTO getGrade(Long gradeId) {
        log.info("2) 성적 단건 조회 시작 - gradeId-:{}", gradeId);

        Grade grade = validator.getEntityOrThrow(repository, gradeId, "성적");

        return entityMapper.toGradeResponseDTO(grade);
    }

    //G-3) 학생의 모든 성적 조회 (학생)
    @Override
    @Transactional(readOnly = true)
    public List<GradeResponseDTO> getStudentGrades(String studentEmail, String requesterEmail) {
        log.info("2) 학생 성적 조회 시작 - 학생-:{}, 요청자-:{}",
                studentEmail, requesterEmail);

        validator.validateOwner(studentEmail, requesterEmail, "본인의 성적만 조회할 수 있습니다.");

        List<Grade> grades= repository.findByStudentEmail(studentEmail);
        log.info("3) 학생 성적 조회 완료 - email-:{}, 성적개수-:{}",
                studentEmail, grades.size());

        return grades.stream()
                .map(entityMapper::toGradeResponseDTO)
                .toList();
    }
    
    //G-4) 강의별 모든 학생에 대한 성적 조회 (교수)
    @Override
    @Transactional(readOnly = true)
    public List<GradeResponseDTO> getOfferingGrades(Long offeringId, String professorEmail) {
        log.info("2) 특정 강의에 대한 성적 조회 시작 - offeringId-:{}, 교수-:{}",
                offeringId, professorEmail);

        CourseOffering offering=offeringService.getCourseOfferingEntity(offeringId);
        validator.validateProfessorOwnership(offering,professorEmail,"성적 조회");
        
        List<Grade> grades=repository.findByOfferingGrade(offeringId);

        log.info("3) 특정 강의에 대한 성적 조회 완료 - offeringId-:{}, 성적개수-:{}",
                offeringId, grades.size());

        return grades.stream()
                .map(entityMapper::toGradeResponseDTO)
                .toList();
    }

    //G-6) 성적 수정 (교수)
    @Override
    public GradeResponseDTO updateGrade(Long gradeId, GradeUpdateDTO dto, String professorEmail) {
        log.info("2) 성적 수정 시작 - gradeId-:{}, 교수-:{}",
                gradeId, professorEmail);

        Grade grade = validator.getEntityOrThrow(repository, gradeId, "성적");

        CourseOffering offering = grade.getEnrollment().getClassSection().getCourseOffering();
        validator.validateProfessorOwnership(offering, professorEmail, "성적수정");

        grade.updateGradeInfo(dto.getTotalScore(), dto.getLetterGrade());

        Grade updateGrade= repository.save(grade);

        log.info("3) 성적 수정 완료 - gradeId-:{}, 교수-:{}",
                updateGrade.getGradeId(), professorEmail);

        return entityMapper.toGradeResponseDTO(updateGrade);
    }

    //G-7) 최종 성적 자동 계산 및 저장(StudentScore를 통해 점수 조회 후 평균 계산 (교수)
    @Override
    public GradeResponseDTO calculateAndSaveGrade(Long enrollmentId, String professorEmail) {

        log.info("2) 최종 성적 자동 계산 시작 - enrollmentId-:{}, 교수-:{}",
                enrollmentId, professorEmail);

        Enrollment enrollment= enrollmentService.getEnrollmentEntity(enrollmentId);

        CourseOffering offering= enrollment.getClassSection().getCourseOffering();
        validator.validateProfessorOwnership(offering, professorEmail, "성적계산");

        Long totalItems = itemService.countOfferingGradeItems(offering.getOfferingId());
        Long submittedScores = scoreService.countStudentScore(enrollmentId);

        if (!totalItems.equals(submittedScores)) {
            throw new IllegalStateException(
                    String.format("아직 채점되지 않은 항목이 있습니다. (전체: %d개, 입력: %d개)", totalItems, submittedScores));
        }

        List<StudentScoreResponseDTO> scoreResponseDTOS= scoreService.getStudentScores(enrollmentId);
        validator.validateNotEmpty(scoreResponseDTOS.isEmpty(), "입력된 점수가 없어 계산할 수 없습니다.");

        // 가중 평균 계산
        Float totalScore =calculateWeightedAverage(scoreResponseDTOS);

        //[갱신] 기존 성적이 있으면 업데이트, 없으면 생성
        Grade grade = repository.findByEnrollment_enrollmentId(enrollmentId)
                .orElse(Grade.builder().enrollment(enrollment).build());

        grade.setTotalScoreAndCalculateGrade(totalScore);

        Grade saveGrade= repository.save(grade);

        log.info("5) 최종 성적 계산 완료 - 총점-:{}, 등급-:{}, 교수-:{}",
                totalScore, grade.getLetterGrade(), professorEmail);

        return entityMapper.toGradeResponseDTO(saveGrade);
    }
    
    //G-8 외부 Service에서 grade에 대한 정보 조회
    @Override
    @Transactional(readOnly = true)
    public Grade getGradeEntity(Long gradeId) {
        return validator.getEntityOrThrow(repository, gradeId, "성적");
    }


    /**
     * 가중 평균 계산 헬퍼 메서드(StudentScoreResponse)를 가져와서 계산
     */

    private Float calculateWeightedAverage(List<StudentScoreResponseDTO> scoreResponseDTOS) {
        float totalWeightedScore = 0.0f; //최종 점수 누적용
        float totalWeight = 0.0f; // 가중치 합계 누적용

        //각 점수에 대해 가중치 적용
        for (StudentScoreResponseDTO scoreResponseDTO : scoreResponseDTOS) {
            //GradeItemService를 통해 평가항목 정보 조회
            GradeItemResponseDTO itemResponseDTO =
                    itemService.getGradeItem(scoreResponseDTO.getItemId());

            // 100점 만점을 기준으로 환산 (획득점수 / 만점)
            float percentageScore =
                    (scoreResponseDTO.getScoreObtained() / itemResponseDTO.getMaxScore()) * 100;
            // 가중치 반영 점수 계산 
            float weightedScore =  percentageScore * (itemResponseDTO.getWeightPercent()/100);
            
            // 누적
            totalWeightedScore += weightedScore;
            totalWeight += itemResponseDTO.getWeightPercent();
        }

        //가중치 합이 100이 아닐 경우 비율 조정
        if(totalWeight > 0 && totalWeight != 100){
            totalWeightedScore =(totalWeightedScore/totalWeight) * 100;
        }

        return totalWeightedScore;
    }

}
