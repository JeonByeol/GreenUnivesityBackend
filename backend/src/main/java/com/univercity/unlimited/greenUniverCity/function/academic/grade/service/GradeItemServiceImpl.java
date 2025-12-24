package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItemType;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeItemRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GradeItemServiceImpl implements GradeItemService{

    private final GradeItemRepository repository;
    private final CourseOfferingRepository offeringRepository;

    private final AcademicSecurityValidator validator;
    private final EntityMapper entityMapper;

    //GI-1) 평가항목 생성
    @Override
    public GradeItemResponseDTO createGradeItem(GradeItemCreateDTO dto, String professorEmail) {
        Long offeringId = dto.getOfferingId();
        String itemName = dto.getItemName();

        log.info("2) 평가항목 생성 시작 - offeringId-:{}, itemName-:{}, 교수-:{}",
                offeringId, itemName, professorEmail);

        CourseOffering offering = getOfferingOrThrow(offeringId);
        validator.validateProfessorOwnership(offering, professorEmail, "평가항목 생성");

        boolean exists = repository.existsByCourseOffering_OfferingIdAndItemName(offeringId, itemName);
        validator.validateDuplicate(exists, "평가 항목(" + itemName + ")");

        checkTotalWeightLimit(dto.getOfferingId(), null, dto.getWeightPercent());

        GradeItem gradeItem = GradeItem.builder()
                .courseOffering(offering)
                .itemName(dto.getItemName())
                .itemType(dto.getItemType())
                .maxScore(dto.getMaxScore())
                .weightPercent(dto.getWeightPercent())
                .build();

        return entityMapper.toGradeItemResponseDTO(repository.save(gradeItem));
    }

    //GI-2) 평가항목 단건 조회
    @Override
    @Transactional(readOnly = true)
    public GradeItemResponseDTO getGradeItem(Long itemId) {
        log.info("2) 평가항목 조회 시작 - itemId-:{}", itemId);

        GradeItem gradeItem = getGradeItemOrThrow(itemId);

        return entityMapper.toGradeItemResponseDTO(gradeItem);
    }

    //GI-3) 강의별 평가항목 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<GradeItemResponseDTO> getOfferingGradeItem(Long offeringId) {
        log.info("2) 강의별 평가항목 조회 시작 - offeringId-:{}", offeringId);

        List<GradeItem> items=repository.findByOfferingId(offeringId);

        log.info("3) 강의별 평가 항목 조회 완료 - offeringId-:{}, 항목 개수-:{}",
                offeringId, items.size());

        return items.stream()
                .map(entityMapper::toGradeItemResponseDTO)
                .toList();
    }

    //GI-4) 평가항목 수정
    @Override
    public GradeItemResponseDTO updateGradeItem(Long itemId, GradeItemUpdateDTO dto, String professorEmail) {
        log.info("2) 평가항목 수정 시작 - itemId-:{},교수-:{}", itemId, professorEmail);

        //조회
        GradeItem gradeItem = getGradeItemOrThrow(itemId);

        //검증
        CourseOffering offering= gradeItem.getCourseOffering();
        validator.validateProfessorOwnership(offering, professorEmail, "평가항목 수정");

        //이름 변경 시 중복 체크
        if (dto.getItemName() != null && !gradeItem.getItemName().equals(dto.getItemName())) {
            boolean exists = repository.existsByCourseOffering_OfferingIdAndItemName(
                    gradeItem.getCourseOffering().getOfferingId(),
                    dto.getItemName()
            );
            validator.validateDuplicate(exists, "평가 항목(" + dto.getItemName() + ")");
        }

        // (자동 검증) 비율 변경 시 100% 초과 여부 확인 (자기 자신은 제외하고 합산)
        if (dto.getWeightPercent() != null) {
            checkTotalWeightLimit(gradeItem.getCourseOffering().getOfferingId(), itemId, dto.getWeightPercent());
        }

        gradeItem.updateGradeItemInfo(
                dto.getItemName(),
                dto.getMaxScore(),
                dto.getWeightPercent()
        );

        return entityMapper.toGradeItemResponseDTO(repository.save(gradeItem));
    }

    //  비율 합계 검증 로직
    private void checkTotalWeightLimit(Long offeringId, Long currentItemId, Float newWeight) {
        List<GradeItem> items = repository.findByOfferingId(offeringId);

        double currentTotal = items.stream()
                // 수정 중일 때는 자기 자신의 기존 비율은 빼고 계산해야 함
                .filter(item -> !item.getItemId().equals(currentItemId))
                .mapToDouble(GradeItem::getWeightPercent)
                .sum();

        if (currentTotal + newWeight > 100.0) {
            throw new IllegalArgumentException(
                    String.format("비율 합계가 100%%를 초과합니다. (현재: %.1f%%, 추가 시: %.1f%%)",
                            currentTotal, currentTotal + newWeight));
        }
    }

    //GI-6) 평가항목 개수 조회( 외부 service에서 사용예정)
    @Override
    @Transactional(readOnly = true)
    public Long countOfferingGradeItems(Long offeringId) {
        return repository.countByCourseOffering_OfferingId(offeringId);
    }

    @Override
    @Transactional(readOnly = true)
    public GradeItem getGradeItemByOfferingAndType(Long offeringId, GradeItemType itemType) {
        return repository.findByCourseOffering_OfferingIdAndItemType(offeringId, itemType)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("이 강의에는 '%s' 평가 항목이 설정되지 않았습니다. 평가 기준을 먼저 등록해주세요.", itemType.name())
                ));
    }

    // =========================================================================
    //  함수
    // =========================================================================
    private GradeItem getGradeItemOrThrow(Long id) {
        return validator.getEntityOrThrow(repository, id, "평가 항목");
    }

    private CourseOffering getOfferingOrThrow(Long id) {
        return validator.getEntityOrThrow(offeringRepository, id, "강의");
    }
    
}
