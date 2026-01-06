package com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.service;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.entity.AcademicTerm;
import com.univercity.unlimited.greenUniverCity.util.BaseApplyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AcademicTermApplyMapper
        extends BaseApplyMapper<AcademicTermCreateDTO, AcademicTermUpdateDTO, AcademicTerm> {

    public AcademicTermApplyMapper() {
        super("AcademicTerm");
    }

    @Override
    protected void validateCreate(AcademicTermCreateDTO dto) {
        super.validateCreate(dto);

        if (dto.getSemester() == null || dto.getSemester().isBlank()) {
            throw new IllegalArgumentException("semester는 필수입니다.");
        }
        if (dto.getRegistrationStart() == null) {
            throw new IllegalArgumentException("registrationStart는 필수입니다.");
        }
        if (dto.getRegistrationEnd() == null) {
            throw new IllegalArgumentException("registrationEnd는 필수입니다.");
        }
    }

    @Override
    protected void validateUpdate(AcademicTermUpdateDTO dto) {
        super.validateUpdate(dto);

        if (dto.getTermId() == null) {
            throw new IllegalArgumentException("termId는 필수입니다.");
        }
        if (dto.getSemester() == null || dto.getSemester().isBlank()) {
            throw new IllegalArgumentException("semester는 필수입니다.");
        }
        if (dto.getRegistrationStart() == null) {
            throw new IllegalArgumentException("registrationStart는 필수입니다.");
        }
        if (dto.getRegistrationEnd() == null) {
            throw new IllegalArgumentException("registrationEnd는 필수입니다.");
        }
    }

    @Override
    protected void validateEntity(AcademicTerm entity) {
        super.validateEntity(entity);

        if (entity.getTermId() == null) {
            throw new IllegalArgumentException("AcademicTerm이 영속화되지 않았습니다.");
        }
        if (entity.getSemester() == null || entity.getSemester().isBlank()) {
            throw new IllegalArgumentException("semester가 비어 있습니다.");
        }
        if (entity.getRegistrationStart() == null) {
            throw new IllegalArgumentException("registrationStart가 null입니다.");
        }
        if (entity.getRegistrationEnd() == null) {
            throw new IllegalArgumentException("registrationEnd가 null입니다.");
        }
    }

    @Override
    protected AcademicTerm createEntity(AcademicTermCreateDTO dto) {
        return AcademicTerm.builder()
                .year(dto.getYear())
                .semester(dto.getSemester())
                .registrationStart(dto.getRegistrationStart())
                .registrationEnd(dto.getRegistrationEnd())
                .isCurrent(dto.isCurrent())
                .build();
    }

    @Override
    protected AcademicTerm updateEntity(AcademicTermUpdateDTO dto, AcademicTerm entity) {
        entity.setYear(dto.getYear());
        entity.setSemester(dto.getSemester());
        entity.setRegistrationStart(dto.getRegistrationStart());
        entity.setRegistrationEnd(dto.getRegistrationEnd());
        entity.setCurrent(dto.isCurrent());
        return entity;
    }
}