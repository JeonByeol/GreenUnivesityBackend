package com.univercity.unlimited.greenUniverCity.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_department")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId; // 학과 ID

    @Column(name = "dept_name")
    private String deptName; // 학과명
}
