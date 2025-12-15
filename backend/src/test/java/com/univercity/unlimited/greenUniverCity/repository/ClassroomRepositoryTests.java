package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.repository.ClassroomRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Random;

@SpringBootTest
@Slf4j
public class ClassroomRepositoryTests {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Test
    @Tag("push")
    @Transactional
    @Rollback(false) // DB에 데이터 저장 (롤백 안 함)
    public void testInsertClassrooms() {
        log.info("========== 강의실(Classroom) 더미 데이터 생성 시작 ==========");

        String[] buildings = {"공학관", "인문관", "자연과학관", "경영관", "법학관", "IT융합관", "예술관"};
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            // 랜덤 건물명 + 호수 생성 (예: 공학관 101호)
            String building = buildings[random.nextInt(buildings.length)];
            int floor = random.nextInt(5) + 1; // 1~5층
            int roomNum = random.nextInt(20) + 1; // 1~20호
            String location = String.format("%s %d%02d호", building, floor, roomNum);

            // 수용 인원 (30 ~ 100명)
            int capacity = (random.nextInt(8) + 3) * 10;

            Classroom classroom = Classroom.builder()
                    .location(location)
                    .capacity(capacity)
                    .build();

            classroomRepository.save(classroom);
        }

        log.info("========== 강의실(Classroom) 데이터 20개 생성 완료 ==========");
    }
}
