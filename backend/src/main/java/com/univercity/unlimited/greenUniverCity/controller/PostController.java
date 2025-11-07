//package com.univercity.unlimited.greenUniverCity.controller;
//
//import com.univercity.unlimited.greenUniverCity.entity.Course;
//import com.univercity.unlimited.greenUniverCity.entity.Post;
//import com.univercity.unlimited.greenUniverCity.service.PostService;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/community/post")
//public class PostController {
//
//    @GetMapping("/All")
//    public List<Post> postAllSearch(){
//        log.info("모든 강의들의 정보 호출");
//        return PostService.findAllPost();
//    }
//    @GetMapping("/Search")
//    public List<Post> postSearch() {
//        log.info("특정 회원의 정보 호출");
//        return PostService.findById();
//    }
//}
