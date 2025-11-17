package com.univercity.unlimited.greenUniverCity.function.attendance.repository;

import com.univercity.unlimited.greenUniverCity.function.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository <Attendance,Integer>{
    //enrollment의 타입
}
