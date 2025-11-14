package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.grade.GradeDTO;
import com.univercity.unlimited.greenUniverCity.dto.grade.GradeProfessorDTO;
import com.univercity.unlimited.greenUniverCity.dto.grade.GradeStudentDTO;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.repository.GradeRepository;
import com.univercity.unlimited.greenUniverCity.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GradeServiceImpl implements GradeService{

    private final GradeRepository repository;

    private final EnrollmentRepository enrollmentRepository;

    private final CourseOfferingRepository courseOfferingRepository;

    private final UserRepository userRepository;

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
    public List<GradeStudentDTO> myGrade(String email) {
//        List<Grade> grades= repository.findByMyGrade(email);
        List<Grade> grades= repository.findByMyGrade(email);
        return grades.stream()
                .map(g -> GradeStudentDTO.builder()
                        .gradeId(g.getGradeId())
                        .gradeValue(g.getGradeValue())
                        .courseName(g.getEnrollment().getCourseOffering().getCourse().getCourseName())
                        .courseId(g.getEnrollment().getCourseOffering().getOfferingId())
                        .studentName(g.getEnrollment().getUser().getNickname())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<GradeProfessorDTO> courseOfGrade(Long offeringId) {
        List<Grade> grades=repository.findByOfferingGrade(offeringId);
        return grades.stream()
                .map(g->GradeProfessorDTO.builder()
                        .gradeId(g.getGradeId())
                        .gradeValue(g.getGradeValue())
                        .studentName(g.getEnrollment().getUser().getNickname())
                        .build()
                )
                .collect(Collectors.toList());
    }

//        List<GradeDTO> dto=new ArrayList<>();
//        log.info("1) dto의 개수가 몇 개인가 :{}",dto);
//        for(Grade i:grades){
//            log.info("2) 여기는 어떻게 들어오는가 :{}",i);
//            GradeDTO r=mapper.map(i,GradeDTO.class);
//            log.info("3) r은 정상적인가 :{}",r);
//            dto.add(r);
//        }
//        return dto;
//    }


    @Override
    public GradeDTO postNewGrade(Long enrollmentId,String gradeValue) {

        Enrollment enrollment=enrollmentRepository.findById(enrollmentId)
                .orElseThrow(()->new EntityNotFoundException("이게 맞을까요?"+enrollmentId));

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
