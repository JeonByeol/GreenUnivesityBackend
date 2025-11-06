package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserVo,Long> {

    @EntityGraph(attributePaths = ("userRoleList"))
    @Query("select m from UserVo m where email = :email" )
    //select * from tbl_user u inner join user_vo_user_role_list ru on u.uno=ru.user_vo_uno;
    UserVo getUserByEmail(@Param("email") String email);
    @Query("SELECT u FROM UserVo u JOIN u.userRoleList r WHERE r = :role")
    List<UserVo> findAllByRole(@Param("role") UserRole role);

    //   |@Query(...) 어노테이션이 아예 필요 없습니다.| UserVo findByEmail(String email);
    // 이거 뭐에요

    @Query("SELECT DISTINCT u FROM UserVo u JOIN FETCH u.offerings o WHERE o.offeringId = :offeringId")
    Optional<UserVo> findByOfferingId(@Param("offeringId") Long offeringId);

    @Query("SELECT DISTINCT u FROM UserVo u JOIN FETCH u.enrollments e WHERE e.enrollmentId = :enrollmentId")
    Optional<UserVo> findByEnrollmentId(@Param("enrollmentId") Long enrollmentId);

    @Query("select m from UserVo m where id = :id" )
    UserVo findById(@Param("id") String id);

//   |@Query(...) 어노테이션이 아예 필요 없습니다.| UserVo findByEmail(String email);
}
