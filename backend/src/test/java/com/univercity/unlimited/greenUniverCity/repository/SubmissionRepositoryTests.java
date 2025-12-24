package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Assignment;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Submission;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository.AssignmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository.SubmissionRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@SpringBootTest
@Transactional
@Tag("push")
public class SubmissionRepositoryTests {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("1. 과제 제출 더미 데이터 생성")
    @Commit
    public void insertSubmissionDummies() {
        // 1. 모든 과제 조회
        List<Assignment> assignments = assignmentRepository.findAll();
        // 2. 모든 학생 조회 (UserRole.STUDENT)
        List<User> students = userRepository.findAll().stream()
                .filter(u -> u.getUserRole() == UserRole.STUDENT)
                .toList();

        if (assignments.isEmpty() || students.isEmpty()) {
            System.out.println(" 과제 또는 학생 데이터가 부족합니다.");
            return;
        }

        Random random = new Random();

        // 3. 각 과제마다 랜덤한 학생 몇 명이 제출했다고 가정
        assignments.forEach(assignment -> {
            // 학생 3명 정도가 제출
            students.stream().limit(3).forEach(student -> {

                // 이미 제출했는지 확인 (중복 방지)
                if(submissionRepository.findByAssignmentIdAndStudentEmail(assignment.getAssignmentId(), student.getEmail()).isPresent()) {
                    return;
                }

                boolean isGraded = random.nextBoolean(); // 채점 여부 랜덤

                Submission submission = Submission.builder()
                        .assignment(assignment)
                        .student(student)
                        .fileUrl("https://s3.aws.com/uploads/" + "_homework.pdf")
                        .submittedAt(LocalDateTime.now().minusHours(random.nextInt(48))) // 최근 48시간 내 제출
                        .score(isGraded ? (float)(random.nextInt(30) + 70) : 0.0f) // 채점 됐으면 70~100점, 아니면 0점
                        .build();

                submissionRepository.save(submission);
            });
        });

        System.out.println(" 과제 제출 더미 데이터 생성 완료: " + submissionRepository.count() + "개");
    }
}