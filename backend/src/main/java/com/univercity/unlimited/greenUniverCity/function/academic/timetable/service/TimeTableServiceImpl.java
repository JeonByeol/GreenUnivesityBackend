package com.univercity.unlimited.greenUniverCity.function.academic.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.community.review.exception.DataIntegrityException;
import com.univercity.unlimited.greenUniverCity.function.community.review.exception.InvalidRoleException;
import com.univercity.unlimited.greenUniverCity.function.community.review.exception.TimeTableNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.community.review.exception.UnauthorizedException;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.repository.TimeTableRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeTableServiceImpl implements TimeTableService{
    private final TimeTableRepository repository;

    private final CourseOfferingService offeringService;

    private final UserService userService;

    /**
     * T-A) TimeTable 엔티티를 (Response)DTO로 변환
     * - 각각의 crud 기능에 사용되는 서비스 구현부에 사용하기 위해 함수로 생성
     */

    private TimeTableResponseDTO toResponseDTO(TimeTable timeTable){
        CourseOffering courseOffering=timeTable.getCourseOffering();
        User user=courseOffering.getProfessor();

        return
                TimeTableResponseDTO.builder()
                        .timetableId(timeTable.getTimetableId())
                        .startTime(timeTable.getStartTime())
                        .endTime(timeTable.getEndTime())
                        .location(timeTable.getLocation())
                        .dayOfWeek(timeTable.getDayOfWeek())
                        .courseName(courseOffering.getCourseName())
                        .professorNickname(user.getNickname())
                        .offeringId(courseOffering.getOfferingId())
                        .build();
    }

    /**
     * T-security) 보안 검사:
     * - 교수 권한 검증: CourseOffering의 담당 교수와 요청자가 일치하는지 확인
     * (T-4/T-5/T-6)
     */

    private void validateProfessorOwnership(CourseOffering offering, String requesterEmail,String action) {
        log.info("4) 시간표 {} - 교수 권한 검증 시작: {}", action, requesterEmail);

        // 1. 요청자 조회 - userService에 존재하는 U-E service 구현부에 requesterEmail을 전달하여 유저를 조회한다
        User requester = userService.getUserByEmail(requesterEmail);

        // 2. 요청자의 교수 권한 확인
        // 1번 과정을 통하여 조회한 user의 정보를 가지고 교수의 역할을 검증한다
        if (!(requester.getUserRole() == UserRole.PROFESSOR
                || requester.getUserRole() == UserRole.ADMIN)) {
            throw new InvalidRoleException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: T-security-1 (시간표 %s) " +
                                    "교수 권한이 없습니다. " +
                                    "요청자: %s, userId: %s, 현재 역할: %s",
                            action, requesterEmail, requester.getUserId(), requester.getUserRole())
            );
        }

        // 3. 담당 교수가 존재하는지 확인
        User professor = offering.getProfessor();

        if (professor == null) {
            throw new DataIntegrityException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: T-security-2 (시간표 %s) " +
                                    "데이터 오류: 개설 강의에 담당 교수가 없습니다. offeringId: %s",
                            action, offering.getOfferingId())
            );
        }

        // 4. 담당 교수의 역할 확인 (데이터 정합성)
        if (!(professor.getUserRole().equals(UserRole.PROFESSOR) || professor.getUserRole().equals(UserRole.ADMIN))) {
            throw new InvalidRoleException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: T-security-3 (시간표 %s) " +
                                    "데이터 오류: 담당자가 교수 권한이 없습니다. " +
                                    "userId: %s, 현재 역할: %s",
                            action, professor.getUserId(), professor.getUserRole())
            );
        }

        // 5. 요청자와 담당 교수 일치 확인
        if (!professor.getUserId().equals(requester.getUserId())) {
            throw new UnauthorizedException(
                    String.format(
                            "4)보안 검사 시도 식별코드-: T-security-4 (시간표 %s) " +
                                    "해당 강의의 담당 교수만 시간표를 %s할 수 있습니다. " +
                                    "담당 교수: %s (userId: %s), 요청자: %s (userId: %s)",
                            action, action,
                            professor.getEmail(), professor.getUserId(),
                            requesterEmail, requester.getUserId())
            );
        }

        log.info("4) 교수 권한 검증 완료 - 교수: {}, 작업: {}", requesterEmail, action);
    }

    //T-1) 리뷰 테이블에 존재하는 모든 데이터를 조회하기 위한 서비스 구현부
    @Transactional
    @Override
    public List<TimeTableResponseDTO> findAllTimeTable() {
        log.info("2) 시간표 전체조회 시작");
        List<TimeTable> timeTables=repository.findAll();

        log.info("3) 시간표 전체조회 성공");

        return timeTables.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //T-2)특정 과목에 존재하는 모든 시간표를 조회하기 위한 서비스 구현부
    //offeringId 값을 받아서 조회를 해야 하는데 db 데이터 문제로 임시적으로 요일을 받아서
    //조회 할 수 있게 기능을 구현해놨음 추후 코드 수정 예정
    //-> 그리고 지금 postmanTest에서 데이터를 조회 할 때 1번째 동작에서 호출을 못하고
    // 2번째 동작부터 호출을 하는 문제가 있음
    @Override
    public List<TimeTableResponseDTO> offeringOfTimeTable(Long offeringId) {
        log.info("2) 특정 시간표 조회 시작 offeringId-:{}",offeringId);
        List<TimeTable> timeTables=repository.findTimeTableByOfferingId(offeringId);

        log.info("3) 시간표 조회 성공 offeringId-:{}",offeringId);

        return timeTables.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //T-3)특정 학생이 신청한 모든 과목의 시간표를 조회하기 위한 서비스 구현부
    @Override
    public List<TimeTableResponseDTO> studentOfTimeTable(String email) {
        log.info("2) 학생이 시간표 조회 요청 학생-:{}",email);

        List<TimeTable> timeTables = repository.findTimetableByStudentEmail(email);

        log.info("3) 학생의 시간표 조회 성공 학생-:{}",email);

        return timeTables.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //T-4) 교수 or 관리자가 특정 강의에 대한 시간표를 생성하기 위한 서비스 구현부 -> 문제 좀 많음 수정 해야함
    // -> 시간표에 대한 생성 기능은 구현 o 하지만 시간표를 생성할떄 그 강의
    // 구상: serviceImpl 구현부내에 userId값을 받아서 검증하는 함수를 만들어서 검증예정
    @Override
    public TimeTableResponseDTO createTimeTableForProfessor(TimeTableCreateDTO dto, String requesterEmail) {
        log.info("2) 시간표 생성 -교수:{}, offeringId:{}",requesterEmail,dto.getOfferingId());

        CourseOffering offering=offeringService.getCourseOfferingEntity(dto.getOfferingId());

        // T-security 보안검사
        validateProfessorOwnership(offering,requesterEmail,"생성");

        TimeTable timeTable=TimeTable.builder()
                .courseOffering(offering)
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .location(dto.getLocation())
                .build();

        TimeTable saveTimeTable=repository.save(timeTable);

        log.info("5) 시간표 생성 완료 -timetableId:{}, 교수:{}",saveTimeTable.getTimetableId(),requesterEmail);

        return toResponseDTO(saveTimeTable);
    }

    //T-5) 교수가 본인이 담당하고 있는 수업에 존재하는 시간표를 수정하기 위한 서비스 구현부
    @Override
    public TimeTableResponseDTO updateTimeTableForProfessor(TimeTableUpdateDTO dto, String requesterEmail) {
        log.info("2) 시간표 수정 시작 -timetableId-:{}, 교수-:{}",dto.getTimetableId(),requesterEmail);

        TimeTable timeTable=repository.findById(dto.getTimetableId())
                .orElseThrow(()->new TimeTableNotFoundException(
                        "3)보안 검사 시도 식별 코드 -:T-5 " +
                                "시간표가 존재하지 않습니다. timeId:" + dto.getTimetableId()));

        // T-security 보안검사보안 검사(소유권 검증)
        CourseOffering offering = timeTable.getCourseOffering();
        validateProfessorOwnership(offering,requesterEmail,"수정");

        timeTable.setLocation(dto.getLocation());
        timeTable.setDayOfWeek(dto.getDayOfWeek());
        timeTable.setStartTime(dto.getStartTime());
        timeTable.setEndTime(dto.getEndTime());

        TimeTable updateTimeTable=repository.save(timeTable);

        log.info("5) 시간표 수정 성공 -교수:{}, timetableId:{},강의실:{},요일:{},시작시간:{},종료시간:{}",
                requesterEmail,
                dto.getTimetableId(),
                dto.getLocation(),
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime());

        return toResponseDTO(updateTimeTable);
    }

    //T-6) 교수 or 관리자가 개설된 강의에 대한 시간표를 삭제하기 위한 서비스 구현부
    @Override
    public void deleteByTimeTable(Integer timetableId,String requesterEmail) {
        log.info("2) 시간표 삭제 요청 -교수: {} ,timetableId: {}",requesterEmail,timetableId);

        //시간표 조회
        TimeTable timeTable=repository.findById(timetableId)
                .orElseThrow(()->new TimeTableNotFoundException(
                        "3)보안 검사 시도 식별 코드-: T-6 " +
                                "시간표가 존재하지 않습니다. timeId:" + timetableId));

        // T-security 보안검사
        CourseOffering offering = timeTable.getCourseOffering();
        validateProfessorOwnership(offering,requesterEmail,"삭제");

        repository.delete(timeTable);

        log.info("5)시간표 삭제 성공 -교수: {},timetableId: {}",requesterEmail,timetableId);
    }

    //T-EX) ** 필요한 기능에 대한 설명 입력 부탁드립니다/없으면 삭제 부탁드립니다 ** 
//    @Override
//    public ResponseEntity<String> addTimeTable(TimeTableDTO timeTableDTO) {
//        return null;
//    }
}
