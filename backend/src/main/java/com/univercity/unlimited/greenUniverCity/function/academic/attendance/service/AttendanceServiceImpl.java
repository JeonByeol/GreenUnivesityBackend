package com.univercity.unlimited.greenUniverCity.function.academic.attendance.service;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{
    private final AttendanceRepository repository;
    private final EnrollmentService enrollmentService; // 학생/수강신청 정보용
    private final CourseOfferingService offeringService; // [NEW] 과목 정보 조회용
    private final AcademicSecurityValidator validator;
    private final EntityMapper entityMapper;

    // =================================================================================
    // A-1). [조회] 출결 단건 조회 (기본 CRUD 보강)
    // - 수정 버튼 누르기 전이나 상세 보기 시 사용
    // =================================================================================
    @Override
    public AttendanceResponseDTO getAttendance(Long attendanceId) {
        //1.존재 여부 확인 (Validator 활용)
        Attendance attendance = validator.getEntityOrThrow(repository, attendanceId, "출결");

        // (선택 사항) 조회하는 사람이 본인(학생)이거나 교수인지 확인하는 로직을 추가할 수 있음
        // 현재는 데이터 존재 여부만 확인하고 리턴

        return entityMapper.toattendanceResponseDTO(attendance);
    }

    // =================================================================================
    // A-2). [조회] 교수가 특정 과목(Offering)의 전체 출결 현황 조회
    // =================================================================================
    @Override
    public List<AttendanceResponseDTO> getAttendanceByOffering(Long offeringId, String professorEmail) {
        log.info("2) 교수 과목별 출결 조회 - offeringId: {}, 교수: {}", offeringId, professorEmail);

        // 1. 과목 정보 가져오기 (외부 서비스 사용)
        // CourseOfferingService에 getEntity 메서드가 있다고 가정 (없으면 추가 필요)
        CourseOffering offering = offeringService.getCourseOfferingEntity(offeringId);

        // 2. [보안 검증] "이 과목, 진짜 당신 강의 맞습니까?"
        validator.validateProfessorOwnership(offering, professorEmail, "출결 조회");

        // 3. 해당 과목의 출결 리스트 조회
        return repository.findByOfferingId(offeringId).stream()
                .map(entityMapper::toattendanceResponseDTO)
                .collect(Collectors.toList());
    }

    // =================================================================================
    // A-3). [조회] 학생이 특정 과목(Enrollment)에 대한 출결 조회 (기존 유지)
    // =================================================================================
    @Override
    public List<AttendanceResponseDTO> findForEnrollmentOfAttendance(Long enrollmentId) {
        log.info("2) 학생 과목별 출결 조회 - enrollmentId: {}", enrollmentId);

        // Enrollment 존재 여부 체크 (외부 서비스)
        enrollmentService.getEnrollmentEntity(enrollmentId);

        List<Attendance> attendances=repository.findByEnrollmentId(enrollmentId);

        return attendances.stream()
                .map(entityMapper::toattendanceResponseDTO)
                .toList();
    }

    // =================================================================================
    // A-4). [조회] 학생이 본인의 모든 출결 조회 (기존 유지)
    // =================================================================================
    @Override
    public List<AttendanceResponseDTO> findForStudentOfAttendance(String email) {
        log.info("2) 학생 전체 출결 조회 - email: {}", email);
        return repository.findByEmail(email).stream()
                .map(entityMapper::toattendanceResponseDTO)
                .collect(Collectors.toList());
    }

    // =================================================================================
    // A-5). [관리자/디버깅용] 출결 전체 조회 (기존 복구)
    // =================================================================================
    @Override
    public List<AttendanceResponseDTO> findAllAttendance() {
        log.info("2) 출결 전체 테이블 조회");
        return repository.findAllWithDetails().stream()
                .map(entityMapper::toattendanceResponseDTO)
                .collect(Collectors.toList());
    }

    // =================================================================================
    // 6. [생성] 출결 데이터 작성 (교수)
    // =================================================================================
    @Override
    public AttendanceResponseDTO createAttendance(AttendanceCreateDTO dto, String professorEmail) {
        log.info("2) 출결 생성 요청 - 교수: {}", professorEmail);

        // 1. Enrollment 조회 (외부 서비스)
        Enrollment enrollment = enrollmentService.getEnrollmentEntity(dto.getEnrollmentId());

        // 2. 중복 검사 (같은 날짜에 이미 출석체크 했는지?)
        // repository에 boolean existsByEnrollmentAndAttendanceDate(...) 메서드 필요
        // validator.validateDuplicate(repository.existsByEnrollmentAndAttendanceDate(enrollment, dto.getAttendanceDate()), "해당 날짜의 출결");

        // 3. 교수 권한 검증 ("이 학생 내 수업 듣는 거 맞아?")
        validator.validateProfessorOwnership(enrollment, professorEmail, "출결 생성");

        Attendance attendance = Attendance.builder()
                .enrollment(enrollment)
                .attendanceDate(dto.getAttendanceDate())
                .status(dto.getStatus())
                .build();

        return entityMapper.toattendanceResponseDTO(repository.save(attendance));
    }

    // =================================================================================
    // 7. [수정] 출결 상태 변경 (교수)
    // =================================================================================
    @Override
    public AttendanceResponseDTO updateAttendance(AttendanceUpdateDTO dto, String professorEmail) {
        Long attendanceId = dto.getAttendanceId();
        log.info("2) 출결 수정 요청 - ID: {}, 교수: {}", attendanceId, professorEmail);

        // 1.[본인 Repo] 조회 및 검증
        Attendance attendance = validator.getEntityOrThrow(repository, attendanceId, "출결");

        // 2.교수 권한 검증
        validator.validateProfessorOwnership(attendance.getEnrollment(), professorEmail, "출결 수정");

        // 3.업데이트
        attendance.setStatus(dto.getStatus());
        attendance.setAttendanceDate(dto.getAttendanceDate());

        return entityMapper.toattendanceResponseDTO(repository.save(attendance));
    }

    // =================================================================================
    // 8.삭제 출결 삭제 (교수)
    // =================================================================================
    @Override
    public void deleteAttendance(Long attendanceId, String professorEmail) {
        log.info("2) 출결 삭제 요청 - ID: {}, 교수: {}", attendanceId, professorEmail);

        Attendance attendance = validator.getEntityOrThrow(repository, attendanceId, "출결");
        validator.validateProfessorOwnership(attendance.getEnrollment(), professorEmail, "출결 삭제");

        repository.delete(attendance);
    }
}
