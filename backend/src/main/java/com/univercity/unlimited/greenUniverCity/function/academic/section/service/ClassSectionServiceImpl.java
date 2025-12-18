package com.univercity.unlimited.greenUniverCity.function.academic.section.service;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.service.ClassroomService;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentCountService;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.ClassSectionNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.service.TimeTableService;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.service.TimeTableValidationService;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassSectionServiceImpl implements ClassSectionService{

    private final ClassSectionRepository repository;

    private final CourseOfferingService offeringService;

    private final EnrollmentCountService enrollmentCountService;

    private final ClassroomService classroomService;

    private final AcademicSecurityValidator validator;

    private final TimeTableValidationService timeTableValidationService;


    /**
     * SE-A) ClassSection 엔티티를 (Response)DTO로 변환
     * - 각각의 crud 기능에 사용되는 서비스 구현부에 사용하기 위해 함수로 생성
     */

    private ClassSectionResponseDTO toResponseDTO(ClassSection section){
        CourseOffering courseOffering=section.getCourseOffering();
        User user=courseOffering.getProfessor();

        // 1. 현재 수강 인원 계산 (EnrollmentService 사용)
        Integer currentCount = enrollmentCountService.getCurrentEnrollmentCount(section.getSectionId());
        int safeCount = (currentCount != null) ? currentCount : 0;

        // 2. 시간표 정보 변환 (Entity List -> DTO List)
        List<TimeTableResponseDTO> timeTableDTOs = section.getTimeTables().stream()
                .map(tt -> TimeTableResponseDTO.builder()
                        .dayOfWeek(tt.getDayOfWeek())
                        .startTime(tt.getStartTime())
                        .endTime(tt.getEndTime())
                        .classroomId(tt.getClassroom() != null ? tt.getClassroom().getClassroomId() : null)
                        .classroomName(tt.getClassroom() != null ? tt.getClassroom().getLocation() : "미정")
                        .build())
                .toList();

        ClassSectionResponseDTO response= ClassSectionResponseDTO.builder()
                        .sectionId(section.getSectionId())
                        .sectionName(section.getSectionName())
                        .maxCapacity(section.getMaxCapacity())
                        .currentCount(safeCount)
                        .sectionType(section.getSectionType())
                        .offeringId(courseOffering.getOfferingId())
                        .courseName(courseOffering.getCourseName())
                        .year(courseOffering.getYear())
                        .semester(courseOffering.getSemester())
                        .professorName(user.getNickname())
                        .timeTables(timeTableDTOs) // 시간표 포함
                        .build();

        // 3. 계산 필드 추가 (Service에서 계산)
        calculateAndSetAdditionalFields(response);

        return response;
    }

    /**
     * 계산 필드를 계산하여 DTO에 설정
     * - maxCapacity가 null이면 계산하지 않음
     * - currentCount는 이미 null-safe 처리됨 (toResponseDTO에서 0으로 변환)
     */
    private void calculateAndSetAdditionalFields(ClassSectionResponseDTO response) {
        Integer max = response.getMaxCapacity();
        Integer current = response.getCurrentCount();

        // max가 null이면 계산 불가
        if (max == null) return;
        // 1. 남은 자리 계산
        response.setAvailableSeats(max - current);

        // 2. 마감 여부 계산
        response.setIsFull(current >= max);

        // 3. 수강률 계산 (백분율)
        response.setEnrollmentRate(max > 0 ? (current.doubleValue() / max) * 100.0 : 0.0);
    }
    
    //시간표 등록 로직
    private void registerTimeTables(ClassSection section, List<TimeTableResponseDTO> timeTables) {
        if (timeTables == null || timeTables.isEmpty()) return;

        for (TimeTableResponseDTO ttDto : timeTables) {
            Classroom classroom = classroomService.getClassroomEntity(ttDto.getClassroomId());

            // 중복 검사
            boolean isOverlap = timeTableValidationService.validateTimeOverlap(
                    classroom.getClassroomId(), ttDto.getDayOfWeek(),
                    ttDto.getStartTime(), ttDto.getEndTime()
            );

            if (isOverlap) {
                throw new IllegalStateException("시간표 중복 발생: " + classroom.getLocation());
            }

            // 생성 및 연결
            TimeTable timeTable = TimeTable.builder()
                    .dayOfWeek(ttDto.getDayOfWeek())
                    .startTime(ttDto.getStartTime())
                    .endTime(ttDto.getEndTime())
                    .classroom(classroom)
                    .classSection(section)
                    .build();

            section.addTimeTable(timeTable);
        }
    }
    @Override
    public List<ClassSectionResponseDTO> findAllSection() {
        log.info("2) 분반 전체조회 시작");
        List<ClassSection> classSections=repository.findAll();

        log.info("3) 분반 전체조회 성공");

        return classSections.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //SE-2) 특정 개설강의에 존재하는 분반에 대한 목록을 조회하기 위한 서비스 구현부
    @Override
    public List<ClassSectionResponseDTO> findSectionsByOfferingId(Long offeringId) {
        log.info("2) 특정 개설강의에 대한 분반 전체조회 시작 offeringId-:{}",offeringId);
        List<ClassSection> classSections=repository.findSectionByOfferingId(offeringId);

        log.info("3) 특정 개설강의에 존재하는 분반 조회 성공");

        return classSections.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //SE-3) 특정 개설강의에 대한 새로운 분반을 생성하기 위한 서비스 구현부 
    @Override
    public ClassSectionResponseDTO createSection(ClassSectionCreateDTO dto, String email) {
        log.info("2) 분반 생성 교수-:{}, offeringId-:{}",
                email, dto.getOfferingId());

        CourseOffering offering=offeringService.getCourseOfferingEntity(dto.getOfferingId());

        //SE-security 보안검사
        validator.validateProfessorOwnership(offering, email,"분반 생성");


        ClassSection classSection= ClassSection.builder()
                .courseOffering(offering)
                .maxCapacity(dto.getMaxCapacity())
                .sectionName(dto.getSectionName())
                .sectionType(dto.getSectionType())
                .build();

        // 2. 시간표 리스트 처리 (Method A: 한 번에 저장)
        registerTimeTables(classSection, dto.getTimeTables());

        ClassSection saveClassSection=repository.save(classSection);

        log.info("5) 분반 생성 완료 sectionId-:{}, 교수-:{}",
                saveClassSection.getSectionId(), email);

        return toResponseDTO(saveClassSection);
    }
    
    //SE-4) 특정 강의에 생성 된 분반의 내용을 수정하기 위한 서비스 구현부
    @Override
    public ClassSectionResponseDTO updateSection(ClassSectionUpdateDTO dto, String email) {
        log.info("2) 분반 수정 시작 sectionId-:{}, 교수-:{}",
                dto.getSectionId(), email);

        ClassSection classSection=repository.findById(dto.getSectionId())
                .orElseThrow(()->new ClassSectionNotFoundException(
                        "3)보안검사 시도 식별코드 -:SE-4 " +
                                "분반이 존재하지 않습니다. sectionId-:" + dto.getSectionId()));
        
        //SE-security 보안검사
        CourseOffering offering= classSection.getCourseOffering();
        validator.validateProfessorOwnership(offering,email,"분반 수정");

        classSection.setSectionName(dto.getSectionName());
        classSection.setMaxCapacity(dto.getMaxCapacity());
        classSection.setSectionType(dto.getSectionType());

        ClassSection updateClassSection=repository.save(classSection);

        log.info(
                "5) 분반 정보 수정 성공 교수-:{}, sectionId-:{}, 분반명-:{}, 수강정원-:{}",
                email,
                dto.getSectionId(),
                dto.getSectionName(),
                dto.getMaxCapacity()
        );

        return toResponseDTO(updateClassSection);
    }
    
    //SE-5) 특정 강의를 잘못 생성했을 경우 삭제하기 위한 서비스 구현부
    @Override
    public void deleteSection(Long sectionId, String email) {
        log.info("2) 분반 삭제 요청 교수-:{}, sectionId-:{}",email,sectionId);

        ClassSection classSection=repository.findById(sectionId)
                .orElseThrow(()->new ClassSectionNotFoundException(
                        "3)보안검사 시도 식별 코드 -: SE-5 " +
                                "분반이 존재하지 않습니다. sectionId:" + sectionId));

        CourseOffering offering=classSection.getCourseOffering();
        validator.validateProfessorOwnership(offering,email,"분반 삭제");

        repository.delete(classSection);

        log.info("5)분반 삭제 요청 성공 교수-:{},sectionId-:{}",email,sectionId);
    }
    
    //SE-6) 개발환경 sectionId를 통해 데이터 조회
    @Override
    public ClassSectionResponseDTO getSection(Long sectionId) {
        log.info("2) 특정 분반 상세 조회 시작 sectionId-:{}", sectionId);
        ClassSection section = repository.findById(sectionId)
                .orElseThrow(() -> new ClassSectionNotFoundException(
                        "분반이 존재하지 않습니다. sectionId:" + sectionId));
        return toResponseDTO(section);
    }

    //SE-E) 외부 Service에서 classSection 정보 조회 및 활용
    @Override
    public ClassSection getClassSectionEntity(Long sectionId) {
        return repository.findById(sectionId)
                .orElseThrow(()->new IllegalArgumentException("분반이 존재하지 않습니다."));
    }
}
