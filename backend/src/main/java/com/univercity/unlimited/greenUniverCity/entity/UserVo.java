package com.univercity.unlimited.greenUniverCity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity //Jpa가 관리하고 db와 테이블이 매핑함
@AllArgsConstructor //
@NoArgsConstructor //파라미터가 없는 기본 생성자 생성
@Getter
@Table(name = "tbl_user")
@ToString(exclude = {"userRoleList","offerings","enrollments"})// 객체 정보를 문자열로 출력 할 수 있는 롬복
@Builder
public class UserVo {
    @Id //아이디 이메일 비밀번호 닉네임 칼럼 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String email;
    private String password;
    private String nickname;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<UserRole> userRoleList=new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<CourseOffering> offerings = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    public void addRole(UserRole memberRole){userRoleList.add(memberRole);}
    public void addOffering(CourseOffering courseOffering){
        offerings.add(courseOffering);
    }
}

