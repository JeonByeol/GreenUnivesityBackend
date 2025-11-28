package com.univercity.unlimited.greenUniverCity.function.user.entity;
import com.univercity.unlimited.greenUniverCity.function.comment.entity.Comment;
import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.notice.entity.Notice;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.post.entity.Post;
import jakarta.persistence.*;
        import lombok.*;

        import java.util.ArrayList;
import java.util.List;

@Entity //Jpa가 관리하고 db와 테이블이 매핑함
@AllArgsConstructor //
@NoArgsConstructor //파라미터가 없는 기본 생성자 생성
@Getter
//@Table(name = "tbl_user")
@ToString
@Builder
public class UserDummy{

    @Id //아이디 이메일 비밀번호 닉네임 칼럼 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true)
    private String email;
    private String password;
    private String nickname;

    //  List에서 단일 필드로 변경!
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // ===== 비즈니스 메서드 =====

    // 역할 변경 메서드 (setter 대신)
    public void changeRole(UserRole newRole) {
        this.role = newRole;
    }

    // 비밀번호 변경 메서드
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // 닉네임 변경 메서드
    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }

    // 특정 역할인지 확인
    public boolean hasRole(UserRole targetRole) {
        return this.role == targetRole;
    }

    //  교수 권한 체크
    public boolean isProfessor() {
        return this.role == UserRole.PROFESSOR;
    }

    // 학생 권한 체크
    public boolean isStudent() {
        return this.role == UserRole.STUDENT;
    }

    // 관리자 권한 체크
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }
}

