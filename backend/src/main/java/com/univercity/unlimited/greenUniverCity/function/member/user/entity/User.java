package com.univercity.unlimited.greenUniverCity.function.member.user.entity;

import com.univercity.unlimited.greenUniverCity.function.member.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity //Jpa가 관리하고 db와 테이블이 매핑함
@AllArgsConstructor //
@NoArgsConstructor //파라미터가 없는 기본 생성자 생성
@Getter
@Table(name = "tbl_user") //사용자 테이블
@ToString
@Builder
@Setter
public class User {

    @Id //아이디 이메일 비밀번호 닉네임 칼럼 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true)
    private String email;
    private String password;
    private String nickname;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "student_number")
    private String studentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
