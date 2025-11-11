package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Notice;
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
    public List<Notice> postmanTestNotice(){
        log.info("모든 공지들의 정보 호출");
        return noticeService.findAllNotice();
    }

    //특정강의 조회
//    @GetMapping("/all/partCourse")
//    public List<Course> postmanTestPartCourse(@PathVariable("partCourse")String course_id,String course_name){
//        log.info("특정강의 정보 호출");
//        return noticeService.findPartCourse(course_id,course_name);
//    }
}
