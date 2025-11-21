package com.univercity.unlimited.greenUniverCity.function.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.review.entity.Review;
import com.univercity.unlimited.greenUniverCity.function.review.exception.*;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.timetable.repository.TimeTableRepository;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.user.entity.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeTableServiceImpl implements TimeTableService{
    private final TimeTableRepository repository;

    private final CourseOfferingService offeringService;

    private final ModelMapper mapper;

    /**
     * T-A) TimeTable 엔티티를 (Response)DTO로 변환 | 각각의 crud 기능에 사용되는 서비스 구현부에 사용하기 위해 함수로 생성
     */
    private TimeTableResponseDTO toResponseDTO(TimeTable timeTable){
        CourseOffering courseOffering=timeTable.getCourseOffering();

        return
                TimeTableResponseDTO.builder()
                        .timetableId(timeTable.getTimetableId())
                        .startTime(timeTable.getStartTime())
                        .endTime(timeTable.getEndTime())
                        .location(timeTable.getLocation())
                        .dayOfWeek(timeTable.getDayOfWeek())
                        .courseName(courseOffering.getCourseName())

                        .build();
    }

    /**
     * T-4-1) 교수 권한 검증
     * - CourseOffering의 담당 교수와 요청자가 일치하는지 확인
     */
    private void validateProfessorOwnership(CourseOffering offering, String requesterEmail) {
        User professor = offering.getProfessor();

        // 1. professor null 체크
        if (professor == null) {
            throw new DataIntegrityException(
                    "데이터 오류: 개설 강의에 담당 교수가 없습니다. offeringId: " + offering.getOfferingId()
            );
        }

        // 2.교수 역할 확인 (List에 PROFESSOR 포함 여부)
        if (!professor.getUserRoleList().contains(UserRole.PROFESSOR)) {
            throw new InvalidRoleException(
                    "담당자가 교수 권한이 없습니다. userId: " + professor.getUserId() +
                            ", 현재 역할: " + professor.getUserRoleList()
            );
        }

        // 3. 요청자와 담당 교수 일치 확인
        if (!professor.getEmail().equals(requesterEmail)) {
            throw new UnauthorizedException(
                    "해당 강의의 담당 교수만 시간표를 생성할 수 있습니다. " +
                            "담당 교수: " + professor.getEmail() + ", 요청자: " + requesterEmail
            );
        }
        log.info("4) 교수 권한 검증 완료 - 교수: {}", requesterEmail);
    }
    /**
     * T-5-1) 교수 권한 검증
     * - CourseOffering의 담당 교수와 요청자가 일치하는지 확인
     */
    private void validateReviewOwnership(TimeTable timeTable, CourseOffering offering,String requesterEmail) {
        User professor = offering.getProfessor();;

        if (!professor.getEmail().equals(requesterEmail)) {
            log.warn("리뷰 수정 권한 없음 - 요청자: {}, 작성자: {}",
                    requesterEmail, professor);
            throw new UnauthorizedReviewException(
                    "본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        log.info("리뷰 소유권 검증 통과 - 교수: {}, timetableId: {}", requesterEmail, timeTable.getTimetableId());
    }

    //T-1) 리뷰 테이블에 존재하는 모든 데이터를 조회하기 위한 서비스 구현부
    @Transactional
    @Override
    public List<TimeTableDTO> findAllTimeTable() {
        log.info("2) 여기는 시간표 전체조회 서비스입니다");
        List<TimeTableDTO> dto=new ArrayList<>();
        for(TimeTable i:repository.findAll()){
            TimeTableDTO r= mapper.map(i,TimeTableDTO.class);
            dto.add(r);
        }
        return dto;
    }

    //T-2)특정 과목에 존재하는 모든 시간표를 조회하기 위한 서비스 구현부
    //offeringId 값을 받아서 조회를 해야 하는데 db 데이터 문제로 임시적으로 요일을 받아서
    //조회 할 수 있게 기능을 구현해놨음 추후 코드 수정 예정
    //-> 그리고 지금 postmanTest에서 데이터를 조회 할 때 1번째 동작에서 호출을 못하고
    // 2번째 동작부터 호출을 하는 문제가 있음
    @Override
    public List<TimeTableResponseDTO> get2(String dayOfWeek) {
        List<TimeTable> timeTables=repository.findByDayTimeTable(dayOfWeek);

        return timeTables.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    //T-3)특정 학생이 신청한 모든 과목의 시간표를 조회하기 위한 서비스 구현부
    @Override
    public List<TimeTableResponseDTO> get(String email) {
        List<TimeTable> timeTables = repository.findTimetableByStudentEmail(email);

        return timeTables.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    //T-4) 교수 or 관리자가 특정 강의에 대한 시간표를 생성하기 위한 서비스 구현부 -> 문제 좀 많음 수정 해야함
    // -> 시간표에 대한 생성 기능은 구현 o 하지만 시간표를 생성할떄 그 강의 
    // 구상: serviceImpl 구현부내에 userId값을 받아서 검증하는 함수를 만들어서 검증예정
    @Override
    public TimeTableResponseDTO post(TimeTableCreateDTO dto, String requesterEmail) {
        log.info("2) 시간표 생성 -교수:{}, offeringId:{}",requesterEmail,dto.getOfferingId());

        CourseOffering offering=offeringService.getCourseOfferingEntity(dto.getOfferingId());

        validateProfessorOwnership(offering,requesterEmail);

        TimeTable timeTable=TimeTable.builder()
                .courseOffering(offering)
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .location(dto.getLocation())
                .build();

        TimeTable saveTimeTable=repository.save(timeTable);

        log.info("3) 시간표 생성 완료 -timetableId:{}, 교수:{}",saveTimeTable.getTimetableId(),requesterEmail);

        return toResponseDTO(saveTimeTable);
    }



    @Override
    public TimeTableResponseDTO put(Integer timetableId,TimeTableCreateDTO dto,String requesterEmail) {
        log.info("시간표 수정 시작 -timetableId:{},교수이름:{}",timetableId,requesterEmail);

        CourseOffering offering=offeringService.getCourseOfferingEntity(dto.getOfferingId());

        TimeTable timeTable=repository.findById(timetableId)
                .orElseThrow(()->new TimeTableNotFoundException("시간표가 존재하지 않습니다. timeId:" + timetableId));

        validateReviewOwnership(timeTable,offering,requesterEmail);

        timeTable.setLocation(dto.getLocation());
        timeTable.setDayOfWeek(dto.getDayOfWeek());
        timeTable.setStartTime(dto.getStartTime());
        timeTable.setEndTime(dto.getEndTime());

        TimeTable updateTimeTable=repository.save(timeTable);

        log.info("시간표 수정 성공 -교수:{}, timetableId:{},강의실:{},요일:{},시작시간:{},종료시간:{}",
                requesterEmail,
                timetableId,
                dto.getLocation(),
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime());


        return toResponseDTO(updateTimeTable);
    }

    @Override
    public TimeTable delete() {
        return null;
    }

    @Override
    public ResponseEntity<String> addTimeTable(TimeTableDTO timeTableDTO) {
        return null;
    }
}
