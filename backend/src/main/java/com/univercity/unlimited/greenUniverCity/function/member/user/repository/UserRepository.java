package com.univercity.unlimited.greenUniverCity.function.member.user.repository;

import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT m FROM User m WHERE m.email = :email")
    User getUserByEmail(@Param("email") String email);

    @Query("""
        SELECT u FROM User u 
        JOIN u.userRoleList role 
        WHERE role = :role
    """)
    List<User> findAllByRole(@Param("role") UserRole role);

    User findByUserId(Long id);

    @Query("""
        SELECT u FROM User u 
        JOIN u.userRoleList role 
        WHERE u.userId = :userId 
          AND role = com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole.PROFESSOR
    """)
    Optional<User> findProfessorById(Long userId);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoleList WHERE u.email = :email")
    User getWithRole(@Param("email") String email);
}
