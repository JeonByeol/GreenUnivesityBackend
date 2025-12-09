package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.LegacyGradeDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.Grade;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
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
        List<Grade> grades= repository.findByMyGrade(email);

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
    
    //G-1)성적 생성
    @Override
    public GradeResponseDTO createGrade(GradeCreateDTO dto) {
        log.info("2)성적 생성 시작 - enrollmentId-:{}",dto.getEnrollmentId());

        Enrollment enrollment=enrollmentService.getEnrollmentEntity(dto.getEnrollmentId());

        if(repository.existsByEnrollment_EnrollmentId(dto.getEnrollmentId())){
            throw new IllegalStateException("이미 성적 등록이 되어 있습니다.");
        }

        Grade grade=Grade.builder()
                .enrollment(enrollment)
                .totalScore(dto.getTotalScore())
                .letterGrade(dto.getLetterGrade())
                .build();

        Grade saveGrade = repository.save(grade);

        return toResponseDTO(saveGrade);
    }

    //G-2) 성적 단건 조회
    @Override
    public GradeResponseDTO getGrade(Long gradeId) {
        log.info("2) 성적 단건 조회 시작 - gradeId-:{}",gradeId);

        Grade grade = repository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("성적 정보를 찾을 수 없습니다"));

        return toResponseDTO(grade);
    }

    @Override
    public List<GradeResponseDTO> getStudentGrades(String email) {
        return List.of();
    }

    @Override
    public List<GradeResponseDTO> getOfferingGrades(Long offeringId) {
        return List.of();
    }

    @Override
    public GradeResponseDTO updateGrade(Long gradeId, GradeUpdateDTO dto) {
        return null;
    }

    @Override
    public GradeResponseDTO calculateAndSaveGrade(Long enrollmentId) {
        return null;
    }

    @Override
    public Grade getGradeEntity(Long gradeId) {
        return null;
    }

}
