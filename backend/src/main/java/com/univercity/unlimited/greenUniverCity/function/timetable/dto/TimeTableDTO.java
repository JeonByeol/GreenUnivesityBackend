package com.univercity.unlimited.greenUniverCity.function.timetable.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.univercity.unlimited.greenUniverCity.function.offering.dto.CourseOfferingDTO;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class TimeTableDTO {
    private Integer timetableId;

    private String dayOfWeek;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

    @JsonBackReference("offering-timetable")
    @ToString.Exclude
    private CourseOfferingDTO courseOffering;

}
