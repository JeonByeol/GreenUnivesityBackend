package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.AttendanceStatus;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.Grade;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItemType;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.StudentScore;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeItemRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.StudentScoreRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest
@Slf4j
public class GradeRepositoryTests {
    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private GradeItemRepository gradeItemRepository;

    @Autowired
    private StudentScoreRepository studentScoreRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    /**
     * 테스트 데이터 생성 시나리오:
     * 1. 각 강의(CourseOffering)에 평가항목(GradeItem) 생성
     * 2. 각 수강신청(Enrollment)에 대해 점수(StudentScore) 생성
     * 3. 점수를 기반으로 최종 성적(Grade) 자동 계산 및 생성
     */
    @Test
    @Tag("push")
    @Transactional
    @Rollback(false)
    public void testCreateGradeData() {
        log.info("========== 성적 테스트 데이터 생성 시작 ==========");

        // 1단계: 평가항목(GradeItem) 생성
        createGradeItems();

        // 2단계: 학생 점수(StudentScore) 생성
        createStudentScores();

        // 3단계: 최종 성적(Grade) 계산 및 생성
        createFinalGrades();

        log.info("========== 성적 테스트 데이터 생성 완료 ==========");
    }


    /**
     *  1단계: 각 강의에 평가항목 생성
     * - 중간고사(30%): 100점 만점
     * - 기말고사(30%): 100점 만점
     * - 과제(20%): 50점 만점 (시험보다 낮은 배점)
     * - 출결(20%): 20점 만점 (1주당 1점 + 알파 or 비율 그대로 점수화)
     */
    private void createGradeItems() {
        log.info("1) 평가항목 생성 시작");

        List<CourseOffering> offerings = courseOfferingRepository.findAll();

        if (offerings.isEmpty()) {
            log.warn("강의가 없습니다. CourseOffering 데이터를 먼저 생성해주세요.");
            return;
        }

        int createdCount = 0;

        for (CourseOffering offering : offerings) {
            // 이미 평가항목이 있는지 확인 (중복 생성 방지)
            List<GradeItem> existingItems = gradeItemRepository.findByOfferingId(offering.getOfferingId());

            if (!existingItems.isEmpty()) {
                log.info("강의 [{}]는 이미 평가항목이 존재합니다. 건너뜁니다.", offering.getCourseName());
                continue;
            }

            // 1. 중간고사 (30%) - 시험은 보통 100점 만점
            GradeItem midterm = GradeItem.builder()
                    .courseOffering(offering)
                    .itemName("중간고사")
                    .itemType(GradeItemType.MIDTERM)
                    .maxScore(100.0f)
                    .weightPercent(30.0f)
                    .build();
            gradeItemRepository.save(midterm);

            // 2. 기말고사 (30%) - 시험은 보통 100점 만점
            GradeItem finalExam = GradeItem.builder()
                    .courseOffering(offering)
                    .itemName("기말고사")
                    .itemType(GradeItemType.FINAL)
                    .maxScore(100.0f)
                    .weightPercent(30.0f)
                    .build();
            gradeItemRepository.save(finalExam);

            // 3. 과제 (20%) - 과제는 시험보다 작은 50점 만점으로 설정
            GradeItem assignment = GradeItem.builder()
                    .courseOffering(offering)
                    .itemName("개별 과제")
                    .itemType(GradeItemType.ASSIGNMENT)
                    .maxScore(50.0f)
                    .weightPercent(20.0f)
                    .build();
            gradeItemRepository.save(assignment);

            // 4. 출결 (20%) - 출결은 주차별 점수 등을 고려해 20점 만점 (비율과 동일하게)
            GradeItem attendance = GradeItem.builder()
                    .courseOffering(offering)
                    .itemName("출결")
                    .itemType(GradeItemType.ATTENDANCE)
                    .maxScore(20.0f)
                    .weightPercent(20.0f)
                    .build();
            gradeItemRepository.save(attendance);

            createdCount++;
            log.info("강의 [{}]에 평가항목 4개(중간/기말/과제/출결) 생성 완료", offering.getCourseName());
        }

        log.info("평가항목 생성 완료 - 총 {}개 강의에 생성", createdCount);
    }

