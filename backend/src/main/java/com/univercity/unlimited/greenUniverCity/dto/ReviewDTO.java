package com.univercity.unlimited.greenUniverCity.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class ReviewDTO {
    private Integer reviewId;

    private Integer rating;

    private String comment;

    private LocalDateTime createAt;

    @JsonBackReference("enrollment-review")
    @ToString.Exclude
    private EnrollmentDTO enrollmentDTO;
}
