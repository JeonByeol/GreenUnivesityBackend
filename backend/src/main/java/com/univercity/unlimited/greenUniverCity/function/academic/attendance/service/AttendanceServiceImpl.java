package com.univercity.unlimited.greenUniverCity.function.academic.attendance.service;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.DataIntegrityException;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.InvalidRoleException;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.UnauthorizedException;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{
    private final AttendanceRepository repository;

    private final UserService userService;

    private final EnrollmentService enrollmentService;

    private final EntityMapper entityMapper;

    private final ModelMapper mapper;

    private void validateStudentOwnerShip(String requesterEmail,String targetEmail,String action){
        log.info("4) 학생 권한 검증 시작 요청자 -:{}, 대상자 -:{}, 작업 -:{}",requesterEmail,targetEmail,action);

        // 1.요청자 조회
        User requester=userService.getUserByEmail(requesterEmail);

        // 2.요청자의 학생 권한 확인
        if(!requester.getUserRole().equals(UserRole.STUDENT)){
            throw new InvalidRoleException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: A-Security-1 (출결%s) " +
                                    "학생 권한이 없습니다. " +
                                    "요청자: %s,userId: %s, 현재역할: %s",
                            action,requester.getUserId(),requester.getUserRole())
            );
        }

    }


    /**
     * A-Security) 보안검사:
     * - 교수 권한 검증: CourseOffering의 담당 교수와 요청자가 일치하는지 확인
     */
    private void validateProfessorOwnership(Enrollment enrollment,String email,String action){
        log.info("4) 교수 권한 검증 시작 -: {}",email);

        // 1.요청자 조회 - userService에 존재하는 U-E service 구현부에 email을 전달하여 유저를 조회합니다
        User requester=userService.getUserByEmail(email);

        // 2. 요청자의 교수 권한 확인
        if(!(requester.getUserRole() == UserRole.PROFESSOR
                || requester.getUserRole() == UserRole.ADMIN)){
            throw new InvalidRoleException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: A-Security-1 (출결%s) " +
                                    "교수 권한이 없습니다. " +
                                    "요청자: %s, userId: %s, 현재역할: %s",
                            action,email,requester.getUserId(),requester.getUserRole())
            );
        }

        // 3. 담당 교수가 존재하는지 확인
        User professor=enrollment.getClassSection().getCourseOffering().getProfessor();

        if (professor == null) {
            throw new DataIntegrityException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: A-security-2 (출결 %s) " +
                                    "데이터 오류: 개설 강의에 담당 교수가 없습니다. offeringId: %s",
                            action, enrollment.getClassSection().getCourseOffering().getOfferingId())
            );
        }

        // 4. 담당 교수의 역할 확인 (데이터 정합성)
        if (!(professor.getUserRole().equals(UserRole.PROFESSOR) || professor.getUserRole().equals(UserRole.ADMIN))) {
            throw new InvalidRoleException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: A-security-3 (출결 %s) " +
                                    "데이터 오류: 담당자가 교수 권한이 없습니다. " +
                                    "userId: %s, 현재 역할: %s",
                            action, professor.getUserId(), professor.getUserRole())
            );
        }

        // 5. 요청자와 담당 교수 일치 확인
        if (!professor.getUserId().equals(requester.getUserId())) {
            throw new UnauthorizedException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: A-security-4 (출결 %s) " +
                                    "해당 강의의 담당 교수만 출결을 %s할 수 있습니다. " +
                                    "담당 교수: %s (userId: %s), 요청자: %s (userId: %s)",
                            action, action,
                            professor.getEmail(), professor.getUserId(),
                            email, requester.getUserId())
            );
        }

        log.info("4) 교수 권한 검증 완료 - 교수: {}, 작업: {}", email, action);
    }

    //A-1) 출결 테이블에 존재하는 데이터를 조회하는 서비스 구현부
    @Override
    public List<AttendanceResponseDTO> findAllAttendance() {
        log.info("2)출결 표 전체조회 시작");

        List<Attendance> attendances=repository.findAll();

        return attendances.stream()
                .map(entityMapper::toattendanceResponseDTO)
                .collect(Collectors.toList());
    }

    //A-2)학생이 특정 과목에 대한 출결을 조회하기 위한 서비스 구현부
    @Override
    public List<AttendanceResponseDTO> findForEnrollmentOfAttendance(Long enrollmentId) {
        log.info("2) 학생이 출결 조회 시작 enrollmentId-:{}",enrollmentId);

        List<Attendance> attendances=repository.findByEnrollmentId(enrollmentId);

        return attendances.stream()
                .map(entityMapper::toattendanceResponseDTO)
                .collect(Collectors.toList());
    }

    //A-3)학생이 자신의 수강과목에 대한 모든 출결을 조회하기 위한 서비스 구현부
    @Override
    public List<AttendanceResponseDTO> findForStudentOfAttendance(String email) {
        log.info("2) 학생이 출결 조회 시작 email-:{}",email);

        List<Attendance> attendances=repository.findByEmail(email);

        return attendances.stream()
                .map(entityMapper::toattendanceResponseDTO)
                .collect(Collectors.toList());
    }

    //A-4)  교수가 특정 학생에 대한 출결을 작성하기 위한 서비스 구현부
    @Override
    public AttendanceResponseDTO createAttendanceForProfessor(AttendanceCreateDTO dto,String professorEmail) {
        log.info("2) 교수가 학생 출결 생성 시작 교수-:{}",professorEmail);

        Enrollment enrollment=enrollmentService.getEnrollmentEntity(dto.getEnrollmentId());

        //A-security 보안검사
        validateProfessorOwnership(enrollment,professorEmail,"생성");

        Attendance attendance= Attendance.builder()
                .enrollment(enrollment)
                .attendanceDate(dto.getAttendanceDate())
                .status(dto.getStatus())
                .build();

        Attendance createAttendance=repository.save(attendance);

        log.info("5) 출결 작성 완료 -attendanceId:{}, 교수:{}",createAttendance.getAttendanceId(),professorEmail);

        return entityMapper.toattendanceResponseDTO(createAttendance);
    }

    //A-5) 교수가 학생에 대한 출결을 수정하기 위한 서비스 구현부
    @Override
    public AttendanceResponseDTO updateAttendanceForProfessor(AttendanceUpdateDTO dto, String professorEmail) {
        log.info("2) 교수가 학생 출결 수정 시작  attendanceId-:{}, 교수-:{}",dto.getAttendanceId(),professorEmail);

        Attendance attendance=repository.findById(dto.getAttendanceId())
                .orElseThrow(()->new RuntimeException(
                        "3)보안 검사 식별 코드 -:A-5 " +
                                "출결이 존재하지 않습니다. attendanceId " + dto.getAttendanceId()));

        Enrollment enrollment=attendance.getEnrollment();
        validateProfessorOwnership(enrollment,professorEmail,"수정");

        attendance.setAttendanceDate(dto.getAttendanceDate());
        attendance.setStatus(dto.getStatus());

        Attendance updateAttendance=repository.save(attendance);

        log.info("5) 출결 수정 성공 교수-:{}, attendanceId-:{}",professorEmail,dto.getAttendanceId());

        return entityMapper.toattendanceResponseDTO(updateAttendance);
    }


}
