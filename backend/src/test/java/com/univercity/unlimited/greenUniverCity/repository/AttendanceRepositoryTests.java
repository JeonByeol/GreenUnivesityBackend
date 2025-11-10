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
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AttendanceRepository repository;

    @Test
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
                    .localDate(LocalDate.now())
                    .enrollment(enrollment)
                    .status(status)
                    .build();

            repository.save(attendance);
        }
    }

}
