package com.univercity.unlimited.greenUniverCity.dto;

import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.entity.Grade;
import com.univercity.unlimited.greenUniverCity.entity.Review;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentDTO {


    private Long enrollmentId; // 과목 코드

    private LocalDateTime enrollDate;
    private CourseOfferingDTO courseOffering;
    private UserDTO user;
    private GradeDTO grade;
    private List<ReviewDTO> reviews = new ArrayList<>();

}
