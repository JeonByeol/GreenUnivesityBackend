package com.univercity.unlimited.greenUniverCity.function.academic.section.service;

import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import com.univercity.unlimited.greenUniverCity.function.community.review.exception.*;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
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

    private final AcademicSecurityValidator validator;

    private final EnrollmentService enrollmentService;

    /**
     * SE-A) ClassSection 엔티티를 (Response)DTO로 변환
     * - 각각의 crud 기능에 사용되는 서비스 구현부에 사용하기 위해 함수로 생성
     */

    private ClassSectionResponseDTO toResponseDTO(ClassSection section){
        CourseOffering courseOffering=section.getCourseOffering();
        User user=courseOffering.getProfessor();

        return
                ClassSectionResponseDTO.builder()
                        .sectionId(section.getSectionId())
                        .sectionName(section.getSectionName())
                        .maxCapacity(section.getMaxCapacity())
//                        .currentCount() ** enrollment에서 큐런트 카운트를 계산하는 쿼리가 필요함
                        .offeringId(courseOffering.getOfferingId())
                        .courseName(courseOffering.getCourseName())
                        .year(courseOffering.getYear())
                        //.semester(courseOffering.getSemester()) ** courseOffering에서 semester String 변경필요 혹은 삭제
                        .professorName(user.getNickname())
                        .build();
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
        log.info("2) 분반 생성 교수-:{}, offeringId-:{}",email,dto.getOfferingId());

        CourseOffering offering=offeringService.getCourseOfferingEntity(dto.getOfferingId());

        //SE-security 보안검사
        validator.validateProfessorOwnership(offering,email,"분반 생성");

        ClassSection classSection= ClassSection.builder()
                .courseOffering(offering)
                .maxCapacity(dto.getMaxCapacity())
                .sectionName(dto.getSectionName())
                .build();

        ClassSection saveClassSection=repository.save(classSection);

        log.info("5) 분반 생성 완료 sectionId-:{}, 교수-:{}",saveClassSection.getSectionId(),email);

        return toResponseDTO(saveClassSection);
    }
    
    //SE-4) 특정 강의에 생성 된 분반의 내용을 수정하기 위한 서비스 구현부
    @Override
    public ClassSectionResponseDTO updateSection(ClassSectionUpdateDTO dto, String email) {
        log.info("2) 분반 수정 시작 sectionId-:{}, 교수-:{}",dto.getSectionId(),email);

        ClassSection classSection=repository.findById(dto.getSectionId())
                .orElseThrow(()->new ClassSectionNotFoundException(
                        "3)보안검사 시도 식별코드 -:SE-4 " +
                                "분반이 존재하지 않습니다. sectionId-:" + dto.getSectionId()));
        
        //SE-security 보안검사
        CourseOffering offering= classSection.getCourseOffering();
        validator.validateProfessorOwnership(offering,email,"분반 수정");

        classSection.setSectionName(dto.getSectionName());
        classSection.setMaxCapacity(dto.getMaxCapacity());

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
}
