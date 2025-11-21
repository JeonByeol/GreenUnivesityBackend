package com.univercity.unlimited.greenUniverCity.function.user.repository;

import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @EntityGraph(attributePaths = ("userRoleList"))
    @Query("select m from User m where email = :email" )
    //select * from tbl_user u inner join user_vo_user_role_list ru on u.uno=ru.user_vo_uno;
    User getUserByEmail(@Param("email") String email);
    @Query("SELECT u FROM User u JOIN u.userRoleList r WHERE r = :role")
    List<User> findAllByRole(@Param("role") UserRole role);

//    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.offerings o WHERE o.offeringId = :offeringId")
//    Optional<User> findByOfferingId(@Param("offeringId") Long offeringId);
//
//    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.enrollments e WHERE e.enrollmentId = :enrollmentId")
//    Optional<User> findByEnrollmentId(@Param("enrollmentId") Long enrollmentId);
    
    // 유저의 정보를 찾기 위한 쿼리 선언
    User findByUserId(Long id);

    //PROFESSOR 역할을 가진 사용자만 조회하기 위한 service선언에 활용될 쿼리
    @Query("SELECT u FROM User u JOIN u.userRoleList role WHERE u.userId = :userId AND role = 'PROFESSOR'")
    Optional<User> findProfessorById(@Param("userId") Long userId);

}
