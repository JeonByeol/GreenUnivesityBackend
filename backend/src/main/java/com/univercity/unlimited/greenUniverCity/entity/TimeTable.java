package com.univercity.unlimited.greenUniverCity.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString

@Table(
        name = "tbl_timetable", indexes = {
        @Index(columnList = "offering_id", name = "idx_timetable_offering")
}
)
public class TimeTable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Integer timetableId;

    @Column(name = "day_of_week", length = 10, nullable = false)
    private String dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @Column(name = "location", length = 50)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id")
    @ToString.Exclude
    private CourseOffering courseOffering;
    //OneToMany,ManyToOne ***To*** 형태는
    //앞에게 현재 테이블 뒤에게 조인칼럼

}