    /**
     * 2단계: 각 수강신청에 대해 점수 생성
     * - 랜덤 점수 부여 (60~100점)
     */
    private void createStudentScores() {
        log.info("2) 학생 점수 생성 시작");

        List<Enrollment> enrollments = enrollmentRepository.findAll();

        if (enrollments.isEmpty()) {
            log.warn("수강신청이 없습니다. Enrollment 데이터를 먼저 생성해주세요.");
            return;
        }

        int createdCount = 0;

        for (Enrollment enrollment : enrollments) {
            // 해당 강의의 평가항목 조회
            List<GradeItem> gradeItems = gradeItemRepository
                    .findByOfferingId(enrollment.getClassSection().getCourseOffering().getOfferingId());

            if (gradeItems.isEmpty()) continue;

            // 이미 점수가 있는지 확인 (중복 생성 방지)
            List<StudentScore> existingScores = studentScoreRepository
                    .findByEnrollmentId(enrollment.getEnrollmentId());

            if (!existingScores.isEmpty()) continue;

            // 각 평가항목에 대해 점수 생성
            for (GradeItem gradeItem : gradeItems) {
                float scoreObtained = 0.0f;

                if (gradeItem.getItemType() == GradeItemType.ATTENDANCE) {

                    // 1. 해당 학생의 출결 기록 가져오기
                    List<Attendance> attendances = attendanceRepository
                            .findByEnrollmentId(enrollment.getEnrollmentId());

                    // 2. 감점 계산 (예: 결석 1회당 1점, 지각 3회당 1점 감점)
                    int absenceCount = 0;
                    int lateCount = 0;

                    for (Attendance att : attendances) {
                        if (att.getStatus() == AttendanceStatus.ABSENT) {
                            absenceCount++;
                        } else if (att.getStatus() == AttendanceStatus.LATE) {
                            lateCount++;
                        }
                    }

                    // 3. 점수 산출 (MaxScore에서 감점, 최소 0점)
                    // (지각 3번 = 결석 1번 취급 로직 예시)
                    float deduction = (absenceCount * 1.0f) + (lateCount / 3.0f);
                    float calculatedScore = gradeItem.getMaxScore() - deduction;

                    scoreObtained = Math.max(0.0f, calculatedScore); // 0점 미만 방지

                }
                // 🔥 [CASE 2] 과제, 시험 등 나머지 -> 랜덤 생성
                else {
                    float maxScore = gradeItem.getMaxScore();
                    // 60% ~ 100% 사이 랜덤 비율
                    float randomRatio = 0.6f + (float)(Math.random() * 0.4f);
                    float rawScore = maxScore * randomRatio;

                    // 소수점 첫째자리 반올림
                    scoreObtained = Math.round(rawScore * 10.0f) / 10.0f;
                }

                // 점수 저장
                StudentScore studentScore = StudentScore.builder()
                        .enrollment(enrollment)
                        .gradeItem(gradeItem)
                        .scoreObtained(scoreObtained)
                        .build();

                studentScoreRepository.save(studentScore);
            }
            createdCount++;
        }

        log.info("학생 점수 생성 완료 - 총 {}명의 학생", createdCount);
    }

    /**
     * 3단계: 점수를 기반으로 최종 성적 자동 계산 및 생성
     */
    private void createFinalGrades() {
        log.info("3) 최종 성적 계산 및 생성 시작");

        List<Enrollment> enrollments = enrollmentRepository.findAll();

        if (enrollments.isEmpty()) {
            log.warn("수강신청이 없습니다.");
            return;
        }

        int createdCount = 0;

        for (Enrollment enrollment : enrollments) {
            // 이미 성적이 있는지 확인
            if (gradeRepository.existsByEnrollment_EnrollmentId(enrollment.getEnrollmentId())) {
                log.info("수강신청 ID [{}]는 이미 최종 성적이 존재합니다. 건너뜁니다.",
                        enrollment.getEnrollmentId());
                continue;
            }

            // 해당 학생의 모든 점수 조회
            List<StudentScore> scores = studentScoreRepository
                    .findByEnrollmentId(enrollment.getEnrollmentId());

            if (scores.isEmpty()) {
                log.warn("수강신청 ID [{}]의 점수가 없습니다. 건너뜁니다.",
                        enrollment.getEnrollmentId());
                continue;
            }

            // 가중 평균 계산
            float totalScore = calculateWeightedAverage(scores);

            // 등급 계산
            String letterGrade = Grade.calculateGrade(totalScore);

            // Grade 생성
            Grade grade = Grade.builder()
                    .enrollment(enrollment)
                    .totalScore(totalScore)
                    .letterGrade(letterGrade)
                    .build();

            gradeRepository.save(grade);

            createdCount++;
            log.info("최종 성적 생성 - 학생: {}, 총점: {}, 등급: {}",
                    enrollment.getUser().getNickname(),
                    String.format("%.2f", totalScore),
                    letterGrade);
        }

        log.info("최종 성적 생성 완료 - 총 {}명의 학생", createdCount);
    }


    /**
     * 가중 평균 계산 헬퍼 메서드
     */
    private float calculateWeightedAverage(List<StudentScore> scores) {
        float totalWeightedScore = 0.0f;
        float totalWeight = 0.0f;

        for (StudentScore score : scores) {
            GradeItem item = score.getGradeItem();

            // (획득점수 / 만점) * 100 = 백분율 점수
            float percentageScore = (score.getScoreObtained() / item.getMaxScore()) * 100;

            // 백분율 점수 * (가중치 / 100) = 가중 점수
            float weightedScore = percentageScore * (item.getWeightPercent() / 100);

            totalWeightedScore += weightedScore;
            totalWeight += item.getWeightPercent();
        }

        // 가중치 합이 100이 아닐 경우 비율 조정
        if (totalWeight > 0 && totalWeight != 100) {
            totalWeightedScore = (totalWeightedScore / totalWeight) * 100;
        }

        return totalWeightedScore;
    }

}
