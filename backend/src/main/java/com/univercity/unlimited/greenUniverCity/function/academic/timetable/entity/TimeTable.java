package com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
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
@Table(name = "tbl_time_table")
public class TimeTable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Long timetableId; //시간표아이디

    @Column(name = "day_of_week", length = 10, nullable = false)
    private String dayOfWeek; //요일

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; //시작시간

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; //종료시간
    
    @Column(name = "location", length = 50) // ** classroom이라는 장소에 대한 테이블 생성으로 삭제 예정 **
    private String location; //장소

    @ManyToOne(fetch = FetchType.LAZY) // fk로 section을 받는게 맞음
    @JoinColumn(name="section_id")
    private ClassSection classSection;

//    @ManyToOne(fetch = FetchType.LAZY) // ** 관계가 timetable이 fk로 offering을 받는것에서 section을 받는것으로 변경 삭제 예정 **
//    @JoinColumn(name = "offering_id")
//    private CourseOffering courseOffering;


}