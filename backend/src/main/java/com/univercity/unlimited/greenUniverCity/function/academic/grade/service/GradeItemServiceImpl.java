package com.univercity.unlimited.greenUniverCity.function.academic.grade.service;

import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeItemRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.service.CourseOfferingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GradeItemServiceImpl implements GradeItemService{

    private final GradeItemRepository repository;

    private final CourseOfferingService courseOfferingService;

    private final AcademicSecurityValidator validator;


    /**
     * GI-A) GradeItem 엔티티를 ResponseDTO로 변환
     */
    private GradeItemResponseDTO toResponseDTO(GradeItem gradeItem){
        CourseOffering offering=gradeItem.getCourseOffering();

        return GradeItemResponseDTO.builder()
                .itemId(gradeItem.getItemId())
                .offeringId(offering.getOfferingId())
                .itemName(gradeItem.getItemName())
                .itemType(gradeItem.getItemType())
                .maxScore(gradeItem.getMaxScore())
                .weightPercent(gradeItem.getWeightPercent())
                .build();
    }
    
    //GI-1) 평가항목 생성
    @Override
    public GradeItemResponseDTO createGradeItem(GradeItemCreateDTO dto, String professorEmail) {
        log.info("2) 평가항목 생성 시작 - offeringId-:{}, itemName-:{}, 교수-:{}",
                 dto.getOfferingId(), dto.getItemName(), professorEmail);

        CourseOffering offering =
                courseOfferingService.getCourseOfferingEntity(dto.getOfferingId());

        validator.validateProfessorOwnership(offering, professorEmail, "평가항목 생성");
        
        if(repository.existsByCourseOffering_OfferingIdAndItemName(
                dto.getOfferingId(), dto.getItemName())){
            throw new IllegalStateException("이미 같은 이름의 평가 항목이 존재합니다");
        }

        GradeItem gradeItem = GradeItem.builder()
                .courseOffering(offering)
                .itemName(dto.getItemName())
                .itemType(dto.getItemType())
                .maxScore(dto.getMaxScore())
                .weightPercent(dto.getWeightPercent())
                .build();

        GradeItem savedItem = repository.save(gradeItem);

        log.info("5) 평가 항목 생성 완료 - itemId-:{}, 교수-:{}",
                savedItem.getItemId(), professorEmail);

        return toResponseDTO(savedItem);
    }

    //GI-2) 평가항목 단건 조회
    @Override
    public GradeItemResponseDTO getGradeItem(Long itemId) {
        log.info("2) 평가항목 조회 시작 - itemId-:{}", itemId);

        GradeItem gradeItem=repository.findById(itemId)
                .orElseThrow(()->new IllegalArgumentException("평가항목을 찾을 수 없습니다"));

        return toResponseDTO(gradeItem);
    }

    //GI-3) 강의별 평가항목 목록 조회
    @Override
    public List<GradeItemResponseDTO> getOfferingGradeItem(Long offeringId) {
        log.info("2) 강의별 평가항목 조회 시작 - offeringId-:{}", offeringId);

        List<GradeItem> items=repository.findByOfferingId(offeringId);

        log.info("3) 강의별 평가 항목 조회 완료 - offeringId-:{}, 항목 개수-:{}",
                 offeringId, items.size());

        return items.stream()
                .map(this::toResponseDTO)
                .toList();
    }
    
    //GI-4) 평가항목 수정
    @Override
    public GradeItemResponseDTO updateGradeItem(Long itemId, GradeItemUpdateDTO dto, String professorEmail) {
        log.info("2) 평가항목 수정 시작 - itemId-:{},교수-:{}", itemId, professorEmail);

        GradeItem gradeItem = repository.findById(itemId)
                .orElseThrow(()->new IllegalArgumentException("3) 평가 항목을 찾을 수 없습니다"));
        
        CourseOffering offering= gradeItem.getCourseOffering();
        validator.validateProfessorOwnership(offering, professorEmail, "평가항목 수정");

        gradeItem.setItemName(dto.getItemName());
//        gradeItem.setItemType(dto.getItemType());
        gradeItem.setMaxScore(dto.getMaxScore());
        gradeItem.setWeightPercent(dto.getWeightPercent());

        GradeItem updateItem= repository.save(gradeItem);

        log.info("5) 평가항목 수정 완료 - itemId-:{}, 교수-:{}",updateItem.getItemId(), professorEmail);

        return toResponseDTO(updateItem);
    }

    //GI-5) 평가항목 비율 합계 검증
    @Override
    public boolean validateWeightSum(Long offeringId) {
        log.info("2) 평가항목 비율 합계 검증 - offeringId-:{}", offeringId);

        //모든 평가항목 조회
        List<GradeItem> items=repository.findByOfferingId(offeringId);

        float totalWeight = 0.0f;

        for(GradeItem item: items){
            totalWeight += item.getWeightPercent();
        }

        //100%인지 확인
        boolean isValid = Math.abs(totalWeight - 100.0f) < 0.01f;

        log.info("비율 합계-:{}%, 유효성-:{}", totalWeight, isValid);

        return isValid;
    }
    //GI-6) 평가항목 개수 조회( 외부 service에서 사용예정)
    @Override
    public Long countOfferingGradeItems(Long offeringId) {
        return repository.countByCourseOffering_OfferingId(offeringId);
    }

    //GI-7) GradeItem에 대한 정보 조회(외부 service에서 사용예정)
    @Override
    public GradeItem getGradeItemEntity(Long itemId) {
        return repository.findById(itemId)
                .orElseThrow(()->new IllegalArgumentException("평가 항목을 찾을 수 없습니다."));
    }
}
