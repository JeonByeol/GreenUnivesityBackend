package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.repository.ClassroomRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.SectionType;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Random;

@SpringBootTest
@Slf4j
public class ClassSectionRepositoryTests {

    @Autowired
    private ClassSectionRepository classSectionRepository;

    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    @Test
    @Tag("push")
    @Transactional
    @Rollback(false)
    public void testInsertClassSections() {
        log.info("========== 분반(ClassSection) 더미 데이터 생성 시작 ==========");

        // 1. 개설 강의(CourseOffering) 조회
        List<CourseOffering> offerings = courseOfferingRepository.findAll();
        if (offerings.isEmpty()) {
            log.warn("CourseOffering 데이터가 없습니다. 먼저 강의 데이터를 생성해주세요.");
            return;
        }

        log.info("조회된 CourseOffering 개수: {}", offerings.size());

        // 2. 분반명 배열
        String[] sectionNames = {"A반", "B반", "C반", "D반", "오전반", "오후반", "야간반", "주간반"};

        // 3. 정원 옵션
        Integer[] capacities = {30, 40, 50, 60, 80, 90, 100};

        // 4. SectionType 배열
        SectionType[] sectionTypes = SectionType.values(); // ACTIVE, INACTIVE 등

        Random random = new Random();
        int createdCount = 0;

        // 5. 각 개설 강의마다 1~3개의 분반 생성
        for (CourseOffering offering : offerings) {
            // 랜덤으로 1~3개 분반 생성
            int sectionCount = random.nextInt(3) + 1; // 1, 2, 또는 3

            for (int i = 0; i < sectionCount; i++) {
                ClassSection classSection = ClassSection.builder()
                        .sectionName(sectionNames[random.nextInt(sectionNames.length)])
                        .maxCapacity(capacities[random.nextInt(capacities.length)])
                        .sectionType(sectionTypes[random.nextInt(sectionTypes.length)])
                        .courseOffering(offering)
                        .build();

                try {
                    classSectionRepository.save(classSection);
                    createdCount++;

                    // 처음 5개만 로그 출력
                    if (createdCount <= 5) {
                        log.info("생성됨: {} | {} | 정원: {} | 타입: {}",
                                offering.getCourseName(),
                                classSection.getSectionName(),
                                classSection.getMaxCapacity(),
                                classSection.getSectionType());
                    }
                } catch (Exception e) {
                    log.error("ClassSection 생성 실패 - OfferingId: {}, 에러: {}",
                            offering.getOfferingId(), e.getMessage());
                }
            }
        }

        log.info("========== 분반(ClassSection) 데이터 생성 완료 ==========");
        log.info("총 {}개의 분반이 생성되었습니다.", createdCount);
    }
}