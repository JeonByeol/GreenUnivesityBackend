package com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomResponseDTO{
    private Long classroomId; // 강의실 ID
    private String location; // 건물/호수 (예: 공학관 301호)
    private Integer capacity; // 수용 인원

}
