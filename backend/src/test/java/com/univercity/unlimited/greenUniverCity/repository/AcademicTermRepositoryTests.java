package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.dto.AcademicTermResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.entity.AcademicTerm;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.respository.AcademicTermRepository;
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
public class AcademicTermRepositoryTests {
    @Autowired
    private AcademicTermRepository repository;

    @Test
    @Tag("push")
    public void testInsertData() {
        // 데이터 세팅
        for(int i=0; i<10; i++) {
            AcademicTerm term = AcademicTerm.builder()
                    .year(2020 + i)
                    .semester(i%2+"학기")
                    .registrationStart(LocalDate.now())
                    .registrationEnd(LocalDate.now())
                    .isCurrent(((int)(Math.random() * 2) % 2) == 1)
                    .build();

            repository.save(term);
        }
    }

}
