package com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomCreateDTO {

    @NotBlank(message = "강의실 위치(건물/호수)는 필수입니다")
    @Size(min = 1, max = 50, message = "강의실 위치는 1-50자 이내여야 합니다")
    private String location; //장소(A건물,B건물)

    @NotNull(message = "수용 인원은 필수입니다")
    @Min(value = 1, message = "수용 인원은 최소 1명 이상이어야 합니다")
    @Max(value = 500, message = "수용 인원은 최대 500명 이하여야 합니다")
    private Integer capacity; // 수용인원

}
