package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeService noticeService;
    //확인(학생)
    //해당강의 아이디,과목명,과목코드 들어옴/교수id들어옴

    //전체강의 조회
    @GetMapping("/all")
    public List<Course> postmanTestCourse(){
        log.info("모든 강의들의 정보 호출");
        return noticeService.findAllCourse();
    }
    //특정강의 조회
    @GetMapping("/all/partCourse")
    public List<Course> postmanTestPartCourse(@PathVariable("partCourse")String course_id,String course_name){
        log.info("특정강의 정보 호출");
        return noticeService.findPartCourse(course_id,course_name);
    }

    //작성(교수)
    //해당학과 아이디확인,교수id들어옴,/>>추후 필요:글 작성.등록

}
