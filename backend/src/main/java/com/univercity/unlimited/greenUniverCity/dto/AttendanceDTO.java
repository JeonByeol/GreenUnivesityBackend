package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    private LocalDateTime localDateTime;

    private String status;

    @JsonBackReference("enrollment-attendance")
    private EnrollmentDTO enrollment;
}
