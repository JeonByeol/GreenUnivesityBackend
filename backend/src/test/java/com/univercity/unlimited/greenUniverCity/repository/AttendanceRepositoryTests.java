package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.Attendance;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
public class AttendanceRepositoryTests {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Test
    public void testInsertData() {
        for (int i = 0; i < 1; i++) {
            Attendance attendance = Attendance.builder()
                    .attendance(i)
                    .enrollment(null)
                    .localDate(LocalDate.now())
                    .status("출석")
                    .build();
            attendanceRepository.save(attendance);
        }
    }

}
