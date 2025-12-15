package com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeTableCreateDTO {
    @NotNull(message = "id값을 필수입니다.")
    private Long sectionId; //분반 id

    @NotNull(message = "요일은 필수입니다.")
    private String dayOfWeek; // 요일

    @NotNull(message = "시작 시간은 필수입니다.")
    private LocalDateTime startTime; //시작시간

    @NotNull(message = "종료 시간은 필수입니다.")
    private LocalDateTime endTime; //종료시간

//    private String location; //강의실 ** 삭제예정 **
//
//    @NotNull(message = "id값을 필수입니다.")
//    private Long offeringId; //개설강의고유Id Long sectionId로 변경예정


}
