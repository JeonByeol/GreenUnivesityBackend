package com.univercity.unlimited.greenUniverCity.function.academic.section.service;

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

    private final UserService userService;

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

    /**
     * SE-security) 보안 검사:
     * - 교수 권한 검증: CourseOffering의 담당 교수와 요청자가 일치하는지 확인
     */

    private void validateProfessorOwnerShip(CourseOffering offering,String email,String action){
        log.info("4) 분반-:{} 교수 권함 검증 시작-:{}",action,email);
        
        // 1. 요청자 조회 userService에 존재하는 U-E service 구현부에 requesterEmail을 전달하여 유저를 조회한다
        User requester= userService.getUserByEmail(email);

        // 2. 요청자의 교수 권한을 확인
        if(!requester.getUserRoleList().contains(UserRole.PROFESSOR)){
            throw new InvalidRoleException(
                    String.format(
                            "4)보안검사 시도 식별코드 -: SE-security-1 (분반 %s) " +
                                    "교수 권한이 존재하지 않습니다 " +
                                    "요청자-: %s, userid-: %s, 현재 역할-: %s",
                            action,email,requester.getUserId(),requester.getUserRoleList())
            );
        }

        // 3. 담당 교수가 존재하는지 확인
        User professor=offering.getProfessor();

        if(professor == null){
            throw new DataIntegrityException(
                    String.format(
                            "4)보안검사 시도 식별코드 -: SE-security-2 (분반 %s) " +
                                    "데이터 오류: 개설 강의에 담당 교수가 없습니다. offeringId-: %s",
                            action,offering.getOfferingId())
            );
        }

        // 4.담당 교수의 역할 확인
        if(!professor.getUserRoleList().contains(UserRole.PROFESSOR)){
            throw new InvalidRoleException(
                    String.format(
                            "4)보안검사 시도 식별코드 -: SE-security-3 (분반 %s) " +
                                    "데이터 오류: 담당자가 교수 권한이 없습니다. " +
                                    "userId-: %s, 현재 역할-: %s",
                            action,professor.getUserId(),professor.getUserRoleList())
            );
        }

        // 5.요청자와 담당 교수 일치 확인
        if(!professor.getUserId().equals(requester.getUserId())){
            throw new UnauthorizedException(
                    String.format(
                            "4)보안검사 시도 식별코드 -: SE-security-4 (분반 %s) " +
                                    "해당 강의의 담당 교수만 시간표를 %s 할 수 있습니다. " +
                                    "담당 교수: %s (userId: %s), 요청자: %s (userId: %s)",
                            action,action,
                            professor.getEmail(),professor.getUserId(),
                            email,requester.getUserId())
            );
        }
        log.info("4)교수 권한 검증 완료 교수-:{}, 작업-:{}",email,action);
    }


    //SE-1) 분반 테이블에 존재하는 모든 데이터를 조회하기 위한 서비스 구현부
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
        validateProfessorOwnerShip(offering,email,"생성");

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
        validateProfessorOwnerShip(offering,email,"수정");

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
        validateProfessorOwnerShip(offering,email,"삭제");

        repository.delete(classSection);

        log.info("5)분반 삭제 요청 성공 교수-:{},sectionId-:{}",email,sectionId);
    }
}
