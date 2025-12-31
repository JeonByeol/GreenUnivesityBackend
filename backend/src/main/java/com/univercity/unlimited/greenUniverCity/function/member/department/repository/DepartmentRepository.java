package com.univercity.unlimited.greenUniverCity.function.member.department.repository;

import com.univercity.unlimited.greenUniverCity.function.member.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDeptName(String deptName);
}

