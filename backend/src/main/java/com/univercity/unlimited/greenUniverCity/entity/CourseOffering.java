package com.univercity.unlimited.greenUniverCity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "tbl_course")
@ToString
@Getter
public class CourseOffering {
    @Id
    @Column(name = "offering_id")
    private Long offeringId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "professor_id")
    private String professorId;

    private int year;
    private int semester; // ex) 1,2
}
