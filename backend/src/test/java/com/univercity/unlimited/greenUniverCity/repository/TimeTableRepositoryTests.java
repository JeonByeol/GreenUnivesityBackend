package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.repository.ClassroomRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.repository.TimeTableRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@Slf4j
public class TimeTableRepositoryTests {
    @Autowired
    private TimeTableRepository repository;

    @Autowired
    private ClassSectionRepository classSectionRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Test
    @Tag("push")
    @Transactional
    @Rollback(false)
    public void testTimeTableData() {
        log.info("=== TimeTable 더미 데이터 생성 시작 ===");

        // 요일 배열 (DayOfWeek Enum)
        DayOfWeek[] daysOfWeek = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
        };

        //시간표 타임슬롯 (시작시간, 종료시간 쌍)
        LocalTime[][] timeSlots = {
                {LocalTime.of(9, 0), LocalTime.of(10, 30)},   // 1교시
                {LocalTime.of(10, 30), LocalTime.of(12, 0)},  // 2교시
                {LocalTime.of(13, 0), LocalTime.of(14, 30)},  // 3교시
                {LocalTime.of(14, 30), LocalTime.of(16, 0)},  // 4교시
                {LocalTime.of(16, 0), LocalTime.of(17, 30)},  // 5교시
                {LocalTime.of(18, 0), LocalTime.of(19, 30)}   // 야간 1교시
        };

        // 분반 데이터 조회
        List<ClassSection> classSections = classSectionRepository.findAll();
        if (classSections.isEmpty()) {
            log.warn("ClassSection 데이터가 없습니다. 더미 데이터 생성을 중단합니다.");
            return;
        }

        // 강의실 데이터 조회
        List<Classroom> classrooms = classroomRepository.findAll();
        if (classrooms.isEmpty()) {
            log.warn("Classroom 데이터가 없습니다. 더미 데이터 생성을 중단합니다.");
            return;
        }

        log.info("분반 개수: {}, 강의실 개수: {}", classSections.size(), classrooms.size());

        // 각 분반에 1~2개의 시간표 생성
        int createdCount = 0;
        for (ClassSection classSection : classSections) {
            // 랜덤으로 1~2개 시간표 생성 (주 1회 또는 주 2회 수업)
            int timeTableCount = (int) (Math.random() * 2) + 1; // 1 or 2

            for (int i = 0; i < timeTableCount; i++) {
                // 랜덤 요일 선택
                DayOfWeek randomDay = daysOfWeek[(int) (Math.random() * daysOfWeek.length)];

                // 랜덤 시간대 선택
                LocalTime[] randomTimeSlot = timeSlots[(int) (Math.random() * timeSlots.length)];
                LocalTime startTime = randomTimeSlot[0];
                LocalTime endTime = randomTimeSlot[1];

                // 랜덤 강의실 선택
                Classroom randomClassroom = classrooms.get((int) (Math.random() * classrooms.size()));

                // TimeTable 생성
                TimeTable timeTable = TimeTable.builder()
                        .classSection(classSection)
                        .dayOfWeek(randomDay)
                        .startTime(startTime)
                        .endTime(endTime)
                        .classroom(randomClassroom)
                        .build();

                try {
                    repository.save(timeTable);
                    createdCount++;

                    if (createdCount <= 5) { // 처음 5개만 로그 출력
                        log.info("생성됨: {} | {} {}~{} | {}",
                                classSection.getSectionName(),
                                randomDay.name(),
                                startTime,
                                endTime,
                                randomClassroom.getLocation());
                    }
                } catch (Exception e) {
                    log.error("TimeTable 생성 실패 - SectionId: {}, 에러: {}",
                            classSection.getSectionId(), e.getMessage());
                }
            }
        }

        log.info("=== TimeTable 더미 데이터 생성 완료 ===");
        log.info("총 {}개의 시간표가 생성되었습니다.", createdCount);
    }

    /**
     * 특정 분반에 "월/수" 형태의 주 2회 수업 시간표 생성 예시
     */
    @Test
    @Tag("manual")
    @Transactional
    public void testCreateTwiceWeeklyTimeTable() {
        log.info("=== 주 2회 수업 시간표 생성 테스트 ===");

        // 첫 번째 분반 조회
        ClassSection section = classSectionRepository.findAll().get(0);
        Classroom classroom = classroomRepository.findAll().get(0);

        // 월요일 10:30~12:00
        TimeTable monday = TimeTable.builder()
                .classSection(section)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(10, 30))
                .endTime(LocalTime.of(12, 0))
                .classroom(classroom)
                .build();

        // 수요일 10:30~12:00
        TimeTable wednesday = TimeTable.builder()
                .classSection(section)
                .dayOfWeek(DayOfWeek.WEDNESDAY)
                .startTime(LocalTime.of(10, 30))
                .endTime(LocalTime.of(12, 0))
                .classroom(classroom)
                .build();

        repository.save(monday);
        repository.save(wednesday);

        log.info("주 2회 시간표 생성 완료: {} | 월/수 10:30-12:00 | {}",
                section.getSectionName(),
                classroom.getLocation());
    }

//    /**
//     * 강의실 충돌 테스트 (같은 시간, 같은 강의실에 배정 시도)
//     */
//    @Test
//    @Tag("conflict-test")
//    @Transactional
//    public void testClassroomConflict() {
//        log.info("=== 강의실 충돌 테스트 ===");
//
//        List<ClassSection> sections = classSectionRepository.findAll();
//        Classroom classroom = classroomRepository.findAll().get(0);
//
//        // 첫 번째 시간표: 월요일 10:30~12:00
//        TimeTable timeTable1 = TimeTable.builder()
//                .classSection(sections.get(0))
//                .dayOfWeek(DayOfWeek.MONDAY)
//                .startTime(LocalTime.of(10, 30))
//                .endTime(LocalTime.of(12, 0))
//                .classroom(classroom)
//                .build();
//        repository.save(timeTable1);
//        log.info("첫 번째 시간표 저장 성공");
//
//        // 두 번째 시간표: 같은 시간, 같은 강의실 (충돌!)
//        TimeTable timeTable2 = TimeTable.builder()
//                .classSection(sections.get(1))
//                .dayOfWeek(DayOfWeek.MONDAY)
//                .startTime(LocalTime.of(11, 0)) // 10:30~12:00과 겹침
//                .endTime(LocalTime.of(12, 30))
//                .classroom(classroom)
//                .build();
//
//        // Service 계층에서는 충돌 검사로 예외 발생
//        // Repository 직접 호출 시에는 저장됨 (테스트용)
//        repository.save(timeTable2);
//        log.warn("충돌하는 시간표가 저장됨 (Service 계층에서는 차단됨)");
//
//        // 충돌 검사 쿼리 테스트
//        List<TimeTable> conflicts = repository.findConflictingTimeTables(
//                classroom.getClassroomId(),
//                DayOfWeek.MONDAY,
//                LocalTime.of(11, 0),
//                LocalTime.of(12, 30)
//        );
//
//        log.info("충돌하는 시간표 개수: {}", conflicts.size());
//        conflicts.forEach(t -> log.info("  - {} {}~{}",
//                t.getClassSection().getSectionName(),
//                t.getStartTime(),
//                t.getEndTime()));
//    }
}