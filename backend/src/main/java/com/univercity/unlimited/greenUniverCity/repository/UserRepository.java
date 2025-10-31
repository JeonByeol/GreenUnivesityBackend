package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserVo,String> {

    @EntityGraph(attributePaths = ("userRoleList"))
    @Query("select m from UserVo m where email = :email" )
    //select * from tbl_user u inner join user_vo_user_role_list ru on u.uno=ru.user_vo_uno;
    UserVo getUserByEmail(@Param("email") String email);

//   |@Query(...) 어노테이션이 아예 필요 없습니다.| UserVo findByEmail(String email);
}
