package com.univercity.unlimited.greenUniverCity.function.timetable.repository;

import com.univercity.unlimited.greenUniverCity.function.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<TimeTable,Integer> {
}
