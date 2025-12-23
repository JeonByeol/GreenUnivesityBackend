package com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(name = "tbl_time_table")
public class TimeTable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Long timetableId; //시간표아이디

    // String → DayOfWeek Enum으로 변경
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek; // MONDAY, TUESDAY, ...

    // LocalDateTime → LocalTime으로 변경
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime; // 09:00

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime; // 11:00

    @ManyToOne(fetch = FetchType.LAZY) // fk로 section을 받는게 맞음
    @JoinColumn(name="section_id")
    @ToString.Exclude
    private ClassSection classSection;

    //강의실은 시간표에 속함 (TimeTable N : Classroom 1)
    // 이유: 월요일은 101호, 수요일은 202호일 수 있음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    @ToString.Exclude
    private Classroom classroom;

    public void updateTimeTableInfo(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Classroom classroom) {
        if (dayOfWeek != null) this.dayOfWeek = dayOfWeek;
        if (startTime != null) this.startTime = startTime;
        if (endTime != null) this.endTime = endTime;
        if (classroom != null) this.classroom = classroom;
    }
}