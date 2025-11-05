package com.univercity.unlimited.greenUniverCity.dto;

import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class AttendanceDTO {
    private Integer attendance;

    private Enrollment enrollment;
    private LocalDate localDate;
    private String status;
}
