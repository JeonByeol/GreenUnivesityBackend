package com.univercity.unlimited.greenUniverCity.function.academic.attendance.service;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository repository;
    private final EnrollmentRepository enrollmentRepository; // Service 대신 Repo 직접 사용
    private final CourseOfferingRepository offeringRepository;

    private final AcademicSecurityValidator validator;
    private final EntityMapper entityMapper;

    // A-1) 조회: 출결 단건 조회 
    @Override
    @Transactional(readOnly = true)
    public AttendanceResponseDTO getAttendance(Long attendanceId) {
        // 1 존재 여부 확인
        Attendance attendance = getAttendanceOrThrow(attendanceId);

        // (선택 사항) 조회하는 사람이 본인(학생)이거나 교수인지 확인하는 로직을 추가할 수 있음
        // 현재는 데이터 존재 여부만 확인하고 리턴

        return entityMapper.toattendanceResponseDTO(attendance);
    }

    // A-2) 조회: 교수가 특정 과목(Offering)의 전체 출결 현황 조회
    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAttendanceByOffering(Long offeringId, String professorEmail) {
        log.info("2) 교수 과목별 출결 조회 - offeringId: {}, 교수: {}", offeringId, professorEmail);

        // 1 과목 정보 조회
        CourseOffering offering = getOfferingOrThrow(offeringId);

        // 2 보안검증
        validator.validateProfessorOwnership(offering, professorEmail, "출결 조회");

        // 3 해당 과목의 출결 리스트 조회
        return repository.findByOfferingId(offeringId).stream()
                .map(entityMapper::toattendanceResponseDTO)
                .collect(Collectors.toList());
    }

    // A-3) 조회: 학생이 특정 과목(Enrollment)에 대한 출결 조회
    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> findForEnrollmentOfAttendance(Long enrollmentId) {
        log.info("2) 학생 과목별 출결 조회 - enrollmentId: {}", enrollmentId);

        // Enrollment 존재 여부 체크 단순히 검증용 호출
        getEnrollmentOrThrow(enrollmentId);

        List<Attendance> attendances = repository.findByEnrollmentId(enrollmentId);

        return attendances.stream()
                .map(entityMapper::toattendanceResponseDTO)
                .toList();
    }

    // A-4) 조회: 학생 본인의 전체 출결 조회 (완료)
    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> findForStudentOfAttendance(String email) {
        log.info("2) 학생 전체 출결 조회 - email: {}", email);

        // (필요 시 여기서 학생 이메일 유효성 검증 로직 추가 가능)

        return repository.findByEmail(email).stream()
                .map(entityMapper::toattendanceResponseDTO)
                .collect(Collectors.toList());
    }

    // A-5) 조회: 출결 전체 조회 (관리자)
    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> findAllAttendance() {
        log.info("2) 출결 전체 테이블 조회");
        return repository.findAllWithDetails().stream()
                .map(entityMapper::toattendanceResponseDTO)
                .collect(Collectors.toList());
    }

    // A-6) 생성: 출결 데이터 등록 (교수)
    @Override
    public AttendanceResponseDTO createAttendance(AttendanceCreateDTO dto, String professorEmail) {
        log.info("2) 출결 생성 요청 - 교수: {}", professorEmail);

        // 1. Enrollment 조회
        Enrollment enrollment = getEnrollmentOrThrow(dto.getEnrollmentId());

        // 2. 중복 검사 (같은 날짜에 이미 출석체크 했는지?)
        // repository에 boolean existsByEnrollmentAndAttendanceDate(...) 메서드 필요
        // validator.validateDuplicate(repository.existsByEnrollmentAndAttendanceDate(enrollment, dto.getAttendanceDate()), "해당 날짜의 출결");

        // 3. 교수 권한 검증 ("이 학생 내 수업 듣는 거 맞아?")
        validator.validateProfessorOwnership(enrollment, professorEmail, "출결 생성");

        // [수정] 주차 정보 자동 계산 로직 적용 (입력값 없으면 자동 계산)
        // dto에 week 필드가 있다고 가정 (만약 없다면 CreateDTO에 Integer week 추가 필요)
        Integer weekVal = (dto.getWeek() != null && dto.getWeek() > 0)
                ? dto.getWeek()
                : calculateWeek(dto.getAttendanceDate());

        Attendance attendance = Attendance.builder()
                .enrollment(enrollment)
                .attendanceDate(dto.getAttendanceDate())
                .status(dto.getStatus())
                .week(weekVal) // [수정] 계산된 주차 저장 (Entity에 week 필드 필요)
                .build();

        return entityMapper.toattendanceResponseDTO(repository.save(attendance));
    }


    // A-7) 수정: 출결 상태 변경 (교수)
    @Override
    public AttendanceResponseDTO updateAttendance(AttendanceUpdateDTO dto, String professorEmail) {
        Long attendanceId = dto.getAttendanceId();
        log.info("2) 출결 수정 요청 - ID: {}, 교수: {}", attendanceId, professorEmail);

        // 1.[본인] 조회 및 검증
        Attendance attendance = getAttendanceOrThrow(dto.getAttendanceId());

        // 2.교수 권한 검증
        validator.validateProfessorOwnership(attendance.getEnrollment(), professorEmail, "출결 수정");

        // 3.업데이트
        attendance.setStatus(dto.getStatus());
        attendance.setAttendanceDate(dto.getAttendanceDate());

        // [수정] 주차 정보 업데이트 (입력값 있으면 수정, 없으면 날짜 기준 자동 재계산)
        if (dto.getWeek() != null && dto.getWeek() > 0) {
            attendance.setWeek(dto.getWeek());
        } else {
            // 날짜가 바뀌었다면 주차도 다시 계산해서 넣어주는 것이 정확함
            attendance.setWeek(calculateWeek(dto.getAttendanceDate()));
        }

        return entityMapper.toattendanceResponseDTO(repository.save(attendance));
    }

    // A-8) 삭제: 출결 데이터 삭제 (교수)
    @Override
    public void deleteAttendance(Long attendanceId, String professorEmail) {
        log.info("2) 출결 삭제 요청 - ID: {}, 교수: {}", attendanceId, professorEmail);

        //조회
        Attendance attendance = getAttendanceOrThrow(attendanceId);

        //검증
        validator.validateProfessorOwnership(attendance.getEnrollment(), professorEmail, "출결 삭제");

        repository.delete(attendance);
    }

    // =========================================================================
    // 함수
    // =========================================================================

    private Attendance getAttendanceOrThrow(Long id) {
        return validator.getEntityOrThrow(repository, id, "출결");
    }

    private Enrollment getEnrollmentOrThrow(Long id) {
        return validator.getEntityOrThrow(enrollmentRepository, id, "수강신청");
    }

    private CourseOffering getOfferingOrThrow(Long id) {
        return validator.getEntityOrThrow(offeringRepository, id, "강의(Offering)");
    }

    // 주차 자동 계산
    private Integer calculateWeek(LocalDate attendanceDate) {
        try {
            // 3월~8월 -> 1학기 (3월 1일 시작)
            // 9월~12월 -> 2학기 (9월 1일 시작)
            // 1월~2월 -> 2학기 겨울방학/계절학기 (직전 연도 9월 1일 시작)

            int year = attendanceDate.getYear();
            int month = attendanceDate.getMonthValue();
            LocalDate termStartDate;

            if (month >= 3 && month <= 8) {
                // 1학기: 해당 연도 3월 1일
                termStartDate = LocalDate.of(year, 3, 1);
            } else if (month >= 9) {
                // 2학기: 해당 연도 9월 1일
                termStartDate = LocalDate.of(year, 9, 1);
            } else {
                // 1월, 2월 (겨울): 직전 연도 9월 1일 기준
                termStartDate = LocalDate.of(year - 1, 9, 1);
            }

            // 혹시라도 개강일보다 출석일이 빠르다면 1주차로 처리 (예: 2월 28일인데 3월 1일 기준인 경우 방지)
            if (attendanceDate.isBefore(termStartDate)) return 1;

            // 2. 날짜 차이 계산 (일주일 단위)
            long weeksBetween = ChronoUnit.WEEKS.between(termStartDate, attendanceDate);

            // 0주차부터 시작하므로 +1 (예: 개강일 당일은 0주차 -> 1주차로 보정)
            return (int) weeksBetween + 1;

        } catch (Exception e) {
            log.warn("주차 자동 계산 실패 (기본값 1 설정): {}", e.getMessage());
            return 1; // 계산 실패 시 기본값
        }
    }
}
