package com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeTableUpdateDTO {
    @NotNull(message = "id는 필수입니다.")
    private Integer timetableId; // 타임테이블 id

    @NotNull(message = "요일은 필수입니다.")
    private String dayOfWeek; // 요일

    @NotNull(message = "시작시간은 필수입니다.")
    private LocalDateTime startTime; //시작시간
    
    @NotNull(message = "종료시간은 필수입니다.")
    private LocalDateTime endTime; //종료시간

    private String location; //강의실 **삭제예정**


}
