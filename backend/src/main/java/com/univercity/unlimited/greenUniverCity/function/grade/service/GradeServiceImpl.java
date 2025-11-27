package com.univercity.unlimited.greenUniverCity.function.grade.service;

import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.grade.repository.GradeRepository;
import com.univercity.unlimited.greenUniverCity.function.grade.dto.GradeDTO;
import com.univercity.unlimited.greenUniverCity.function.grade.dto.GradeProfessorDTO;
import com.univercity.unlimited.greenUniverCity.function.grade.dto.GradeStudentDTO;
import com.univercity.unlimited.greenUniverCity.function.grade.entity.Grade;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
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
    private GradeProfessorDTO illWishChainResponse(Grade grade){
        Enrollment enrollment=grade.getEnrollment();
        CourseOffering offering=enrollment.getCourseOffering();
        User user=enrollment.getUser();

        return
                GradeProfessorDTO.builder()
                        .gradeId(grade.getGradeId())
                        .gradeValue(grade.getGradeValue())
                        .courseName(offering.getCourseName())
                        .courseId(offering.getOfferingId())
                        .studentName(user.getNickname())
                        .build();

    }

    //G-1)성적 전체 조회
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

    //G-2) 학생이 본인이 수강한 모든 과목의 성적과 과목명을 조회하기 위한 서비스 구현부 todo
    @Override
    public List<GradeStudentDTO> myGrade(String email) {
        List<Grade> grades= repository.findByMyGrade(email);

        log.info("1)학생이 수강한 모든 과목의 성적을 조회하는 service가 맞냐:{}",grades);

        List<GradeStudentDTO> myGrade= grades.stream()
                .map(g -> {
                    Enrollment enrollment=g.getEnrollment();
                    CourseOffering offering=enrollment.getCourseOffering();
                    User user=enrollment.getUser();
//                    EnrollmentTestDTO info=
//                   enrollmentService.getEnrollmentForGrade(g.getEnrollment().getEnrollmentId());//todo E-2)
                    return
                        GradeStudentDTO.builder()
                        .gradeId(g.getGradeId())
                        .gradeValue(g.getGradeValue())
                        .courseName(offering.getCourseName())
                        .courseId(offering.getOfferingId())
                        .studentName(user.getNickname())
                        .build();

        })
                .collect(Collectors.toList());

        return myGrade;
    }


    //G-3) 교수가 특정 과목의 수업을 듣는 전체학생 조회하기 위한 service 구현부
    @Override
    public List<GradeProfessorDTO> offeringOfGrade(Long offeringId) {

        List<Grade> grades=repository.findByOfferingGrade(offeringId);

        return grades.stream()
                .map(g-> {
                    Enrollment enrollment=g.getEnrollment();
                    User user=enrollment.getUser();
                    return
                    GradeProfessorDTO.builder()
                            .gradeId(g.getGradeId())
                            .gradeValue(g.getGradeValue())
                            .studentName(user.getNickname())
                            .build();
                })
                .collect(Collectors.toList());
    }


    //G-4) 교수가 학생에 대한 정보를 받아와서 성적의 대한 값을 수정하기 위한 service 구현부
    @Override
    public GradeDTO updateNewGrade(Long enrollmentId,String gradeValue) {

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

        grade.setGradeValue(gradeValue);
        grade.setEnrollment(enrollment);

        Grade saveGrade= repository.save(grade);

        log.info("성공:  학생 [{}]에게 성적 [{}] 입력 완료",
                 enrollment.getUser().getEmail(), gradeValue);

        return mapper.map(saveGrade,GradeDTO.class);
    }

}
