package com.univercity.unlimited.greenUniverCity.function.academic.timetable.service;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.service.ClassroomService;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.service.ClassSectionService;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.repository.TimeTableRepository;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TimeTableServiceImpl implements TimeTableService{
    private final TimeTableRepository repository;

    private final ClassSectionService sectionService;

    private final ClassroomService classroomService;

    private final TimeTableValidationService validationService;

    private final AcademicSecurityValidator validator;

    private final EntityMapper entityMapper;


    @Override
    @Transactional(readOnly = true)
    public List<TimeTableResponseDTO> findAllTimeTable() {
        log.info("2) 시간표 전체조회 시작");

        return repository.findAllWithDetails().stream()
                .map(entityMapper::toTimeTableResponseDTO)
                .toList();
    }

    //T-2)특정 과목에 존재하는 모든 시간표를 조회하기 위한 서비스 구현부
    //offeringId 값을 받아서 조회를 해야 하는데 db 데이터 문제로 임시적으로 요일을 받아서
    //조회 할 수 있게 기능을 구현해놨음 추후 코드 수정 예정
    //-> 그리고 지금 postmanTest에서 데이터를 조회 할 때 1번째 동작에서 호출을 못하고
    // 2번째 동작부터 호출을 하는 문제가 있음
    @Override
    @Transactional(readOnly = true)
    public List<TimeTableResponseDTO> offeringOfTimeTable(Long offeringId) {
        log.info("2) 특정 시간표 조회 시작 offeringId-:{}",offeringId);

        return repository.findTimeTableByOfferingId(offeringId).stream()
                .map(entityMapper::toTimeTableResponseDTO)
                .toList();
    }
    
    //T-2-1-1) 본인 id를 활용하여 단건 조회를 할 수 있는 service구현부
    @Override
    @Transactional(readOnly = true)
    public TimeTableResponseDTO getTimeTable(Long timetableId) {
        log.info("2) 시간표 단건 조회 시작 - timetableId-:{}",timetableId);

        TimeTable timeTable = validator.getEntityOrThrow(repository, timetableId, "시간표");

        return entityMapper.toTimeTableResponseDTO(timeTable);
    }

    //T-3)특정 학생이 신청한 모든 과목의 시간표를 조회하기 위한 서비스 구현부
    @Override
    public List<TimeTableResponseDTO> studentOfTimeTable(String email) {
        log.info("2) 학생이 시간표 조회 요청 학생-:{}",email);

        return repository.findTimetableByStudentEmail(email).stream()
                .map(entityMapper::toTimeTableResponseDTO)
                .toList();
    }

    //T-4) 교수 or 관리자가 특정 강의에 대한 시간표를 생성하기 위한 서비스 구현부 -> 문제 좀 많음 수정 해야함
    // -> 시간표에 대한 생성 기능은 구현 o 하지만 시간표를 생성할떄 그 강의
    // 구상: serviceImpl 구현부내에 userId값을 받아서 검증하는 함수를 만들어서 검증예정
    @Override
    public TimeTableResponseDTO createTimeTableForProfessor(TimeTableCreateDTO dto, String requesterEmail) {
        log.info("2) 시간표 생성 -교수:{}, getSectionId:{}",requesterEmail,dto.getSectionId());

        ClassSection section=sectionService.getClassSectionEntity(dto.getSectionId());
        CourseOffering offering=section.getCourseOffering();

        // T-security 보안검사
        validator.validateProfessorOwnership(offering,requesterEmail,"시간표 생성");

        // 강의실 조회
        Classroom classroom = classroomService.getClassroomEntity(dto.getClassroomId());

        // 중복 검사
        validationService.validateTimeOverlap(
                classroom.getClassroomId(), dto.getDayOfWeek(),
                dto.getStartTime(), dto.getEndTime()
        );


        TimeTable timeTable=TimeTable.builder()
                .classSection(section)
                .classroom(classroom)
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();


        log.info("5) 시간표 생성 완료");

        return entityMapper.toTimeTableResponseDTO(repository.save(timeTable));
    }

    //T-5) 교수가 본인이 담당하고 있는 수업에 존재하는 시간표를 수정하기 위한 서비스 구현부
    @Override
    public TimeTableResponseDTO updateTimeTableForProfessor(TimeTableUpdateDTO dto, String requesterEmail) {
        log.info("2) 시간표 수정 시작 -timetableId-:{}, 교수-:{}",dto.getTimetableId(),requesterEmail);

        TimeTable timeTable = validator.getEntityOrThrow(repository, dto.getTimetableId(), "시간표");

        // T-security 보안검사보안 검사(소유권 검증)
        CourseOffering offering = timeTable.getClassSection().getCourseOffering();
        validator.validateProfessorOwnership(offering,requesterEmail,"시간표 수정");

        // 강의실 조회
        Classroom classroom = classroomService.getClassroomEntity(dto.getClassroomId());

        // 2. 중복 검사 (수정용)
        validationService.validateTimeOverlapExcludingId(
                classroom.getClassroomId(), dto.getDayOfWeek(),
                dto.getStartTime(), dto.getEndTime(),
                dto.getTimetableId()
        );

        timeTable.updateTimeTableInfo(
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime(),
                classroom
        );

        TimeTable updateTimeTable=repository.save(timeTable);

        log.info("5) 시간표 수정 성공 -교수:{}, timetableId:{},요일:{},시작시간:{},종료시간:{}",
                requesterEmail,
                dto.getTimetableId(),
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime());

        return entityMapper.toTimeTableResponseDTO(updateTimeTable);
    }

    //T-6) 교수 or 관리자가 개설된 강의에 대한 시간표를 삭제하기 위한 서비스 구현부
    @Override
    public void deleteByTimeTable(Long timetableId,String requesterEmail) {
        log.info("2) 시간표 삭제 요청 -교수: {} ,timetableId: {}",requesterEmail,timetableId);

        //시간표 조회
        TimeTable timeTable = validator.getEntityOrThrow(repository, timetableId, "시간표");

        // T-security 보안검사
        CourseOffering offering = timeTable.getClassSection().getCourseOffering();
        validator.validateProfessorOwnership(offering,requesterEmail,"시간표 삭제");

        repository.delete(timeTable);

        log.info("5)시간표 삭제 성공 -교수: {},timetableId: {}",requesterEmail,timetableId);
    }

}
