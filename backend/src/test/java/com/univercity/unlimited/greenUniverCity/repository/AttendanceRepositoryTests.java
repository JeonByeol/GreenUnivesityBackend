package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.AttendanceStatus;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Slf4j
public class AttendanceRepositoryTests {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AttendanceRepository repository;

//    @Test
//    @Tag("push")
//    @Transactional
//    @Rollback(false) // 테스트 끝나고 DB에 데이터 남기기
//    public void testInsertData() {
//        log.info("========== 출결 더미 데이터 생성 시작 (2학기 기준, 50개 제한) ==========");
//
//        // 1. 전체 수강신청 내역 조회
//        List<Enrollment> enrollments = enrollmentRepository.findAll();
//
//        if(enrollments.isEmpty()) {
//            log.info("Enrollment 데이터가 없습니다. 수강신청 데이터를 먼저 생성해주세요.");
//            return;
//        }
//
//        // 2. 최대 50개까지만 생성하도록 제한
//        int targetCount = 50;
//        int currentCount = 0;
//
//        // 3. 2학기 시작일 설정 (올해 9월 1일)
//        int currentYear = LocalDate.now().getYear();
//        LocalDate semesterStart = LocalDate.of(currentYear, 9, 1);
//
//        for(Enrollment enrollment : enrollments) {
//            // 50개 만들었으면 중단
//            if (currentCount >= targetCount) break;
//
//            int ran = (int)(Math.random() * 4);
//            AttendanceStatus status;
//
//            // 랜덤 상태 배정
//            switch (ran) {
//                case 0: status = AttendanceStatus.PRESENT; break; // 출석
//                case 1: status = AttendanceStatus.LATE;    break; // 지각
//                case 2: status = AttendanceStatus.ABSENT;  break; // 결석
//                case 3: status = AttendanceStatus.EXCUSED; break; // 공결
//                default: status = AttendanceStatus.PRESENT;
//            }
//
//            // 🔥 [핵심] 날짜 생성 로직 (2학기: 9월 ~ 12월 사이 랜덤)
//            // 0일 ~ 100일 사이의 랜덤 날짜를 더함 (대략 9월 1일 ~ 12월 초중순)
//            int randomDays = (int)(Math.random() * 100);
//            LocalDate randomDate = semesterStart.plusDays(randomDays);
//
//            // 주차(Week) 계산 (단순하게 7일 단위로 나눔, 1주차부터 시작)
//            int week = (randomDays / 7) + 1;
//
//            Attendance attendance = Attendance.builder()
//                    .attendanceDate(randomDate) // 2학기 날짜 적용
//                    .enrollment(enrollment)
//                    .status(status)
//                    .week(week) // 날짜에 맞는 주차 자동 계산
//                    .build();
//
//            repository.save(attendance);
//            currentCount++;
//
//            log.debug("출결 생성 완료: {} - {} ({}주차)",
//                    enrollment.getUser().getNickname(), randomDate, week);
//        }
//
//        log.info("========== 총 {}개의 출결 데이터 생성 완료 ==========", currentCount);
//    }
//}

@Test
@Tag("push")
@Transactional
@Rollback(false)
public void testInsertFullSemesterData() {
    log.info("========== 2학기 전체(15주) 출결 더미 데이터 생성 시작 ==========");

    List<Enrollment> enrollments = enrollmentRepository.findAll();

    if (enrollments.isEmpty()) {
        log.warn("수강신청 데이터가 없습니다.");
        return;
    }

    // 🎯 타겟 학생 이메일 (이 학생만 결석 10번!)
    String targetEmail = "student@aaa.com";
    int targetAbsenceCount = 10;

    // 📅 2학기 설정 (9월 1일 개강 ~ 15주간)
    int currentYear = LocalDate.now().getYear();
    LocalDate semesterStart = LocalDate.of(currentYear, 9, 1);
    int totalWeeks = 15; // 한 학기는 보통 15주~16주

    int totalCount = 0;

    for (Enrollment enrollment : enrollments) {
        String studentEmail = enrollment.getUser().getEmail();
        boolean isTargetStudent = targetEmail.equals(studentEmail);

        // 1주차 ~ 15주차 데이터 생성
        for (int week = 1; week <= totalWeeks; week++) {

            // 해당 주차의 수업 날짜 계산 (매주 같은 요일이라고 가정하고 주차만 더함)
            // 실제 수업 요일을 따지려면 복잡하니, 대략 월~금 중 하루로 랜덤 설정
            LocalDate weekDate = semesterStart.plusWeeks(week - 1);
            // 주말이면 월요일로 보정 (선택사항)
            if (weekDate.getDayOfWeek() == DayOfWeek.SATURDAY || weekDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekDate = weekDate.plusDays(2);
            }

            AttendanceStatus status;

            // 🔥 [핵심 로직] 타겟 학생은 10번 결석시키기
            if (isTargetStudent) {
                if (week <= targetAbsenceCount) {
                    status = AttendanceStatus.ABSENT; // 1~10주차 결석
                } else {
                    status = AttendanceStatus.PRESENT; // 나머지는 출석
                }
            }
            // 🟢 다른 학생들은 모범생 (대부분 출석)
            else {
                int ran = (int) (Math.random() * 100);
                if (ran < 90) status = AttendanceStatus.PRESENT; // 90% 확률로 출석
                else if (ran < 95) status = AttendanceStatus.LATE; // 5% 지각
                else status = AttendanceStatus.ABSENT; // 5% 결석
            }

            Attendance attendance = Attendance.builder()
                    .attendanceDate(weekDate)
                    .enrollment(enrollment)
                    .status(status)
                    .week(week)
                    .build();

            repository.save(attendance);
            totalCount++;
        }

        if (isTargetStudent) {
            log.info("🎯 타겟 학생({})에게 결석 {}회 폭탄 투하 완료!", studentEmail, targetAbsenceCount);
        }
    }

    log.info("========== 총 {}개의 출결 데이터 생성 완료 (학생당 15주차) ==========", totalCount);
}
}