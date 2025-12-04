package com.univercity.unlimited.greenUniverCity.function.academic.classroom.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
    //CR-2 특정 강의실에 대한 정보를 조회하는 서비스 구현부에서 location의 값을 전달하기 위해 쿼리문 작성
    List<Classroom> findByLocationContain(String keyword);
}
