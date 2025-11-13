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

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    private List<UserRole> userRoleList=new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<CourseOffering> offerings = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Grade> grades = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Notice> notices = new ArrayList<>();

    public void addRole(UserRole memberRole){userRoleList.add(memberRole);}
    public void addOffering(CourseOffering courseOffering){
        offerings.add(courseOffering);
    }
    public void addEnrollment(Enrollment enrollment) {enrollments.add(enrollment);}
    public void addComment(Comment comment){comments.add(comment);}
    public void addPost(Post post){posts.add(post);}
    public void addGrade(Grade grade){grades.add(grade);}
}

