package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Slf4j
public class AttendanceRepositoryTests {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    @Transactional
    @Commit
    public void testInsertData() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        Enrollment r=enrollments.get(0);
        for (int i = 0; i < 1; i++) {
            Attendance attendance = Attendance.builder()
                    .enrollment(r)
                    .localDate(LocalDate.now())
                    .status("출석")
                    .build();
            attendanceRepository.save(attendance);
        }
    }

}
