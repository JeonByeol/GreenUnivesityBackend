package com.univercity.unlimited.greenUniverCity.function.academic.section.service;

import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.dto.ClassSectionUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassSectionServiceImpl implements ClassSectionService{

    private ClassSectionResponseDTO toResponseDTO(ClassSection section){
        CourseOffering courseOffering=section.getCourseOffering();
        User user=courseOffering.getProfessor();

        return
                ClassSectionResponseDTO.builder()
                        .sectionId(section.getSectionId())
                        .sectionName(section.getSectionName())
                        .maxCapacity(section.getMaxCapacity())
                        
                        .build();
    }

    @Override
    public List<ClassSectionResponseDTO> findAllSection() {
        return List.of();
    }

    @Override
    public List<ClassSectionResponseDTO> findSectionsByOfferingId(Long offeringId) {
        return List.of();
    }

    @Override
    public ClassSectionResponseDTO createSection(ClassSectionCreateDTO dto, String email) {
        return null;
    }

    @Override
    public ClassSectionResponseDTO updateSection(ClassSectionUpdateDTO dto, String email) {
        return null;
    }

    @Override
    public void deleteSection(Long sectionId, String email) {

    }
}
