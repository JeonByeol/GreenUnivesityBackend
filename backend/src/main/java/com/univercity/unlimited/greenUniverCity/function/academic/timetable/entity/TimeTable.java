package com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(
        name = "tbl_timetable", indexes = {
        @Index(columnList = "offering_id", name = "idx_timetable_offering")
} //시간표 테이블
)
public class TimeTable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Integer timetableId; //시간표아이디

    @Column(name = "day_of_week", length = 10, nullable = false)
    private String dayOfWeek; //요일

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; //시작시간

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; //종료시간
    
    @Column(name = "location", length = 50)
    private String location; //장소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id")
    private CourseOffering courseOffering;
}