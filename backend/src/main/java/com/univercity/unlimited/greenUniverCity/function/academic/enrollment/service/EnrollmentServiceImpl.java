package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.exception.DuplicateEnrollmentException;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.exception.EnrollmentNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.exception.UserNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.exception.CourseOfferingNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.section.service.ClassSectionService;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository repository;
    private final ClassSectionRepository sectionRepository;
    private final CourseOfferingRepository offeringRepository;
    private final UserRepository userRepository;

    private final AcademicSecurityValidator validator;
    private final ModelMapper mapper;
    private final EntityMapper entityMapper;

    public UserResponseDTO toUserDTO(User user) {
        if (user == null) return null;

        UserResponseDTO dto = UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .studentNumber(user.getStudentNumber())
                .isDelete(user.isDelete())
                .build();

        if (user.getUserRole() != null) dto.setRole(user.getUserRole().name());
        if (user.getDepartment() != null) dto.setDeptName(user.getDepartment().getDeptName());

        return dto;
    }



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

    private void validateDuplicateEnrollmentToOfferingId(Long userId, Long sectionId) {
        ClassSection section = getSectionOrThrow(sectionId);
        Long offeringId = section.getCourseOffering().getOfferingId();

        boolean exists = repository.existsByUserUserIdAndClassSectionCourseOfferingOfferingId(userId, offeringId);

        if (exists) {
            throw new DuplicateEnrollmentException(
                    String.format(
                            "중복 수강신청! 학생 ID: %d는 이미 수업 ID: %d에 신청했습니다.",
                            userId, offeringId
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

        User user = getUserOrThrow(dto.getUserId());
        ClassSection section = getSectionOrThrow(dto.getSectionId());
//        CourseOffering offering = offeringService.getCourseOfferingEntity(dto.getOfferingId());

        log.info("3-1)section 탐색 : {}", section);
        log.info("3-2)유저 탐색 : {}", user);

        // 검증 1: 중복 수강신청 검사 (데이터 조회 후, 저장 전)
        validateDuplicateEnrollment(dto.getUserId(), dto.getSectionId());
        log.info("3-3)중복 검증 통과");
        validateDuplicateEnrollmentToOfferingId(dto.getUserId(), dto.getSectionId());
        log.info("3-3-2)중복 검증 통과 2");

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

        User user = getUserOrThrow(dto.getUserId());
        ClassSection section = getSectionOrThrow(dto.getSectionId());
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
    @Override
    public Map<CourseOfferingResponseDTO, List<UserResponseDTO>> findAllStudentsByProfessorEmail(String professorEmail) {
        User professor = userRepository.findByEmail(professorEmail)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        List<Object[]> results = repository.findStudentsWithCourseByProfessorEmail(professorEmail);

        System.out.println("=== Repository 결과 개수: " + results.size() + " ===");

        Map<Long, CourseOfferingResponseDTO> offeringMap = new HashMap<>(); // offeringId 기준
        Map<Long, List<UserResponseDTO>> studentsMap = new HashMap<>();

        for (Object[] row : results) {
            CourseOffering co = (CourseOffering) row[0];
            Long offeringId = co.getOfferingId();
            CourseOfferingResponseDTO offeringDTO = entityMapper.toCourseOfferingResponseDTO(co);
            User user = (User) row[1];
            UserResponseDTO userDTO = toUserDTO(user);

            // offeringId 기준으로 map에 추가
            offeringMap.putIfAbsent(offeringId, offeringDTO);

            List<UserResponseDTO> students = studentsMap.computeIfAbsent(offeringId, k -> new ArrayList<>());

            // 학생 중복 확인
            boolean exists = students.stream()
                    .anyMatch(s -> s.getUserId().equals(userDTO.getUserId()));

            if (!exists) {
                students.add(userDTO);
            }
        }

        // 최종적으로 CourseOfferingResponseDTO → List<UserResponseDTO> 형태로 변환
        Map<CourseOfferingResponseDTO, List<UserResponseDTO>> result = new HashMap<>();
        for (Map.Entry<Long, CourseOfferingResponseDTO> entry : offeringMap.entrySet()) {
            result.put(entry.getValue(), studentsMap.get(entry.getKey()));
        }

        return result;
    }

    public List<EnrollmentResponseDTO> findMyEnrollments(String email) {
        return repository.findByUserEmail(email)
                .stream()
                .map(entityMapper::toEnrollmentResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getEnrollmentsByOffering(Long offeringId, String professorEmail) {
        log.info("2) 교수 과목별 수강생 명단 조회 - offeringId: {}, 교수: {}", offeringId, professorEmail);

        // 1. 과목 정보 조회 (존재 여부 확인)
        // (기존에 쓰시던 getOfferingOrThrow 메서드 활용)
        CourseOffering offering = getOfferingOrThrow(offeringId);

        // 2. 보안 검증 (이 교수의 강의가 맞는지?)
        // (Validator는 그대로 재사용 가능합니다)
        validator.validateProfessorOwnership(offering, professorEmail, "수강생 명단 조회");

        // 3. 해당 과목의 수강생(Enrollment) 리스트 조회
        List<Enrollment> enrollments = repository.findByOfferingId(offeringId);

        // 4. DTO 변환 후 반환
        return enrollments.stream()
                .map(entityMapper::toEnrollmentResponseDTO) // 매퍼 메서드 이름 확인 필요
                .collect(Collectors.toList());
    }





    //E-2) **(기능 입력 바랍니다/사용 안할시 삭제 부탁드립니다)**
//    @Override
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

    @Override
    public List<EnrollmentResponseDTO> getEnrollmentByUserAndOffering(Long userId, Long offeringId) {
        List<EnrollmentResponseDTO> enrollmentList = repository.findEnrollmentsByUserIdAndOfferingId(userId, offeringId).stream()
                .map(enrollment -> entityMapper.toEnrollmentResponseDTO(enrollment)).toList();

        return enrollmentList;
    }

    // =========================================================================
    //  함수
    // =========================================================================
    private Enrollment getEnrollmentOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment 를 찾을 수없습니다." + id));
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id : " + id));
    }

    private ClassSection getSectionOrThrow(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found with id : " + id));
    }

    private CourseOffering getOfferingOrThrow(Long id) {
        return validator.getEntityOrThrow(offeringRepository, id, "강의(Offering)");
    }

}

