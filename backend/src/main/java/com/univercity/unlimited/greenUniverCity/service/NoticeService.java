package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.NoticeDTO;
import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.entity.Department;
import com.univercity.unlimited.greenUniverCity.entity.Notice;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoticeService {
    //강의 아이디,강의코드,강의명 전체조회
    //교수id조회
    //전체 강의 데이터 조회
    List<Notice> findAllNotice();
    //특정강의id와 강의명조회
    List<Course> findPartCourse( String course_id,String course_name);

    ResponseEntity<String> addNotice(NoticeDTO noticeDTO);
}
