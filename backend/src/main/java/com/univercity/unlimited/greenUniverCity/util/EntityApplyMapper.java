package com.univercity.unlimited.greenUniverCity.util;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.entity.AcademicTerm;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.respository.AcademicTermRepository;
import com.univercity.unlimited.greenUniverCity.util.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class EntityApplyMapper {
    /**
     * DTOMapper-A) 각종 엔티티들을 (Entity) 로 변환
     * - 각각의 crud 기능에 사용되는 서비스 구현부에 사용하기 MapperUtil로 생성
     */

    // =========================
    // 1️⃣ Entity 검증
    // =========================
    // DTOValidate-AT) Entity Check .
    private void validateEntity(AcademicTerm entity) {
        if (entity == null) {
            throw new EntityNotFoundException("AcademicTerm Entity 가 null 입니다.");
        }

        if (entity.getTermId() == null) {
            throw new IllegalStateException("AcademicTerm 이 영속화되지 않았습니다.");
        }

        if (entity.getSemester() == null || entity.getSemester().isBlank()) {
            throw new IllegalStateException("기존 AcademicTerm semester 가 비어 있습니다.");
        }

        if (entity.getRegistrationStart() == null) {
            throw new IllegalStateException("기존 AcademicTerm registrationStart 가 null 입니다.");
        }

        if (entity.getRegistrationEnd() == null) {
            throw new IllegalStateException("기존 AcademicTerm registrationEnd 가 null 입니다.");
        }
    }
    
    // =========================
    // 2️⃣ DTO 검증
    // =========================
    // DTOValidate-ATC) AcademicTerm CreateDTO Check .
    private void validateCreate(AcademicTermCreateDTO dto) {
        if (dto.getSemester() == null || dto.getSemester().isBlank()) {
            throw new IllegalArgumentException("semester 는 비어 있을 수 없습니다.");
        }

        if (dto.getRegistrationStart() == null) {
            throw new IllegalArgumentException("registrationStart 는 비어 있을 수 없습니다.");
        }

        if (dto.getRegistrationEnd() == null) {
            throw new IllegalArgumentException("registrationEnd 는 비어 있을 수 없습니다.");
        }
    }

    // DTOValidate-ATU) AcademicTerm UpdateDTO Check .
    private void validateUpdate(AcademicTermUpdateDTO dto) {
        if(dto.getTermId() == null){
            throw new IllegalArgumentException("termId 는 비어 있을 수 없습니다.");
        }

        if (dto.getSemester() == null || dto.getSemester().isBlank()) {
            throw new IllegalArgumentException("semester 는 비어 있을 수 없습니다.");
        }

        if (dto.getRegistrationStart() == null) {
            throw new IllegalArgumentException("registrationStart 는 비어 있을 수 없습니다.");
        }

        if (dto.getRegistrationEnd() == null) {
            throw new IllegalArgumentException("registrationEnd 는 비어 있을 수 없습니다.");
        }
    }

    // =========================
    // 3️⃣ DTO → Entity 변환
    // =========================
    // DTOMapper-ATC) AcademicTerm CreateDTO -> AcademicTerm
    public AcademicTerm applyCreate(AcademicTermCreateDTO dto){
        log.info("1) AcademicTerm CreateDTO -> AcademicTerm . Ready to enter");
        log.info("1-2) AcademicTerm CreateDTO : {}", dto);

        log.info("2) AcademicTerm CreateDTO -> AcademicTerm . Check");
        validateCreate(dto);

        log.info("3) AcademicTerm CreateDTO -> AcademicTerm . Swap");
        AcademicTerm academicTerm = AcademicTerm.builder()
                .year(dto.getYear())
                .semester(dto.getSemester())
                .registrationStart(dto.getRegistrationStart())
                .registrationEnd(dto.getRegistrationEnd())
                .build();

        return academicTerm;
    }

    // DTOMapper-ATU) AcademicTerm UpdateDTO -> AcademicTerm
    public AcademicTerm applyUpdate(AcademicTermUpdateDTO dto, AcademicTerm entity){
        log.info("1) AcademicTerm UpdateDTO -> AcademicTerm . Ready to enter");
        log.info("1-2) AcademicTerm UpdateDTO : {}", dto);

        log.info("2) AcademicTerm UpdateDTO -> AcademicTerm . Check");
        validateUpdate(dto);
        validateEntity(entity);

        log.info("3) AcademicTerm UpdateDTO -> AcademicTerm . Swap");
        entity.setYear(dto.getYear());
        entity.setSemester(dto.getSemester());
        entity.setRegistrationStart(dto.getRegistrationStart());
        entity.setRegistrationEnd(dto.getRegistrationEnd());
        entity.setCurrent(dto.isCurrent());

        return entity;
    }
}
