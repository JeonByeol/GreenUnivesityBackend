package com.univercity.unlimited.greenUniverCity.function.academic.classroom.service;

import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomUpdateDTO;

import java.util.List;

public interface ClassroomService {
    //주석-1) ClassroomController=CR

    //CR-1(모두) CR에 선언된 postmanTestClassroom의 요청을 받아서 클래스룸 테이블 전체 데이터를 조회하기 위해 동작하는 서비스 선언
    List<ClassroomResponseDTO> findAllClassroom();

    //CR-2(모두) CR에 선언된 postmanTest --의 요청을 받아서 특정 장소에 대한 검색을 하여 조회하기 위해 동작하는 서비스 선언
    //예: "공학관" 검색 -> [공학관 101호, 공학관 102호, ...] 반환
    List<ClassroomResponseDTO> findByLocation(String keyword);

    //CR-3(교수OR관리자) CR에 선언된 postmanTest -- 의 요청을 받아서 새로운 강의실을 작성하기 위해 동작하는 서비스 선언
    ClassroomResponseDTO createClassroom(ClassroomCreateDTO dto,String email);

    //CR-4(교수OR관리자) CR에 선언된 postmanTest --의 요청을 받아서 기존 존재하는 강의실의 정보가(수용인원등) 변경 시키기 위해 동작하는 서비스 선언
    ClassroomResponseDTO updateClassroom(ClassroomUpdateDTO dto,String email);

    //CR-5(교수OR관리자) CR에 선언된 postmanTest --의 요청을 받아서 강의실이 폐쇄 될때 삭제하기 위해 동작하는 서비스 선언
    void deleteByClassroom(Long classroomId,String email);
}
