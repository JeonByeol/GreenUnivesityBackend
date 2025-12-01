package com.univercity.unlimited.greenUniverCity.function.enrollment.service;

import com.univercity.unlimited.greenUniverCity.function.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.LegacyEnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.enrollment.exception.EnrollmentNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.enrollment.exception.UserNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.offering.exception.CourseOfferingNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository repository;

    private final ModelMapper mapper;

    private final CourseOfferingService offeringService;
    private final UserService userService;

    private EnrollmentResponseDTO toResponseDTO(Enrollment enrollment){
        User user = enrollment.getUser();
        CourseOffering offering = enrollment.getCourseOffering();

        return
                EnrollmentResponseDTO.builder()
                        .enrollmentId(enrollment.getEnrollmentId())
                        .offeringId(offering != null ?  offering.getOfferingId() : -1)
                        .enrollDate(enrollment.getEnrollDate())
                        .userId(user != null ? user.getUserId() : -1)
                        .build();
    }

    //E-1)Enroll에 존재하는 모든 데이터 조회 서비스 구현부
    @Override
    public List<LegacyEnrollmentDTO> legacyFindAllEnrollment() {
        List<LegacyEnrollmentDTO> dtoList = new ArrayList<>();
        for (Enrollment i : repository.findAll()) {
            LegacyEnrollmentDTO r = mapper.map(i, LegacyEnrollmentDTO.class);
            dtoList.add(r);
        }
        return dtoList;
    }

    @Override
    public List<EnrollmentResponseDTO> findAllEnrollment() {
        log.info("2) Enrollment 전체조회 시작");
        List<Enrollment> enrollments = repository.findAll();

        log.info("3) Enrollment 전체조회 성공");

        return enrollments.stream()
                .map(this::toResponseDTO).toList();
    }

    @Override
    public List<EnrollmentResponseDTO> findById(Long enrollmentId) {
        log.info("2) Enrollment 한개조회 시작 , enrollmentId : {}",enrollmentId);

        Optional<Enrollment> enrollmentOptional = repository.findById(enrollmentId);

        if(enrollmentOptional.isEmpty()){
            throw new RuntimeException("Enrollment 를 찾을 수없습니다." + enrollmentId);
        }

        EnrollmentResponseDTO responseDTO = toResponseDTO(enrollmentOptional.get());
        return List.of(responseDTO);
    }

    @Override
    public EnrollmentResponseDTO createEnrollmentByAuthorizedUser(EnrollmentCreateDTO dto, String email) {
        log.info("2)Enrollment 추가 시작 Enrollment : {}", dto);

        Enrollment enrollment = new Enrollment();
        MapperUtil.updateFrom(dto, enrollment, new ArrayList<>());

        log.info("3)EnrollmentCreateDTO -> Enrollment : {}", enrollment);

        User user = userService.getUserById(dto.getUserId());
        CourseOffering offering = offeringService.getCourseOfferingEntity(dto.getOfferingId());

        log.info("3-1)Offering 탐색 : {}", offering);
        log.info("3-2)유저 탐색 : {}", user);

        MapperUtil.updateFrom(dto,enrollment,List.of("enrollmentId"));
        enrollment.setUser(user);
        enrollment.setCourseOffering(offering);

        log.info("4)Offering 을 추가한 이후 Course : {}", enrollment);

        Enrollment result = repository.save(enrollment);
        log.info("4)추가된 Course : {}", result);

        return toResponseDTO(result);
    }

    @Override
    public EnrollmentResponseDTO updateEnrollmentByAuthorizedUser(EnrollmentUpdateDTO dto, String email) {
        log.info("2)Enrollment 수정 시작 Enrollment : {}", dto);

        Optional<Enrollment> enrollmentOptional = repository.findById(dto.getEnrollmentId());

        if(enrollmentOptional.isEmpty()){
            throw new RuntimeException("Enrollment가 없습니다, + " + dto.getEnrollmentId());
        }

        Enrollment enrollment = enrollmentOptional.get();

        User user = userService.getUserById(dto.getUserId());
        CourseOffering offering = offeringService.getCourseOfferingEntity(dto.getOfferingId());
        log.info("3-1)Offering 탐색 : {}", offering);
        log.info("3-2)유저 탐색 : {}", user);

        log.info("3) 수정 이전 Enrollment : {}",enrollment);
        MapperUtil.updateFrom(dto,enrollment,List.of("enrollmentId"));
        enrollment.setUser(user);
        enrollment.setCourseOffering(offering);

        log.info("5) 기존 Enrollment : {}",enrollment);
        Enrollment updateCourse=repository.save(enrollment);

        log.info("6) Course 수정 성공 updateCourse:  {}",updateCourse);

        return toResponseDTO(updateCourse);
    }

    @Override
    public Map<String, String> deleteByEnrollmentId(Long enrollmentId, String email) {
        log.info("2) Enrollment 한개삭제 시작 , enrollmentId : {}",enrollmentId);

        Optional<Enrollment> enrollment = repository.findById(enrollmentId);

        if(enrollment.isEmpty()) {
            return Map.of("Result","Failure");
        }

        repository.delete(enrollment.get());

        return Map.of("Result","Success");
    }


    //E-2) **(기능 입력 바랍니다/사용 안할시 삭제 부탁드립니다)**
    @Override
    public int addEnrollment(LegacyEnrollmentDTO legacyEnrollmentDTO) {
        log.info("1) 확인 : {}", legacyEnrollmentDTO);
        Enrollment enrollment = mapper.map(legacyEnrollmentDTO, Enrollment.class);
        log.info("확인 : {}", enrollment);
        try {
            repository.save(enrollment);
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    //E-3)다른 service에서 enrollment와 여기에 속한 상위 테이블의 정보를 실질적으로 사용하기 위한 service 구현부
    //현재 사용위치: reviewServiceImpl에서 [ R-3) - 리뷰 생성 테이블에서 사용 ]
    @Override
    public Enrollment getEnrollmentEntity(Long id) {
        Enrollment enrollment = repository.findByEnrollmentId(id);

        //Enrollment 수강 내역 id에 대한 검증
        if (enrollment == null) {
            throw new EnrollmentNotFoundException(
                    "3) 보안 검사 시도 식별코드 -:E-3" +
                            "수강 정보를 찾을 수 없습니다. id: " + id);
        }

        //User 검증 추가 -- 현재 사용 안함
        if (enrollment.getUser() == null) {
            throw new UserNotFoundException("수강 정보에 연결된 사용자가 존재하지 않습니다 id:."+id);
        }

        //Offering 개설 강의 Id에 대한 검증
        if (enrollment.getCourseOffering() == null) {
            throw new CourseOfferingNotFoundException( "데이터 오류: 수강 정보에 개설 강의가 없습니다. id: " + id);
        }

        return enrollment;
    }
}

/**
 * E-4) 다른 클래스에 정보를 전달할 때 repository가 옳은지 service가 옳은지 검증 후 추후 사용 혹은 삭제 예정
 */
//@Override
//    public EnrollmentTestDTO getEnrollmentForGrade(Long id) {
//        Enrollment e=repository.findByEnrollmentId(id);
//
//        CourseOffering co=e.getCourseOffering();
//
//        return EnrollmentTestDTO.builder()
//                .enrollmentId(e.getEnrollmentId())
////                .studentId(e.getUser().getUserId())
//                .offeringId(co.getOfferingId())
//                .courseName(co.getCourse().getCourseName())
//                .studentName(e.getUser().getNickname())
//                .build();
//    }


/**
 * /E-5) 다른 클래스에 정보를 전달할 때 repository가 옳은지 service가 옳은지 검증 후 추후 사용 혹은 삭제 예정
 */
//    @Override
//    public List<EnrollmentDTO> getEnrollmentFindUser(Long id) {
//        List<EnrollmentDTO> list=new ArrayList<>();
//        return null;
//    }

