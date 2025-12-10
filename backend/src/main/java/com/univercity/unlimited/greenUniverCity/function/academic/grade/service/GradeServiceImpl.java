package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.LegacyGradeDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.Grade;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    private final AcademicSecurityValidator validator;

    private final ModelMapper mapper;

    /**
     * G-A) Grade 엔티티를 (Response) 형식으로 변환하는 함수
     */
    private GradeResponseDTO illWishChainResponse(Grade grade){
        Enrollment enrollment=grade.getEnrollment();
        CourseOffering offering=enrollment.getCourseOffering();
        User user=enrollment.getUser();

        return
                GradeResponseDTO.builder()
                        .gradeId(grade.getGradeId())
                        .letterGrade(grade.getLetterGrade())
                        .courseName(offering.getCourseName())
//                        .courseId(offering.getOfferingId())
                        .studentName(user.getNickname())
                        .build();

    }

    //G-1)성적 전체 조회
    @Override
    public List<LegacyGradeDTO> findAllGrade() {
        log.info("전체 성적 조회");
        List<LegacyGradeDTO> dto=new ArrayList<>();
        for(Grade i:repository.findAll()){
            LegacyGradeDTO r=mapper.map(i, LegacyGradeDTO.class);
                dto.add(r);
        }
        return dto;
    }

    //G-2) 학생이 본인이 수강한 모든 과목의 성적과 과목명을 조회하기 위한 서비스 구현부 todo
    @Override
    public List<GradeResponseDTO> myGrade(String email) {
        List<Grade> grades= repository.findByStudentEmail(email);

        log.info("1)학생이 수강한 모든 과목의 성적을 조회하는 service가 맞냐:{}",grades);

        List<GradeResponseDTO> myGrade= grades.stream()
                .map(g -> {
                    Enrollment enrollment=g.getEnrollment();
                    CourseOffering offering=enrollment.getCourseOffering();
                    User user=enrollment.getUser();
//                    EnrollmentTestDTO info=
//                   enrollmentService.getEnrollmentForGrade(g.getEnrollment().getEnrollmentId());//todo E-2)
                    return
                            GradeResponseDTO.builder()
                        .gradeId(g.getGradeId())
                                    .letterGrade(g.getLetterGrade())
                        .courseName(offering.getCourseName())
                        .enrollmentId(g.getEnrollment().getEnrollmentId())
                        .studentName(user.getNickname())
                        .build();

        })
                .collect(Collectors.toList());

        return myGrade;
    }


    //G-3) 교수가 특정 과목의 수업을 듣는 전체학생 조회하기 위한 service 구현부
    @Override
    public List<GradeResponseDTO> offeringOfGrade(Long offeringId) {

        List<Grade> grades=repository.findByOfferingGrade(offeringId);

        return grades.stream()
                .map(g-> {
                    Enrollment enrollment=g.getEnrollment();
                    User user=enrollment.getUser();
                    return
                            GradeResponseDTO.builder()
                            .gradeId(g.getGradeId())
                                    .letterGrade(g.getLetterGrade())
                            .studentName(user.getNickname())
                            .build();
                })
                .collect(Collectors.toList());
    }


    //G-4) 교수가 학생에 대한 정보를 받아와서 성적의 대한 값을 수정하기 위한 service 구현부
    @Override
    public LegacyGradeDTO updateNewGrade(Long enrollmentId, String letterGrade) {

        //Enrollment enrollment1=enrollmentRepository.findByEnrollmentId(enrollmentId);
        Enrollment enrollment=enrollmentService.getEnrollmentEntity(enrollmentId);//E-2)

        //Service에서 전달된 교수의 email, 수강신청(enroll)에 연결된 과목(offering)의 담당 교수(user)의
        //email이 일치하는지 보안검사에 대한 코드 "feat Gemini"
//        User professor= enrollment.getCourseOffering().getUser();
//        if(professor == null || !professor.getEmail().equals(professorEmail)){
//            log.warn("권한이 없다: 교수[{}]가 타 과목(Id:{}) 성적 입력을 시도.",professorEmail,enrollmentId);
//            throw new IllegalArgumentException("이 과목의 성적을 입력할 권한이 없습니다.");
//        }

        //성적과 연결된 enrollmentId를 찾고 없으면 신규 객체를 생성한다
        Grade grade=repository.findByEnrollment_enrollmentId(enrollmentId)
                .orElse(new Grade());

        grade.setLetterGrade(letterGrade);
        grade.setEnrollment(enrollment);

        Grade saveGrade= repository.save(grade);

        log.info("성공:  학생 [{}]에게 성적 [{}] 입력 완료",
                 enrollment.getUser().getEmail(), letterGrade);

        return mapper.map(saveGrade, LegacyGradeDTO.class);
    }

    private GradeResponseDTO toResponseDTO(Grade grade){
        Enrollment enrollment=grade.getEnrollment();

        return GradeResponseDTO.builder()
                .gradeId(grade.getGradeId())
                .enrollmentId(enrollment.getEnrollmentId())
                .totalScore(grade.getTotalScore())
                .letterGrade(grade.getLetterGrade())
                .createdAt(grade.getCreatedAt())
                .updatedAt(grade.getUpdatedAt())
                .build();
    }
    
    //G-1)성적 생성 (교수)
    @Override
    public GradeResponseDTO createGrade(GradeCreateDTO dto, String professorEmail) {
        log.info("2)성적 생성 시작 - enrollmentId-:{}, 교수-:{}", dto.getEnrollmentId(), professorEmail);

        Enrollment enrollment=enrollmentService.getEnrollmentEntity(dto.getEnrollmentId());

        if(repository.existsByEnrollment_EnrollmentId(dto.getEnrollmentId())){
            throw new IllegalStateException("3) 이미 성적이 등록되어 있습니다.");
        }

        CourseOffering offering = enrollment.getCourseOffering();
        validator.validateProfessorOwnership(offering, professorEmail, "성적생성");

        Grade grade=Grade.builder()
                .enrollment(enrollment)
                .totalScore(dto.getTotalScore())
                .letterGrade(dto.getLetterGrade())
                .build();

        Grade saveGrade = repository.save(grade);

        log.info("5) 성적 생성 완료 - gradeId-:{}, 교수-:{}",
                saveGrade.getGradeId(), professorEmail);

        return toResponseDTO(saveGrade);
    }

    //G-2) 성적 단건 조회
    @Override
    public GradeResponseDTO getGrade(Long gradeId) {
        log.info("2) 성적 단건 조회 시작 - gradeId-:{}", gradeId);

        Grade grade = repository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("성적 정보를 찾을 수 없습니다"));

        return toResponseDTO(grade);
    }

    //G-3) 학생의 모든 성적 조회 (학생)
    @Override
    public List<GradeResponseDTO> getStudentGrades(String studentEmail, String requesterEmail) {
        log.info("2) 학생 성적 조회 시작 - 학생-:{}, 요청자-:{}",
                studentEmail, requesterEmail);

        if(!studentEmail.equals(requesterEmail)){
            throw new IllegalArgumentException("3) 본인의 성적만 조회할 수 있습니다,");
        }

        List<Grade> grades= repository.findByStudentEmail(studentEmail);

        log.info("3) 학생 성적 조회 완료 - email-:{}, 성적개수-:{}",
                studentEmail, grades.size());

        return grades.stream()
                .map(this::toResponseDTO)
                .toList();
    }
    
    //G-4) 강의별 모든 학생에 대한 성적 조회 (교수)
    @Override
    public List<GradeResponseDTO> getOfferingGrades(Long offeringId, String professorEmail) {
        log.info("2) 특정 강의에 대한 성적 조회 시작 - offeringId-:{}, 교수-:{}",
                offeringId, professorEmail);

        CourseOffering offering=offeringService.getCourseOfferingEntity(offeringId);
        validator.validateProfessorOwnership(offering,professorEmail,"성적 조회");
        
        List<Grade> grades=repository.findByOfferingGrade(offeringId);

        log.info("3) 특정 강의에 대한 성적 조회 완료 - offeringId-:{}, 성적개수-:{}",
                offeringId, grades.size());

        return grades.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //G-5) 성적 수정 (교수)
    @Override
    public GradeResponseDTO updateGrade(Long gradeId, GradeUpdateDTO dto, String professorEmail) {
        log.info("2) 성적 수정 시작 - gradeId-:{}, 교수-:{}",
                gradeId, professorEmail);

        Grade grade=repository.findById(gradeId)
                .orElseThrow(()->new IllegalArgumentException("3) 성적 정보를 찾을 수 없습니다."));

        CourseOffering offering = grade.getEnrollment().getCourseOffering();
        validator.validateProfessorOwnership(offering, professorEmail, "성적수정");

        grade.setTotalScore(dto.getTotalScore());
        grade.setLetterGrade(dto.getLetterGrade());

        Grade updateGrade= repository.save(grade);

        log.info("3) 성적 수정 완료 - gradeId-:{}, 교수-:{}",
                updateGrade.getGradeId(), professorEmail);

        return toResponseDTO(updateGrade);
    }

    //G-6) 최종 성적 자동 계산 및 저장(StudentScore를 통해 점수 조회 후 평균 계산 (교수)
    @Override
    public GradeResponseDTO calculateAndSaveGrade(Long enrollmentId, String professorEmail) {

        log.info("2) 최종 성적 자동 계산 시작 - enrollmentId-:{}, 교수-:{}",
                enrollmentId, professorEmail);

        Enrollment enrollment= enrollmentService.getEnrollmentEntity(enrollmentId);

        CourseOffering offering= enrollment.getCourseOffering();
        validator.validateProfessorOwnership(offering, professorEmail, "성적계산");

        List<StudentScoreResponseDTO> scoreResponseDTOS=
                scoreService.getStudentScores(enrollmentId);

        if(scoreResponseDTOS.isEmpty()){
            throw new IllegalStateException("입력된 점수가 존재하지 않습니다");
        }

        // 가중 평균 계산
        Float totalScore =calculateWeightedAverage(scoreResponseDTOS);

        //등급 계산
        String letterGrade= Grade.calculateGrade(totalScore);

        Grade grade=repository.findByEnrollment_enrollmentId(enrollmentId)
                .orElse(Grade.builder()
                        .enrollment(enrollment)
                        .build());

        grade.setTotalScoreAndCalculateGrade(totalScore);

        Grade saveGrade= repository.save(grade);

        log.info("5) 최종 성적 계산 완료 - totalScore-:{}, letterGrade-:{}, 교수-:{}",
                totalScore, letterGrade, professorEmail);

        return toResponseDTO(saveGrade);
    }
    
    //G-8 외부 Service에서 grade에 대한 정보 조회
    @Override
    public Grade getGradeEntity(Long gradeId) {
        return repository.findById(gradeId)
                .orElseThrow(()->new IllegalArgumentException("성적 정보를 찾을 수 없습니다."));
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
