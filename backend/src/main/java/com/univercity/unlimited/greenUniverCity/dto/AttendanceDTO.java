package com.univercity.unlimited.greenUniverCity.dto;

import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class AttendanceDTO {
    private Integer attendance;

    private EnrollmentDTO enrollment;
    private LocalDateTime localDate;
    private String status;
}
