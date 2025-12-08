package com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class TimeTableResponseDTO {
    //TimeTable 필드에 담긴 정보
    private Integer timetableId; //시간표 아이디

    private String dayOfWeek; //요일

    private LocalDateTime startTime; //시작시간

    private LocalDateTime endTime; // 종료시간

    private String location; // 장소 ** 삭제예정 **

    //CourseOffering 필드에 담긴 정보 ** 삭제예정 **
    private String courseName; //강의명

    private String professorNickname; //교수이름

    private Long offeringId; //개설강의고유 id

    //ClassSection 필드에 담긴 정보
    private String sectionName; //분반명

    private Long sectionId;// 분반 id
    
}
