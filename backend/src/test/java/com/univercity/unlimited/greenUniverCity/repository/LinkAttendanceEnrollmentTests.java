package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.util.List;

//레포지토리테스트:보내거나 받는 양방향
//attendance는 출석부페이지에서 작동하는거

//받아야 할것
//1.모든 강의(과목)들에 관한 정보
//2.특정 강의에 관한 정보
//3.개개인 학생에 관한 정보
//4.각 강의의 학생들의 출석여부에 관한 정보
//5.각 강의의 특정학생의 출석여부에 관한 정보
//6.교수 개개인에 관한 정보
//7.각 강의를 맡고 잇는 교수에 관한 정보

//데이터끼리 연결되어야 하는것
//1.특정 학생과 특정 강의가 연결된 정보
//2.특정강의와 특정 학생의 출결이 연결된 정보
//3.특정강의와 특정교수가 연결된 정보

//보내야 할 것
//1.특정강의,특정학생,출결여부가 연결된 정보

//프론트에 보여져야 할것
//학생이 수강하는 모든강의가 보일 것/거기서  각 강의의 출석일수,결석일수의 전체합계와 출석해야하는 전체일수가 보일것(ex)출석일수/전체일수)
//각 강의의 오늘의 출결여부 확인(모든 날의 개별 출석여부는 학생에게 제공하지 않음)
//특정 강의의 달별(해당강의 시작일부터 달별로 조회가능) 출결여부 확인(교수용)
//모든 강의에 관한 정보가져오기

@SpringBootTest
@Slf4j
public class LinkAttendanceEnrollmentTests {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    @Transactional
    @Commit
    public void testInsertData() {
        // 데이터 세팅
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        List<Attendance> attendances = attendanceRepository.findAll();

        // 체크
        if(enrollments.isEmpty() == true)
        {
            log.info("Enrollment가 비어있습니다.");
            return;
        }

        if(attendances.isEmpty() == true)
        {
            log.info("Attendance가 비어있습니다.");
            return;
        }
        
        // 추가
        for(Enrollment enrollment : enrollments) {
            int ran = (int)(Math.random()*2);
            String status = ran == 1 ? "출석" : "결석";

            int idx = enrollments.indexOf(enrollment);

            if(idx < 0) {
                log.info("유효한 강의가 아닙니다.");
                return;
            }

            Attendance attendance = attendances.get(idx);
            attendance.setEnrollment(enrollment);
            attendance.setStatus(status);

            attendanceRepository.save(attendance);
        }
    }

}
