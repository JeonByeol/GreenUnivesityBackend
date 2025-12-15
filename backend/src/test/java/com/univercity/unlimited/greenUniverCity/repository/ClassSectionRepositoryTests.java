package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.repository.ClassroomRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
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

    @Autowired
    private ClassroomRepository classroomRepository;

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

        // 2. 강의실(Classroom) 조회
        List<Classroom> classrooms = classroomRepository.findAll();
        if (classrooms.isEmpty()) {
            log.warn("Classroom 데이터가 없습니다. 먼저 강의실 데이터를 생성해주세요.");
            return;
        }

        String[] sectionNames = {"A반", "B반", "C반", "오전반", "오후반", "야간반"};
        Random random = new Random();

        // 3. 더미 데이터 20개 생성
        for (int i = 0; i < 20; i++) {
            // 랜덤으로 강의와 강의실 선택
            CourseOffering randomOffering = offerings.get(random.nextInt(offerings.size()));
            Classroom randomClassroom = classrooms.get(random.nextInt(classrooms.size()));

            ClassSection classSection = ClassSection.builder()
                    .sectionName(sectionNames[random.nextInt(sectionNames.length)])
                    .maxCapacity(randomClassroom.getCapacity()) // 정원은 강의실 수용인원에 맞춤
                    .courseOffering(randomOffering)
                    .classroom(randomClassroom)
                    .build();

            classSectionRepository.save(classSection);
        }

        log.info("========== 분반(ClassSection) 데이터 20개 생성 완료 ==========");
    }
}