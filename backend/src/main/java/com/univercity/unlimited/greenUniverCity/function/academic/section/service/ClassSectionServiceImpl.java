package com.univercity.unlimited.greenUniverCity.function.academic.section.service;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.service.ClassroomService;
import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.service.TimeTableValidationService;
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
public class ClassSectionServiceImpl implements ClassSectionService{

    private final ClassSectionRepository repository;
    private final CourseOfferingService offeringService;
    private final ClassroomService classroomService;
    private final AcademicSecurityValidator validator;
    private final TimeTableValidationService timeTableValidationService;
    private final EntityMapper entityMapper;

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
                throw new IllegalStateException("해당 강의실/시간에 이미 수업이 있습니다: " + classroom.getLocation());
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
    @Transactional(readOnly = true)
    public List<ClassSectionResponseDTO> findAllSection() {
        log.info("2) 분반 전체조회 시작");

        return repository.findAllWithDetails().stream()
                .map(entityMapper::toClassSectionResponseDTO) // 매퍼에게 외주 맡김 (깔끔!)
                .toList();
    }

    //SE-2) 특정 개설강의에 존재하는 분반에 대한 목록을 조회하기 위한 서비스 구현부
    @Override
    @Transactional(readOnly = true)
    public List<ClassSectionResponseDTO> findSectionsByOfferingId(Long offeringId) {
        log.info("2) 강의별 분반 조회 시작 offeringId-:{}",offeringId);

        return repository.findSectionByOfferingId(offeringId).stream()
                .map(entityMapper::toClassSectionResponseDTO)
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

        ClassSection savedSection=repository.save(classSection);

        log.info("5) 분반 생성 완료 sectionId-:{}, 교수-:{}",
                savedSection.getSectionId(), email);

        return entityMapper.toClassSectionResponseDTO(savedSection);
    }
    
    //SE-4) 특정 강의에 생성 된 분반의 내용을 수정하기 위한 서비스 구현부
    @Override
    public ClassSectionResponseDTO updateSection(ClassSectionUpdateDTO dto, String email) {
        log.info("2) 분반 수정 시작 sectionId-:{}, 교수-:{}",
                dto.getSectionId(), email);

        ClassSection classSection = validator.getEntityOrThrow(repository, dto.getSectionId(), "분반");
        
        //SE-security 보안검사
        CourseOffering offering= classSection.getCourseOffering();
        validator.validateProfessorOwnership(offering,email,"분반 수정");

        // 2. [수정] 편의 메서드 사용
        classSection.updateSectionInfo(dto.getSectionName(), dto.getMaxCapacity(), dto.getSectionType());

        ClassSection updatedSection=repository.save(classSection);

        log.info(
                "5) 분반 정보 수정 성공 교수-:{}, sectionId-:{}, 분반명-:{}, 수강정원-:{}",
                email,
                dto.getSectionId(),
                dto.getSectionName(),
                dto.getMaxCapacity()
        );

        return entityMapper.toClassSectionResponseDTO(updatedSection);
    }
    
    //SE-5) 특정 강의를 잘못 생성했을 경우 삭제하기 위한 서비스 구현부
    @Override
    public void deleteSection(Long sectionId, String email) {
        log.info("2) 분반 삭제 요청 교수-:{}, sectionId-:{}",email,sectionId);

        ClassSection classSection = validator.getEntityOrThrow(repository, sectionId, "분반");

        CourseOffering offering=classSection.getCourseOffering();
        validator.validateProfessorOwnership(offering,email,"분반 삭제");

        repository.delete(classSection);

        log.info("5)분반 삭제 요청 성공 교수-:{},sectionId-:{}",email,sectionId);
    }
    
    //SE-6) 개발환경 sectionId를 통해 데이터 조회
    @Override
    @Transactional(readOnly = true)
    public ClassSectionResponseDTO getSection(Long sectionId) {
        log.info("2) 특정 분반 상세 조회 시작 sectionId-:{}", sectionId);

        ClassSection section = validator.getEntityOrThrow(repository, sectionId, "분반");
        return entityMapper.toClassSectionResponseDTO(section);
    }

    //SE-E) 외부 Service에서 classSection 정보 조회 및 활용
    @Override
    @Transactional(readOnly = true)
    public ClassSection getClassSectionEntity(Long sectionId) {
        return validator.getEntityOrThrow(repository, sectionId, "분반");
    }
}
