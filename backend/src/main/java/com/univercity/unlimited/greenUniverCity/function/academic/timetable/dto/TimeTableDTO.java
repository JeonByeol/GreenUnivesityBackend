//package com.univercity.unlimited.greenUniverCity.function.timetable.dto;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
//@Getter
//@Setter
//@Builder
//public class TimeTableDTO {
//    ** Legacy **
//    private Integer timetableId; //시간표아이디
//
//    private String dayOfWeek; // 요일
//
//    private LocalDateTime startTime; //시작시간
//
//    private LocalDateTime endTime; //종료시간
//
//    private String location; //강의실
//
//    @JsonBackReference("offering-timetable")
//    @ToString.Exclude
//    private CourseOfferingDTO courseOffering; //강의 개설
//
//}
