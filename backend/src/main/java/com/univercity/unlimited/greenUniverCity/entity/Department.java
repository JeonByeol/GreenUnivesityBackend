package com.univercity.unlimited.greenUniverCity.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "tbl_department")
@ToString
@Getter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "dept_name")
    private String deptName;
}
