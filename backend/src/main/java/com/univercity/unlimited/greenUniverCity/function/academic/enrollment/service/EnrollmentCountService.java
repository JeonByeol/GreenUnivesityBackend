package com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository.SectionCountSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnrollmentCountService {
    private final EnrollmentRepository enrollmentRepository;

    // 수강 인원 조회 로직 담당
    public Integer getCurrentEnrollmentCount(Long sectionId){
        log.info("2) 분반 수강 인원 조회 시작 sectionId-:{}",sectionId);

        Integer count= enrollmentRepository.countByClassSection_SectionId(sectionId);

        if(count==null){
            log.info("2-1)조회 결과 없으면 null대신 0으로 대입(sectionId-:{})", sectionId);
            count= 0;
        }

        log.info("3) 분반 수강 인원 조회 완료 sectionId-:{}, count-:{}", sectionId, count);

        return count;
    }

    // 여러 분반의 현재 수강 인원을 한번에 조회
    public Map<Long,Integer> getCurrentEnrollmentCounts(List<Long> sectionIds){
        log.info("2) 여러분반 수강 인원 조회 시작 sectionId-:{}",sectionIds);

        if(sectionIds == null || sectionIds.isEmpty()){
            return Collections.emptyMap();
        }

        List<SectionCountSummary> summaries= enrollmentRepository.countBySectionIds(sectionIds);

        Map<Long, Integer> result= toCountMap(summaries);

        log.info("3) 여러 분반 수강 인원 조회완료-:{}건", result.size());

        return result;
    }

    //SectionCountSummary 리스트를 맵으로 변환
    private Map<Long, Integer> toCountMap(List<SectionCountSummary> summaries){
        return summaries.stream()
                .collect(Collectors.toMap(
                        SectionCountSummary::getSectionId, //Key:분반 id
                        summary -> summary.getCount().intValue() //Value:Long->Integer 변환
                ));
    }
}
