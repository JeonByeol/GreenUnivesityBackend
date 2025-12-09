package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GradeItemServiceImpl implements GradeItemService{
    @Override
    public GradeItemResponseDTO createGradeItem(GradeItemCreateDTO dto) {
        return null;
    }

    @Override
    public GradeItemResponseDTO getGradeItem(Long itemId) {
        return null;
    }

    @Override
    public List<GradeItemResponseDTO> getOfferingGradeItem(Long offeringId) {
        return List.of();
    }

    @Override
    public GradeItemResponseDTO updateGradeItem(Long itemId, GradeItemUpdateDTO dto) {
        return null;
    }

    @Override
    public boolean validateWeightSum(Long offeringId) {
        return false;
    }

    @Override
    public Long countOfferingGradeItems(Long offeringId) {
        return 0L;
    }

    @Override
    public GradeItem getGradeItemEntity(Long itemId) {
        return null;
    }
}
