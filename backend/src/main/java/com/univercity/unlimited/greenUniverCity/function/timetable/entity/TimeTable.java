package com.univercity.unlimited.greenUniverCity.function.timetable.entity;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private Integer timetableId; //시간표 아이디

    @Column(name = "day_of_week", length = 10, nullable = false)
    private String dayOfWeek; //주간 테이블

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; //시작 시간

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 끝 시간
    
    @Column(name = "location", length = 50)
    private String location; // 돌림

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id")
    @ToString.Exclude
    private CourseOffering courseOffering;
    //OneToMany,ManyToOne ***To*** 형태는
    //앞에게 현재 테이블 뒤에게 조인칼럼

}