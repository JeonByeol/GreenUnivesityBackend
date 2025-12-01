package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Slf4j
public class AttendanceRepositoryTests {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AttendanceRepository repository;

    @Test
    @Tag("push")
    public void testInsertData() {
        // 데이터 세팅
        List<Enrollment> enrollments = enrollmentRepository.findAll();

        // 체크
        if(enrollments.isEmpty() == true)
        {
            log.info("Enrollment가 비어있습니다.");
            return;
        }

        for(Enrollment enrollment : enrollments) {
            int ran = (int)(Math.random()*2);
            String status = ran == 1 ? "출석" : "결석";

            Attendance attendance = Attendance.builder()
                    .attendanceDate(LocalDate.now())
                    .enrollment(enrollment)
                    .status(status)
                    .build();

            repository.save(attendance);
        }
    }

}
