package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.exception.DuplicateEnrollmentException;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.exception.EnrollmentNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.exception.UserNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.exception.CourseOfferingNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.service.ClassSectionService;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository repository;

    private final ModelMapper mapper;

    private final ClassSectionService sectionService;

    private final UserService userService;

    private final EntityMapper entityMapper;

    
    //중복수강신청 검증
    private void validateDuplicateEnrollment(Long userId, Long sectionId) {
        boolean exists = repository.existsByUserUserIdAndClassSectionSectionId(userId, sectionId);

        if (exists) {
            throw new DuplicateEnrollmentException(
                    String.format(
                            "중복 수강신청! 학생 ID: %d는 이미 분반 ID: %d에 수강신청했습니다.",
                            userId, sectionId
                    )
            );
        }
    }

    //정원초과검증
    private void validateSectionCapacity(ClassSection section) {
        Integer currentCount = section.getCurrentCount();
        Integer maxCapacity = section.getMaxCapacity();

        if (currentCount >= maxCapacity) {
            throw new IllegalStateException(
                    String.format(
                            "수강신청 불가! 분반 '%s'의 정원이 초과되었습니다. (현재: %d/%d)",
                            section.getSectionName(),
                            currentCount,
                            maxCapacity
                    )
            );
        }
    }

    //E-1)Enroll에 존재하는 모든 데이터 조회 서비스 구현부
    @Override
    public List<EnrollmentResponseDTO> legacyFindAllEnrollment() {
        List<EnrollmentResponseDTO> dtoList = new ArrayList<>();
        for (Enrollment i : repository.findAll()) {
            EnrollmentResponseDTO r = mapper.map(i, EnrollmentResponseDTO.class);
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
                .map(entityMapper::toEnrollmentResponseDTO).toList();
    }

    @Override
    public List<EnrollmentResponseDTO> findById(Long enrollmentId) {
        log.info("2) Enrollment 한개조회 시작 , enrollmentId : {}",enrollmentId);

        Optional<Enrollment> enrollmentOptional = repository.findById(enrollmentId);

        if(enrollmentOptional.isEmpty()){
            throw new RuntimeException("Enrollment 를 찾을 수없습니다." + enrollmentId);
        }

        EnrollmentResponseDTO responseDTO = entityMapper.toEnrollmentResponseDTO(enrollmentOptional.get());
        return List.of(responseDTO);
    }

    @Override
    public EnrollmentResponseDTO createEnrollmentByAuthorizedUser(EnrollmentCreateDTO dto, String email) {
        log.info("2)Enrollment 추가 시작 Enrollment : {}", dto);

        Enrollment enrollment = new Enrollment();
        MapperUtil.updateFrom(dto, enrollment, new ArrayList<>());

        log.info("3)EnrollmentCreateDTO -> Enrollment : {}", enrollment);

        User user = userService.getUserById(dto.getUserId());
        ClassSection section=sectionService.getClassSectionEntity(dto.getSectionId());
//        CourseOffering offering = offeringService.getCourseOfferingEntity(dto.getOfferingId());

        log.info("3-1)section 탐색 : {}", section);
        log.info("3-2)유저 탐색 : {}", user);

        // 검증 1: 중복 수강신청 검사 (데이터 조회 후, 저장 전)
        validateDuplicateEnrollment(dto.getUserId(), dto.getSectionId());
        log.info("3-3)중복 검증 통과");

        // 검증 2: 정원 초과 검사
        validateSectionCapacity(section);
        log.info("3-4)정원 검증 통과");

        MapperUtil.updateFrom(dto,enrollment,List.of("enrollmentId"));
        enrollment.setUser(user);
        enrollment.setClassSection(section);

        log.info("4)section 을 추가한 이후 Course : {}", enrollment);

        try {
            Enrollment result = repository.save(enrollment);
            log.info("4)추가된 Course : {}", result);
            return entityMapper.toEnrollmentResponseDTO(result);

        } catch (DataIntegrityViolationException e) {
            // DB 제약조건 위반 시 (UNIQUE 제약) - 2중 방어
            log.error("DB 제약조건 위반: {}", e.getMessage());
            throw new DuplicateEnrollmentException(
                    "중복 수강신청입니다. 이미 해당 분반에 신청되었습니다."
            );
        }
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
        ClassSection section=sectionService.getClassSectionEntity(dto.getSectionId());
//        CourseOffering offering = offeringService.getCourseOfferingEntity(dto.getOfferingId());
        log.info("3-1)Section 탐색 : {}", section);
        log.info("3-2)유저 탐색 : {}", user);

        // 검증: 분반 변경 시 중복 검사 (다른 분반으로 변경하는 경우에만)
        if (!enrollment.getClassSection().getSectionId().equals(dto.getSectionId())) {
            validateDuplicateEnrollment(dto.getUserId(), dto.getSectionId());
            log.info("3-3)중복 검증 통과 (분반 변경)");

            // 검증: 새 분반의 정원 검사
            validateSectionCapacity(section);
            log.info("3-4)정원 검증 통과 (새 분반)");
        }


        log.info("3) 수정 이전 Enrollment : {}",enrollment);
        MapperUtil.updateFrom(dto,enrollment,List.of("enrollmentId"));
        enrollment.setUser(user);
        enrollment.setClassSection(section);

        log.info("5) 기존 Enrollment : {}",enrollment);
        Enrollment updateCourse=repository.save(enrollment);

        log.info("6) Course 수정 성공 updateCourse:  {}",updateCourse);

        return entityMapper.toEnrollmentResponseDTO(updateCourse);
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
    public int addEnrollment(EnrollmentResponseDTO legacyEnrollmentDTO) {
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

    /**
     *  -- 전체 Entity --
     */

    //E.All) 다른 service에서 enrollment와 여기에 속한 상위 테이블의 정보를 실질적으로 사용하기 위한 service 구현부
    @Override
    public Enrollment getEnrollmentEntity(Long id) {
        Enrollment enrollment = repository.findByEnrollmentId(id);

        //Enrollment 수강 내역 id에 대한 검증
        if (enrollment == null) {
            throw new EnrollmentNotFoundException(
                    "3) 보안 검사 시도 식별코드 -:E-All" +
                            "수강 정보를 찾을 수 없습니다. id: " + id);
        }

        //User 검증 추가 -- 현재 사용 안함
        if (enrollment.getUser() == null) {
            throw new UserNotFoundException("수강 정보에 연결된 사용자가 존재하지 않습니다 id:."+id);
        }

        //Section 분반 Id에 대한 검증
        if (enrollment.getClassSection() == null) {
            throw new CourseOfferingNotFoundException( "데이터 오류: 수강 정보에 분반이 없습니다. id: " + id);
        }

        return enrollment;
    }



}

