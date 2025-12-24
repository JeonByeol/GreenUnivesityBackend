package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Assignment;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository.AssignmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
@Tag("push")
public class AssignmentRepositoryTests {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ClassSectionRepository classSectionRepository;

    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    @Test
    @DisplayName("1. 과제 더미 데이터 생성 (각 분반별 3개씩)")
    @Commit
    public void insertAssignmentDummies() {
        // 1. 존재하는 모든 분반 조회
        List<ClassSection> sections = classSectionRepository.findAll();

        if (sections.isEmpty()) {
            System.out.println(" 분반 데이터가 없습니다. ClassSectionTests를 먼저 실행해주세요.");
            return;
        }

        // 2. 각 분반마다 과제 3개씩 생성
        sections.forEach(section -> {
            IntStream.rangeClosed(1, 3).forEach(i -> {
                Assignment assignment = Assignment.builder()
                        .classSection(section)
                        .title(section.getSectionName() + " - " + i + "주차 과제")
                        .description("이 과제는 " + i + "주차 수업 내용에 대한 복습 과제입니다.\n기한 내에 제출해주세요.")
                        .maxScore(100.0f)
                        .dueDate(LocalDateTime.now().plusDays(7 * i)) // 1주, 2주, 3주 뒤 마감
                        .build();

                assignmentRepository.save(assignment);
            });
        });

        System.out.println("과제 더미 데이터 생성 완료: " + assignmentRepository.count() + "개");
    }
}