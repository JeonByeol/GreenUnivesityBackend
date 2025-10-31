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
@ToString(exclude = "RoleList") // 객체 정보를 문자열로 출력 할 수 있는 롬복
@Builder
public class UserVo {
    @Id //아이디 이메일 비밀번호 닉네임 칼럼 생성
    private String id;
    private String email;
    private String password;
    private String nickname;

//    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default private List<Role> RoleList=new ArrayList<>();


}

