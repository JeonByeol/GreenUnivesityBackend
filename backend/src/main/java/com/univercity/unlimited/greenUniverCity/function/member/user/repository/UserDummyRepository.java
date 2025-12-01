package com.univercity.unlimited.greenUniverCity.function.member.user.repository;

import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserDummy;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserDummyRepository extends JpaRepository<UserDummy,Long> {
    //이메일로 사용자 조회
    Optional<UserDummy> findByEmail(String email);

    //특정 역할을 가진 사용자 조회 (훨씬 간단해짐!)
    List<UserDummy> findByRole(UserRole role);

    //ID로 사용자 조회
    Optional<UserDummy> findByUserId(Long id);

    // 교수 권한이 있는 사용자만 조회 (간단해짐!)
    @Query("SELECT ud FROM UserDummy ud WHERE ud.userId = :userId AND ud.role = 'PROFESSOR'")
    Optional<UserDummy> findProfessorById(@Param("userId") Long userId);

    //이메일 존재 여부 확인
    boolean existsByEmail(String email);

    // 닉네임 존재 여부 확인
    boolean existsByNickname(String nickname);
}


