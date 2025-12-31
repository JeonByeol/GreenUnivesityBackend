package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.repository;

import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentStatusHistoryRepository extends JpaRepository<StudentStatusHistory, Long> {
    @Query("select ssh from StudentStatusHistory ssh join ssh.user u where u.email = :email")
    List<StudentStatusHistory> findByUserEmail(@Param("email") String email);
}
